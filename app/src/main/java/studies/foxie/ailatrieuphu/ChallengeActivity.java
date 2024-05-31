package studies.foxie.ailatrieuphu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Random;

public class ChallengeActivity extends AppCompatActivity {
    private String userId;
    private String roomId;
    private Button btnFindMatch, btnCreateRoom, btnFindRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ẩn thanh tiêu đề
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Đặt cờ cho cửa sổ
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideNavigationBar();
        setContentView(R.layout.activity_challenge);

        //Ánh xạ các thành phần trong layout
        btnFindMatch = findViewById(R.id.btn_challenge_find_match);
        btnCreateRoom = findViewById(R.id.btn_challenge_create_room);
        btnFindRoom = findViewById(R.id.btn_challenge_find_room);

        //Lấy userId của người đang đăng nhập
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Bắt sự kiện khi bấm vào nút 'Tìm đối thủ'
        btnFindMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChallengeActivity.this, ChallengeGameActivity.class);
                intent.putExtra("mode", 1);
                startActivity(intent);
            }
        });
        //Bắt sự kiện khi bấm vào nút 'Tạo phòng'
        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chuyển đến giao diện phòng chơi của chế độ thách đấu
                Intent intent = new Intent(ChallengeActivity.this, ChallengeGameActivity.class);
                intent.putExtra("mode", 2);
                startActivity(intent);
            }
        });
        //Bắt sự kiện khi bấm vào nút 'Tìm phòng'
        btnFindRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEnterRoomDialog();
            }
        });
    }
    //Hàm ẩn thanh công cụ navigation bar
    private void hideNavigationBar() {
        // Ẩn thanh công cụ (navigation bar)
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }
    private void showEnterRoomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_enter_room, null);
        builder.setView(dialogView);

        EditText roomNumberEditText = dialogView.findViewById(R.id.roomNumberEditText);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton);

        AlertDialog dialog = builder.create();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomNumber = roomNumberEditText.getText().toString();

                if (!TextUtils.isEmpty(roomNumber) && roomNumber.length() == 6) {
                    // Handle room number input (e.g., search for room)
                    dialog.dismiss();
                    //Chuyển đến giao diện phòng chơi của chế độ thách đấu
                    Intent intent = new Intent(ChallengeActivity.this, ChallengeGameActivity.class);
                    intent.putExtra("mode", 3);
                    intent.putExtra("roomId", roomNumber);
                    startActivity(intent);
                } else {
                    Toast.makeText(ChallengeActivity.this, "Please enter a valid 6-digit room number.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }
}
