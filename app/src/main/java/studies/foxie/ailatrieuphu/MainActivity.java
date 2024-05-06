package studies.foxie.ailatrieuphu;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.SeekBar;
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
import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private DB database;
    FirebaseAuth auth;
    private SoundManager backgroundSoundManager, soundEffectManager;
    private TextView tvMoney, tvDiamond, tvName;
    private ImageView ivAvatar, ivMainShop, ivSettings, ivExit;
    private ImageButton ibtnPlay;
    SharedPreferences prefs;
    private boolean music;
    private boolean sound;
    private SeekBar seekBarMusic,seekBarSound;
    float volumnMusic,volumnSound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ẩn thanh tiêu đề
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Đặt cờ cho cửa sổ
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideNavigationBar();
        setContentView(R.layout.activity_main);

        // Ánh xạ các thành phần trong layout
        tvMoney = findViewById(R.id.tv_main_money);
        tvDiamond = findViewById(R.id.tv_main_diamond);
        ivAvatar = findViewById(R.id.iv_main_avatar);
        ibtnPlay = findViewById(R.id.ibtn_main_play);
        ivMainShop = findViewById(R.id.iv_main_shop);
        ivSettings = findViewById(R.id.iv_main_settings);
        ivExit = findViewById(R.id.iv_main_exit);
        tvName = findViewById(R.id.tv_main_player_name);

        //Khởi tạo đối tượng database và khởi tạo database
        database = new DB(MainActivity.this.getApplicationContext());
        database.createDatabase();

        //Khởi tạo đối tượng Firebase Auth để xác thực người dùng
        auth = FirebaseAuth.getInstance();

        //Lấy dữ liệu cài đặt
        prefs= getSharedPreferences("game", MODE_PRIVATE);
        music = prefs.getBoolean("isMusicOn", true);
        sound = prefs.getBoolean("isSoundOn", true);
        volumnMusic = prefs.getFloat("volumnMusic",1);
        volumnSound = prefs.getFloat("volumnSound",1);

        //Khởi tạo đối tượng SoundManager để phát âm thanh
        backgroundSoundManager = new SoundManager(MainActivity.this);
        soundEffectManager = new SoundManager(MainActivity.this);

        //Phát âm thanh chào mừng
        soundEffectManager.playSound(R.raw.mc_wellcome, volumnSound);
        soundEffectManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                backgroundSoundManager.playSoundLoop(R.raw.bg_music_menu, volumnMusic);
                soundEffectManager.removeOnCompletionListener();
            }
        });
        //Hiện thị số tiền đang có của người chơi
        showPlayerMoney();
        //Hiện thị số kim cương đang có của người chơi
        showPlayerDiamond();
        showAvatar();
        tvName.setText(database.getPlayerName());
        //Bắt sự kiện khi ấn nút "bắt đầu"
        ibtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundEffectManager.playSound(R.raw.bg_select_ans, volumnSound);
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
        //Bắt sự kiện khi ấn nút "cửa hàng"
        ivMainShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShopActivity.class));
            }
        });
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showPlayerInfoDialog();
                startActivity(new Intent(MainActivity.this, ActivityPlayerInfo.class));
            }
        });
        //Bắt sự kiện khi ấn nút "cài đặt"
        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingsDialog();
            }
        });
        //Bắt sự kiện khi ấn nút "thoát"
        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitDialog();
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
        showPlayerDiamond();
        showAvatar();
        tvName.setText(database.getPlayerName());
    }

    //Khi activity có sự thay đổi về focus
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //Ẩn thanh công cụ
        hideNavigationBar();
    }

    //Hàm ẩn thanh công cụ navigation bar
    private void hideNavigationBar() {
        // Ẩn thanh công cụ (navigation bar)
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }
    //Hàm hiển thị số tiền đang có của người chơi
    private void showPlayerMoney() {
        tvMoney.setText(database.getStringMoney());
    }
    //Hàm hiển thị số kim cương đang có của người chơi
    private void showPlayerDiamond() {
        tvDiamond.setText(String.valueOf(database.getDiamond()));
    }
    //Hàm hiển thị dialog thông tin người chơi
    private void showPlayerInfoDialog() {
        //Hiển thị dialog thông tin người chơi
        Dialog playerInfoDialog = new Dialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        playerInfoDialog.setContentView(R.layout.dialog_player_info);
        playerInfoDialog.setCancelable(false);
        playerInfoDialog.show();
        //Ẩn thanh công cụ
        playerInfoDialog.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        TextView tvAnsweredQuestion = playerInfoDialog.findViewById(R.id.tv_player_info_answered_question);
        TextView tvCorrectAnsweredQuestion = playerInfoDialog.findViewById(R.id.tv_player_info_correct_answered_question);
        AppCompatButton btnSignIn = playerInfoDialog.findViewById(R.id.btn_player_info_sign_in);
        AppCompatButton btnSignOut = playerInfoDialog.findViewById(R.id.btn_player_info_sign_out);
        AppCompatButton btnClose = playerInfoDialog.findViewById(R.id.btn_player_info_close);
        //Hiển thị số câu đã trả lời và số câu đã trả lời đúng
        tvAnsweredQuestion.setText("Số câu đã trả lời: " + database.getAnsweredQuestion());
        tvCorrectAnsweredQuestion.setText("Số câu đã trả lời đúng: " + database.getCorrectAnsweredQuestion());
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignIn.class));
            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(MainActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerInfoDialog.dismiss();
            }
        });
    }
    private void showSettingsDialog() {
        Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_settings);
        dialog.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView tvClose = dialog.findViewById(R.id.tv_settings_close);
        TextView tvMusicValue = dialog.findViewById(R.id.tv_settings_music_value);
        TextView tvSoundValue = dialog.findViewById(R.id.tv_settings_sound_value);
        RelativeLayout layoutWrap = dialog.findViewById(R.id.layout_settings_wrap);
        ImageView ivMusic = dialog.findViewById(R.id.iv_settings_music);
        ImageView ivSound = dialog.findViewById(R.id.iv_settings_sound);
        seekBarMusic = dialog.findViewById(R.id.sb_setting_music);
        seekBarSound = dialog.findViewById(R.id.sb_setting_sound);
        if(music) {
            ivMusic.setImageResource(R.drawable.icon_music);
        }
        else {
            ivMusic.setImageResource(R.drawable.icon_music_mute);
        }

        if(sound) {
            ivSound.setImageResource(R.drawable.icon_sound);
        }
        else {
            ivSound.setImageResource(R.drawable.icon_sound_mute);
        }

        seekBarMusic.setMax(100);
        int progress1 = (int) (100 - Math.pow(10, (1 - volumnMusic) * Math.log10(100)));
        seekBarMusic.setProgress(progress1);
        tvMusicValue.setText(String.valueOf(progress1));

        seekBarSound.setMax(100);
        int progress2 = (int) (100 - Math.pow(10, (1 - volumnSound) * Math.log10(100)));
        seekBarSound.setProgress(progress2);
        tvSoundValue.setText(String.valueOf(progress2));

        // nhạc nền
        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress!=0){
                    ivMusic.setImageResource(R.drawable.icon_music);
                    music=true;
                }
                else {
                    ivMusic.setImageResource(R.drawable.icon_music_mute);
                    music=false;
                }
                tvMusicValue.setText(String.valueOf(progress));
                volumnMusic = (float) (1 - (Math.log(100 - progress) / Math.log(100)));
                backgroundSoundManager.mediaPlayer.setVolume(volumnMusic, volumnMusic); // Thiết lập âm lượng của MediaPlayer
                SharedPreferences.Editor editor = prefs.edit();
                editor.putFloat("volumnMusic", volumnMusic);
                editor.putBoolean("isMusicOn",music);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        //tiếng lại văn sâm
        seekBarSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress!=0){
                    ivSound.setImageResource(R.drawable.icon_sound);
                    sound=true;
                }
                else {
                    ivSound.setImageResource(R.drawable.icon_sound_mute);
                    sound=false;
                }
                tvSoundValue.setText(String.valueOf(progress));
                volumnSound = (float) (1 - (Math.log(100 - progress) / Math.log(100)));
                soundEffectManager.mediaPlayer.setVolume(volumnSound, volumnSound);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putFloat("volumnSound", volumnSound);
                editor.putBoolean("isSoundOn",sound);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        //Khi bấm chữ trở về thì đóng dialog
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Thoát dialog
                dialog.dismiss();
            }
        });
        layoutWrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Thoát dialog
                dialog.dismiss();
            }
        });
        //Áp dụng animation cho chữ
        Animation fadeInAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.growing_light);
        tvClose.startAnimation(fadeInAnimation);

        dialog.show();
    }

    //Hàm hiện dialog thoát trò chơi
    public void showExitDialog() {
        //Tạo dialog thoát trò chơi
        Dialog exitDialog = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        exitDialog.setContentView(R.layout.dialog_exit);
        exitDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        exitDialog.show();
        //Ẩn thanh công cụ
        exitDialog.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        AppCompatButton btnExitYes = exitDialog.findViewById(R.id.btn_exit_yes);
        AppCompatButton btnExitNo = exitDialog.findViewById(R.id.btn_exit_no);
        RelativeLayout layoutWrap = exitDialog.findViewById(R.id.layout_exit_wrap);
        TextView tvClose = exitDialog.findViewById(R.id.tv_exit_close);

        //Khi bấm chữ thoát thì thoát trò chơi
        btnExitYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });
        //Khi bấm chữ hủy thì đóng dialog
        btnExitNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Thoát dialog
                exitDialog.dismiss();
            }
        });
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Thoát dialog
                exitDialog.dismiss();
            }
        });
        layoutWrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Thoát dialog
                exitDialog.dismiss();
            }
        });
        //Áp dụng animation cho chữ
        Animation growingLight = AnimationUtils.loadAnimation(MainActivity.this, R.anim.growing_light);
        tvClose.startAnimation(growingLight);
    }
    public void showAvatar() {
        ArrayList<ShopItem> avatars = database.getItemsByCategory(2);
        ArrayList<ShopItem> frames = database.getItemsByCategory(3);
        avatars.forEach(item -> {
            if(item.getId() == database.getUsingAvatarId()) {
                // Lấy id của hình ảnh từ tên hình ảnh trong drawable
                int resourceId = MainActivity.this.getResources().getIdentifier(item.getImage(), "drawable", MainActivity.this.getPackageName());
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
                int resourceId = MainActivity.this.getResources().getIdentifier(item.getImage(), "drawable", MainActivity.this.getPackageName());
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
}