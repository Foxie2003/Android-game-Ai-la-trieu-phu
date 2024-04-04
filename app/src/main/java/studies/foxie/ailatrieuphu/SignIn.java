package studies.foxie.ailatrieuphu;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class SignIn extends AppCompatActivity {
    private int RC_DEFAULT_SIGN_IN = 1000;
    private Button button;
    FirebaseAuth auth;
    FirebaseDatabase database;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        button = findViewById(R.id.button);
        //Khởi tạo đối tượng Firebase Auth để thực hiện việc xác thực người dùng
        auth = FirebaseAuth.getInstance();
        //Khởi tạo đối tượng FirebaseDatabase để thực hiện lưu trữ thông tin người dùng
        database = FirebaseDatabase.getInstance();

        //Khởi tạo đối tượng GoogleSignInOptions để yêu cầu việc xác thực bằng tk google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("711718485134-v9ar981u99noh6esc73rkutqqokkhm7d.apps.googleusercontent.com")
                .requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        //Thiết lập sự kiện khi bấm nút đăng nhập
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    //Hàm thực hiện chức năng đăng nhập
    private void signIn() {
        //Yêu cầu intent đăng nhập của google
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, RC_DEFAULT_SIGN_IN);
    }

    //Xử lý kết quả được trả về từ intent đăng nhập của google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Nếu request code là của trường hợp đăng nhập
        if(requestCode == RC_DEFAULT_SIGN_IN) {
            //Lấy thông tin tài khoản mà người dùng đã chọn
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Sử dụng token để xác thực với Firebase
                firebaseAuth(account.getIdToken());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Hàm xác thực Firebase Authentication bằng token được cung cấp bởi Google
    private void firebaseAuth(String idToken) {
        // Tạo credential từ token
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        // Thực hiện đăng nhập vào Firebase bằng credential
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // Nếu đăng nhập thành công, lấy thông tin người dùng và lưu vào Realtime Database
                            FirebaseUser user = auth.getCurrentUser();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", user.getUid());
                            map.put("name", user.getDisplayName());
                            map.put("profile", user.getPhotoUrl().toString());

                            // Lưu thông tin người dùng vào Realtime Database
                            database.getReference().child("users").child(user.getUid())
                                    .setValue(map);

                            // Chuyển sang màn hình chơi
                            Intent intent = new Intent(SignIn.this, PlayActivity.class);
                            startActivity(intent);
                        }
                        else {
                            // Nếu đăng nhập không thành công, hiển thị thông báo lỗi
                            Toast.makeText(SignIn.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}