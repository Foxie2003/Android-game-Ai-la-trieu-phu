package studies.foxie.ailatrieuphu;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;

public class MainActivity extends AppCompatActivity {
    private DB database;
    private SoundManager backgroundSoundManager, soundEffectManager;
    private TextView tvMoney;
    private ImageView ivAvatar;
    private ImageButton ibtnPlay;
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
        tvMoney = findViewById(R.id.tv_main_money);
        ivAvatar = findViewById(R.id.iv_main_avatar);
        ibtnPlay = findViewById(R.id.ibtn_main_play);

        //Khởi tạo đối tượng database và khởi tạo database
        database = new DB(MainActivity.this.getApplicationContext());
        database.createDatabase();

        //Khởi tạo đối tượng SoundManager để phát âm thanh
        backgroundSoundManager = new SoundManager(MainActivity.this);
        soundEffectManager = new SoundManager(MainActivity.this);

        //Phát âm thanh nền trong menu
        backgroundSoundManager.playSound(R.raw.mc_wellcome);
        backgroundSoundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                backgroundSoundManager.playSoundLoop(R.raw.bg_music_menu);
            }
        });
        //Hiện thị số tiền đang có của người chơi
        showPlayerMoney();
        //Bắt sự kiện khi ấn nút "bắt đầu"
        ibtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundEffectManager.playSound(R.raw.bg_select_ans);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Chuyển sang màn hình chơi game sau khi đủ thời gian trễ
                        Intent playIntent = new Intent(MainActivity.this, PlayActivity.class);
                        startActivity(playIntent);
                    }
                }, 400);
            }
        });
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignIn.class));
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
        showPlayerMoney();
    }

    //Hàm format định dạng tiền trong game
    public static String formatNumber(long num) {
        String[] suffixes = {"", " Nghìn", " Triệu", " Tỉ", " Nghìn tỉ"}; // Các đơn vị tiền tố
        int suffixIndex = 0;
        double formattedNumber = num;

        while (formattedNumber >= 1000 && suffixIndex < suffixes.length - 1) {
            formattedNumber /= 1000.0;
            suffixIndex++;
        }

        return String.format("%.1f%s", formattedNumber, suffixes[suffixIndex]);
    }

    //Hàm hiển thị số tiền đang có của người chơi
    private void showPlayerMoney() {
        tvMoney.setText(formatNumber(database.getMoney()));
    }
}