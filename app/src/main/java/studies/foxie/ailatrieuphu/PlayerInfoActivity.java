package studies.foxie.ailatrieuphu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerInfoActivity extends AppCompatActivity {
    private static DB database;
    private CategoryPagerAdapter adapter;
    private static ImageView ivAvatar;
    private TextView tvName, tvMoney, tvDiamond, tvTaiSan, tvCorrectQuestion;
    private ImageButton btnBack;
    private AppCompatButton btnChangeName, btnShare, btnSignIn, btnSignOut;
    private int RC_DEFAULT_SIGN_IN = 1000;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ẩn thanh tiêu đề
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Đặt cờ cho cửa sổ
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideNavigationBar();
        setContentView(R.layout.activity_player_info);
        // Ánh xạ các thành phần trong layout
        ivAvatar = findViewById(R.id.iv_player_info_avatar);
        tvName = findViewById(R.id.tv_player_info_name);
        tvMoney = findViewById(R.id.tv_player_info_money);
        tvDiamond = findViewById(R.id.tv_player_info_diamond);
        tvTaiSan = findViewById(R.id.tv_player_info_tai_san);
        tvCorrectQuestion = findViewById(R.id.tv_player_info_correct_question);
        btnBack = findViewById(R.id.ibtn_player_info_back);
        btnChangeName = findViewById(R.id.btn_player_info_change_name);
        btnShare = findViewById(R.id.btn_player_info_share);
        btnSignIn = findViewById(R.id.btn_player_info_sign_in);
        btnSignOut = findViewById(R.id.btn_player_info_sign_out);

        //Khởi tạo đối tượng database
        database = new DB(PlayerInfoActivity.this.getApplicationContext());

        ViewPager viewPager = findViewById(R.id.vp_player_info_viewpager);
        TabLayout tabLayout = findViewById(R.id.tl_player_info_tablayout);

        adapter = new CategoryPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentTaiSan2(), "Tài sản");
        adapter.addFragment(new FragmentAvatar2(), "Avatar");
        adapter.addFragment(new FragmentKhung2(), "Khung");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setupCustomTabLayout();

        btnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeNameDialog();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        showAvatar(this);
        showPlayerInfo();
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareLinkApp();
            }
        });
        //Khởi tạo đối tượng Firebase Auth để thực hiện việc xác thực người dùng
        auth = FirebaseAuth.getInstance();
        //Khởi tạo đối tượng FirebaseDatabase để thực hiện lưu trữ thông tin người dùng
        firebaseDatabase = FirebaseDatabase.getInstance();

        //Khởi tạo đối tượng GoogleSignInOptions để yêu cầu việc xác thực bằng tk google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("711718485134-v9ar981u99noh6esc73rkutqqokkhm7d.apps.googleusercontent.com")
                .requestEmail().build();
        gsc = GoogleSignIn.getClient(PlayerInfoActivity.this, gso);
        // Kiểm tra trạng thái đăng nhập của người dùng
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            // Người dùng đã đăng nhập
            Toast.makeText(this, "UserId: " + currentUser.getUid(), Toast.LENGTH_SHORT).show();
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
        } else {
            // Người dùng chưa đăng nhập
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
        }
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
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
    private void setupCustomTabLayout() {
        TabLayout tabLayout = findViewById(R.id.tl_player_info_tablayout);

        // Loop through each tab and set custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.custom_tab_layout);

                // Get custom view of the tab
                View tabView = tab.getCustomView();
                if (tabView != null) {
                    ImageView imageView = tabView.findViewById(R.id.iv_tab_image);
                    switch (i) {
                        case (0):
                            imageView.setImageResource(R.drawable.bg_category_tai_san);
                            break;
                        case (1):
                            imageView.setImageResource(R.drawable.bg_category_avatar);
                            break;
                        case (2):
                            imageView.setImageResource(R.drawable.bg_category_khung);
                            break;
                    }
                }
            }
        }
    }
    public static void showAvatar(Context context) {
        ArrayList<ShopItem> avatars = database.getItemsByCategory(2);
        ArrayList<ShopItem> frames = database.getItemsByCategory(3);
        avatars.forEach(item -> {
            if(item.getId() == database.getUsingAvatarId()) {
                // Lấy id của hình ảnh từ tên hình ảnh trong drawable
                int resourceId = context.getResources().getIdentifier(item.getImage(), "drawable", context.getPackageName());
                // Kiểm tra xem id của hình ảnh có hợp lệ không
                if (resourceId != 0) {
                    // Nếu id hợp lệ, đặt hình ảnh vào ImageView
                    ivAvatar.setImageResource(resourceId);
                } else {
                    // Nếu không tìm thấy hình ảnh, xử lý theo nhu cầu của bạn, ví dụ đặt một hình ảnh mặc định
                    //                holder.ivImage.setImageResource(R.drawable.default_image);
                }
            }
        });
        frames.forEach(item -> {
            if(item.getId() == database.getUsingFrameId()) {
                // Lấy id của hình ảnh từ tên hình ảnh trong drawable
                int resourceId = context.getResources().getIdentifier(item.getImage(), "drawable", context.getPackageName());
                // Kiểm tra xem id của hình ảnh có hợp lệ không
                if (resourceId != 0) {
                    // Nếu id hợp lệ, đặt hình ảnh vào ImageView
                    ivAvatar.setBackgroundResource(resourceId);
                } else {
                    // Nếu không tìm thấy hình ảnh, xử lý theo nhu cầu của bạn, ví dụ đặt một hình ảnh mặc định
                    //                holder.ivImage.setImageResource(R.drawable.default_image);
                }
            }
        });
    }
    private void showPlayerInfo() {
        PlayerInfo playerInfo = database.getPlayerInfo();
        tvName.setText(playerInfo.getPlayerName());
        tvMoney.setText(DB.formatNumber(playerInfo.getMoney()));
        tvDiamond.setText(DB.formatNumber(playerInfo.getDiamond()));
        int taiSan = 0;
        ArrayList<ShopItem> taiSans = database.getBoughtItemsByCategory(1);
        for (int i = 0; i < taiSans.size(); i++) {
            taiSan += taiSans.get(i).getPrice();
        }
        tvTaiSan.setText(DB.formatNumber(taiSan));
        tvCorrectQuestion.setText(DB.formatNumber(playerInfo.getCorrectAnsweredQuestion()));
    }
    private void showChangeNameDialog() {
        Dialog dialog = new Dialog(PlayerInfoActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_change_name);
        dialog.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
//        TextView tvClose = dialog.findViewById(R.id.tv_change_name_close);
        EditText edtName = dialog.findViewById(R.id.edt_change_name_name);
        AppCompatButton btnChangeName = dialog.findViewById(R.id.btn_change_name_change);
        AppCompatButton btnBack = dialog.findViewById(R.id.btn_change_name_back);
//        RelativeLayout layoutWrap = dialog.findViewById(R.id.layout_change_name_wrap);
        edtName.setText(database.getPlayerName());
        btnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(PlayerInfoActivity.this, "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
                }
                else {
                    database.setPlayerName(edtName.getText().toString());
                    showPlayerInfo();
                    Toast.makeText(PlayerInfoActivity.this, "Đổi tên thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Khi bấm chữ trở về thì đóng dialog
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Thoát dialog
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void ShareLinkApp(){
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Thay đổi thành URI của liên kết bạn muốn chia sẻ
        Uri uri = Uri.parse("https://drive.google.com/file/d/1QEVftkdZeqsZY1SI2PUKyQ3hPVmCIuSl/view?usp=sharinga");

        // Đặt nội dung của Intent thành liên kết
        intent.putExtra(Intent.EXTRA_TEXT, uri.toString());

        // Thêm cờ cho phép ứng dụng đính kèm xử lý dữ liệu từ URI được cung cấp
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Đặt loại dữ liệu của Intent thành "text/plain"
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent,"Share to..."));
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
                        PlayerInfo playerInfo = database.getPlayerInfo();
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("id", user.getUid());
                        map.put("playerName", playerInfo.getPlayerName());
                        map.put("avatarId", playerInfo.getAvatarId());
                        map.put("frameId", playerInfo.getFrameId());
                        map.put("money", playerInfo.getMoney());
                        map.put("diamond", playerInfo.getDiamond());
                        map.put("highestQuestionNumber", playerInfo.getHighestQuestionNumber());
                        map.put("answeredQuestion", playerInfo.getAnsweredQuestion());
                        map.put("correctAnsweredQuestion", playerInfo.getCorrectAnsweredQuestion());
                        map.put("boughtItems", playerInfo.getBoughtItems());

                        //Kiểm tra xem thông tin người dùng đã tồn tại chưa
                        //ListenerForSingleValueEvent sẽ đọc dữ liệu một lần duy nhất
                        firebaseDatabase.getReference().child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //Nếu thông tin người dùng đã tồn tại
                                if (snapshot.exists()) {
                                    Toast.makeText(PlayerInfoActivity.this, "Old player", Toast.LENGTH_SHORT).show();
                                    database.updatePlayerInfoOnDatabase();
                                }
                                //Nếu thông tin người dùng chưa tồn tại
                                else {
                                    Toast.makeText(PlayerInfoActivity.this, "New player", Toast.LENGTH_SHORT).show();
                                    // Lưu thông tin người dùng vào Realtime Database
                                    firebaseDatabase.getReference().child("users").child(user.getUid())
                                            .setValue(map);
                                }
                                Toast.makeText(PlayerInfoActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                PlayerInfoActivity.this.finish();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Xử lý khi có lỗi xảy ra
                                Log.w("TAG", "loadPost:onCancelled", error.toException());
                            }
                        });
                    }
                    else {
                        // Nếu đăng nhập không thành công, hiển thị thông báo lỗi
                        Toast.makeText(PlayerInfoActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
    private void signOut() {
        // Đăng xuất khỏi Firebase
        auth.signOut();
        // Đăng xuất khỏi Google
        gsc.signOut().addOnCompleteListener(this, task -> {
            Toast.makeText(PlayerInfoActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
        });
        // Xóa dữ liệu của csdl và tạo lại
        database.recreateDatabase();
        this.finish();
    }
}