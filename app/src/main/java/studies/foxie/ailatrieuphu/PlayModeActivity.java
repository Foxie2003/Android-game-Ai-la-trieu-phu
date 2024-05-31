package studies.foxie.ailatrieuphu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class PlayModeActivity extends AppCompatActivity {
    private Button btnClassicMode, btnChallengeMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ẩn thanh tiêu đề
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Đặt cờ cho cửa sổ
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideNavigationBar();
        setContentView(R.layout.activity_play_mode);

        //Ánh xạ các thành phần trong layout
        btnClassicMode = findViewById(R.id.btn_play_mode_classic);
        btnChallengeMode = findViewById(R.id.btn_play_mode_challenge);

        //Bắt sự kiện khi bấm vào nút 'cổ điển'
        btnClassicMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chuyển đến chế độ chơi cổ điển
                Intent intent = new Intent(PlayModeActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
        //Bắt sự kiện khi bấm vào nút 'thách đấu'
        btnChallengeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chuyển đến giao diện chế độ thách đấu
                Intent intent = new Intent(PlayModeActivity.this, ChallengeActivity.class);
                startActivity(intent);
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
}