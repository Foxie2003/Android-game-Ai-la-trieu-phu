package studies.foxie.ailatrieuphu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SoundManager backgroundSoundManager, soundEffectManager;
    AppCompatButton btnStartGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ẩn thanh tiêu đề
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Đặt cờ cho cửa sổ
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Ẩn thanh công cụ (navigation bar)
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_main);
        // Ánh xạ các thành phần trong layout
        ImageView imageView = findViewById(R.id.iv_main_light_effect);
        btnStartGame = findViewById(R.id.btn_main_start_game);
        //Khởi tạo đối tượng SoundManager để phát âm thanh
        backgroundSoundManager = new SoundManager(MainActivity.this);
        soundEffectManager = new SoundManager(MainActivity.this);

        //Chạy animation của hiệu ứng phát sáng
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.growing_light);
        imageView.startAnimation(animation);
        //Phát âm thanh nền trong menu
        backgroundSoundManager.playSound(R.raw.mc_wellcome);
        backgroundSoundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                backgroundSoundManager.playSoundLoop(R.raw.bg_music_menu);

            }
        });
        //Bắt sự kiện khi ấn nút "bắt đầu"
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundEffectManager.playSound(R.raw.bg_select_ans);
                soundEffectManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        soundEffectManager.removeOnCompletionListener();
                        Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Giải phóng MediaPlayer khi activity bị destroy
        if (backgroundSoundManager != null) {
            backgroundSoundManager.stopSound();
        }
        if (soundEffectManager != null) {
            soundEffectManager.stopSound();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Dừng nhạc nếu người dùng ẩn ra khỏi trò chơi
        if (backgroundSoundManager != null) {
            backgroundSoundManager.pauseSound();
        }
        if (soundEffectManager != null) {
            soundEffectManager.pauseSound();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Tiếp tục phát nhạc nếu người dùng ẩn ra khỏi trò chơi
        if (backgroundSoundManager != null) {
            backgroundSoundManager.resumeSound();
        }
        if (soundEffectManager != null) {
            soundEffectManager.resumeSound();
        }
    }

}