package studies.foxie.ailatrieuphu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.animation.ValueAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class PlayActivity extends AppCompatActivity {
    private DB database;
    private SoundManager soundManager;
    private GradientTextView tvCountdown;
    private ImageView imgCountdown;
    private CountDownTimer countDownTimer;
    private final long totalTimeInMillis = 30000; // 30 seconds
    private DrawerLayout layoutPlay;
    private LinearLayout layoutPlayMain, layoutAnswers, layoutLifelines;;
    private RelativeLayout layoutCountDown, layoutQuestion;
    private TextView tvQuestion, tvPlayQuestionNumber1, tvPlayQuestionNumber2, tvPlayQuestionNumber3,
            tvPlayQuestionNumber4, tvPlayQuestionNumber5, tvPlayQuestionNumber6, tvPlayQuestionNumber7,
            tvPlayQuestionNumber8, tvPlayQuestionNumber9, tvPlayQuestionNumber10, tvPlayQuestionNumber11,
            tvPlayQuestionNumber12, tvPlayQuestionNumber13, tvPlayQuestionNumber14, tvPlayQuestionNumber15;
    private AppCompatButton btnReady, btnQuestionNumber, btnAnswerA, btnAnswerB, btnAnswerC, btnAnswerD, btn5050;
    private ImageButton btnAskAudience, btnCall, btnChangeQuestion;
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
        setContentView(R.layout.activity_play);
        //Ánh xạ các thành phần trong layout
        layoutPlay = findViewById(R.id.layout_play);
        layoutPlayMain = findViewById(R.id.layout_play_main);
        layoutCountDown = findViewById(R.id.layout_countdown);
        layoutQuestion = findViewById(R.id.layout_question);
        layoutAnswers = findViewById(R.id.layout_answers);
        layoutLifelines = findViewById(R.id.layout_lifelines);
        tvCountdown = findViewById(R.id.tv_countdown);
        imgCountdown = findViewById(R.id.img_countdown);
        tvQuestion = findViewById(R.id.tv_play_question);
        btnReady = findViewById(R.id.btn_play_ready);
        btnQuestionNumber = findViewById(R.id.btn_play_question_number);
        btnAnswerA = findViewById(R.id.btn_play_answer_a);
        btnAnswerB = findViewById(R.id.btn_play_answer_b);
        btnAnswerC = findViewById(R.id.btn_play_answer_c);
        btnAnswerD = findViewById(R.id.btn_play_answer_d);
        btn5050 = findViewById(R.id.btn_5050);
        btnAskAudience = findViewById(R.id.btn_ask_audience);
        btnCall = findViewById(R.id.btn_call);
        btnChangeQuestion = findViewById(R.id.btn_change_question);
        tvPlayQuestionNumber1 = findViewById(R.id.tv_play_question_number_1);
        tvPlayQuestionNumber2 = findViewById(R.id.tv_play_question_number_2);
        tvPlayQuestionNumber3 = findViewById(R.id.tv_play_question_number_3);
        tvPlayQuestionNumber4 = findViewById(R.id.tv_play_question_number_4);
        tvPlayQuestionNumber5 = findViewById(R.id.tv_play_question_number_5);
        tvPlayQuestionNumber6 = findViewById(R.id.tv_play_question_number_6);
        tvPlayQuestionNumber7 = findViewById(R.id.tv_play_question_number_7);
        tvPlayQuestionNumber8 = findViewById(R.id.tv_play_question_number_8);
        tvPlayQuestionNumber9 = findViewById(R.id.tv_play_question_number_9);
        tvPlayQuestionNumber10 = findViewById(R.id.tv_play_question_number_10);
        tvPlayQuestionNumber11 = findViewById(R.id.tv_play_question_number_11);
        tvPlayQuestionNumber12 = findViewById(R.id.tv_play_question_number_12);
        tvPlayQuestionNumber13 = findViewById(R.id.tv_play_question_number_13);
        tvPlayQuestionNumber14 = findViewById(R.id.tv_play_question_number_14);
        tvPlayQuestionNumber15 = findViewById(R.id.tv_play_question_number_15);

        //Khởi tạo đối tượng database và khởi tạo database
        database = new DB(PlayActivity.this.getApplicationContext());
        database.createDatabase();

        //Khởi tạo đối tượng SoundManager để phát âm thanh
        soundManager = new SoundManager(PlayActivity.this);

        //Đặt ảnh gif làm bg cho ImageView
        Glide.with(this).asGif().load(R.drawable.countdown).into(imgCountdown);
        startCountdown();

        //Test load câu hỏi
        loadQuestion(database.getQuestionByQN(1));

        //Test phát âm thanh bg
        soundManager.playSound(R.raw.mc_rule);


        //Test hàm phổ biến luật chơi
        rulesTelling();

        //Bắt sự kiện khi ấn vào nút số câu hỏi thì hiện ra layout hiện thị danh sách câu hỏi
        btnQuestionNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuestionNumber();
            }
        });

    }

    private void openQuestionNumber() {
        layoutPlay.openDrawer(GravityCompat.END); // Mở layout ở phía bên phải
    }

    private void closeQuestionNumber() {
        layoutPlay.closeDrawer(GravityCompat.END); // Đóng layout ở phía bên phải
    }

    //Hàm phổ biến luật chơi khi bắt đầu ván chơi mới
    private void rulesTelling() {
        //Ẩn các layout, button khi chưa bắt đầu chơi
        layoutCountDown.setVisibility(View.INVISIBLE);
        layoutQuestion.setVisibility(View.INVISIBLE);
        layoutAnswers.setVisibility(View.INVISIBLE);
        btn5050.setVisibility(View.GONE);
        btnAskAudience.setVisibility(View.GONE);
        btnCall.setVisibility(View.GONE);
        btnChangeQuestion.setVisibility(View.GONE);
        //Ẩn backgound của layout trợ giúp
        layoutLifelines.setBackground(null);
        // Tạo một Handler để định thời gian trễ
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hiển thị layout sau khi đủ thời gian trễ
                openQuestionNumber();
            }
        }, 1000);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Highlight những câu hỏi quan trọng
                tvPlayQuestionNumber5.setBackgroundResource(R.drawable.question_selected);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvPlayQuestionNumber5.setBackgroundResource(R.drawable.question_normal);
                    }
                }, 400);
            }
        }, 4200);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Highlight những câu hỏi quan trọng
                tvPlayQuestionNumber10.setBackgroundResource(R.drawable.question_selected);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvPlayQuestionNumber10.setBackgroundResource(R.drawable.question_normal);
                    }
                }, 400);
            }
        }, 4600);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Highlight những câu hỏi quan trọng
                tvPlayQuestionNumber15.setBackgroundResource(R.drawable.question_selected);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvPlayQuestionNumber15.setBackgroundResource(R.drawable.question_normal);
                    }
                }, 400);
            }
        }, 5000);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Ẩn layout sau khi đủ thời gian trễ
                closeQuestionNumber();
            }
        }, 5600);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hiển thị trợ giúp sau khi đủ thời gian trễ
                btn5050.setVisibility(View.VISIBLE);
            }
        }, 7800);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hiển thị trợ giúp sau khi đủ thời gian trễ
                btnAskAudience.setVisibility(View.VISIBLE);
            }
        }, 9000);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hiển thị trợ giúp sau khi đủ thời gian trễ
                btnCall.setVisibility(View.VISIBLE);
            }
        }, 10200);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Chạy animation sau khi đủ thời gian trễ
                Animation shakeAnimation = AnimationUtils.loadAnimation(PlayActivity.this, R.anim.shaking);
                btnReady.startAnimation(shakeAnimation);
            }
        }, 10400);
    }

    //Hàm bắt đầu đếm giờ
    private void startCountdown() {
        countDownTimer = new CountDownTimer(totalTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateCountdownText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
//                TODO: hiển thị màn hình kết thúc trò chơi
                tvCountdown.setText("Hết giờ");
            }
        }.start();
    }

    private void updateCountdownText(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000) % 60;
        tvCountdown.setText(String.format("%02d", seconds));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        // Giải phóng MediaPlayer khi activity bị destroy
        if (soundManager != null) {
            soundManager.stopSound();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Dừng nhạc nếu người dùng ẩn ra khỏi trò chơi
        if (soundManager != null) {
            soundManager.pauseSound();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Tiếp tục phát nhạc nếu người dùng ẩn ra khỏi trò chơi
        if (soundManager != null) {
            soundManager.resumeSound();
        }
    }

    //Hàm load câu hỏi
    private void loadQuestion(Question question) {
        tvQuestion.setText(question.getQuestion());
        btnAnswerA.setText("A: " + question.getAnswer1());
        btnAnswerB.setText("B: " + question.getAnswer2());
        btnAnswerC.setText("C: " + question.getAnswer3());
        btnAnswerD.setText("D: " + question.getAnswer4());
    }
}