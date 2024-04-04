package studies.foxie.ailatrieuphu;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class PlayActivity extends AppCompatActivity {
    private DB database;
    private SoundManager soundManager;
    private boolean playerIsReady = false;
    private Question question;
    private ArrayList<Integer> questionNumberSounds, answerSounds, answerCorrectSounds,
            answerWrongSounds, helpAnswerSounds;
    private ArrayList<AppCompatButton> answerButtons;
    private ArrayList<TextView> questionNumberTextViews;
    private ArrayList<Integer> prizeMoneys = new ArrayList<>(Arrays.asList(200000, 400000, 600000,
            1000000, 2000000, 3000000, 6000000, 10000000, 14000000, 22000000, 30000000, 40000000, 60000000, 85000000, 150000000));
    private int prizeResult = 0;
    private GradientTextView tvCountdown;
    private ImageView ivCountdown, iv5050, ivAskAudience, ivCall, ivAskThreeAudience;
    private CountDownTimer countDownTimer;
    private final long totalTimeInMillis = 30000; // 30 seconds
    private DrawerLayout layoutPlay;
    private LinearLayout layoutPlayMain, layoutAnswers, layoutLifelines, layoutAds;
    private RelativeLayout layoutCountDown;
    private TextView tvQuestion, tvPlayQuestionNumber1, tvPlayQuestionNumber2, tvPlayQuestionNumber3,
            tvPlayQuestionNumber4, tvPlayQuestionNumber5, tvPlayQuestionNumber6, tvPlayQuestionNumber7,
            tvPlayQuestionNumber8, tvPlayQuestionNumber9, tvPlayQuestionNumber10, tvPlayQuestionNumber11,
            tvPlayQuestionNumber12, tvPlayQuestionNumber13, tvPlayQuestionNumber14, tvPlayQuestionNumber15;
    private AppCompatButton btnReady, btnQuestionNumber, btnAnswerA, btnAnswerB, btnAnswerC, btnAnswerD;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ẩn thanh tiêu đề
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Đặt cờ cho cửa sổ
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideNavigationBar();
        setContentView(R.layout.activity_play);
        //Ánh xạ các thành phần trong layout
        layoutPlay = findViewById(R.id.layout_play);
        layoutPlayMain = findViewById(R.id.layout_play_main);
        layoutCountDown = findViewById(R.id.layout_countdown);
        layoutAnswers = findViewById(R.id.layout_answers);
        layoutLifelines = findViewById(R.id.layout_lifelines);
        layoutAds = findViewById(R.id.layout_ads);
        tvCountdown = findViewById(R.id.tv_countdown);
        ivCountdown = findViewById(R.id.iv_play_countdown);
//        tvPrize = findViewById(R.id.tv_play_prize);
        tvQuestion = findViewById(R.id.tv_play_question);
        btnReady = findViewById(R.id.btn_play_ready);
        btnQuestionNumber = findViewById(R.id.btn_play_question_number);
        btnAnswerA = findViewById(R.id.btn_play_answer_a);
        btnAnswerB = findViewById(R.id.btn_play_answer_b);
        btnAnswerC = findViewById(R.id.btn_play_answer_c);
        btnAnswerD = findViewById(R.id.btn_play_answer_d);
        iv5050 = findViewById(R.id.iv_play_5050);
        ivAskAudience = findViewById(R.id.iv_play_ask_audience);
        ivCall = findViewById(R.id.iv_play_call);
        ivAskThreeAudience = findViewById(R.id.iv_play_ask_three_audience);
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

        //Thêm id âm thanh vào các array list để tiện sử dụng
        questionNumberSounds = new ArrayList<>();
        questionNumberSounds.add(R.raw.mc_start_cau1);
        questionNumberSounds.add(R.raw.mc_start_cau2);
        questionNumberSounds.add(R.raw.mc_start_cau3);
        questionNumberSounds.add(R.raw.mc_start_cau4);
        questionNumberSounds.add(R.raw.mc_start_cau5);
        questionNumberSounds.add(R.raw.mc_start_cau6);
        questionNumberSounds.add(R.raw.mc_start_cau7);
        questionNumberSounds.add(R.raw.mc_start_cau8);
        questionNumberSounds.add(R.raw.mc_start_cau9);
        questionNumberSounds.add(R.raw.mc_start_cau10);
        questionNumberSounds.add(R.raw.mc_start_cau11);
        questionNumberSounds.add(R.raw.mc_start_cau12);
        questionNumberSounds.add(R.raw.mc_start_cau13);
        questionNumberSounds.add(R.raw.mc_start_cau14);
        questionNumberSounds.add(R.raw.mc_start_cau15);

        answerSounds = new ArrayList<>();
        answerSounds.add(R.raw.ans_1_1);
        answerSounds.add(R.raw.ans_1_2);
        answerSounds.add(R.raw.ans_1_3);
        answerSounds.add(R.raw.ans_2_1);
        answerSounds.add(R.raw.ans_2_2);
        answerSounds.add(R.raw.ans_2_3);
        answerSounds.add(R.raw.ans_3_1);
        answerSounds.add(R.raw.ans_3_2);
        answerSounds.add(R.raw.ans_3_3);
        answerSounds.add(R.raw.ans_4_1);
        answerSounds.add(R.raw.ans_4_2);
        answerSounds.add(R.raw.ans_4_3);

        answerCorrectSounds = new ArrayList<>();
        answerCorrectSounds.add(R.raw.true_1_1);
        answerCorrectSounds.add(R.raw.true_1_2);
        answerCorrectSounds.add(R.raw.true_1_3);
        answerCorrectSounds.add(R.raw.true_2_1);
        answerCorrectSounds.add(R.raw.true_2_2);
        answerCorrectSounds.add(R.raw.true_2_3);
        answerCorrectSounds.add(R.raw.true_3_1);
        answerCorrectSounds.add(R.raw.true_3_2);
        answerCorrectSounds.add(R.raw.true_3_3);
        answerCorrectSounds.add(R.raw.true_4_1);
        answerCorrectSounds.add(R.raw.true_4_2);
        answerCorrectSounds.add(R.raw.true_4_3);

        answerWrongSounds = new ArrayList<>();
        answerWrongSounds.add(R.raw.lose_a);
        answerWrongSounds.add(R.raw.lose_b);
        answerWrongSounds.add(R.raw.lose_c);
        answerWrongSounds.add(R.raw.lose_d);

        helpAnswerSounds = new ArrayList<>();
        helpAnswerSounds.add(R.raw.answer_help_a);
        helpAnswerSounds.add(R.raw.answer_help_b);
        helpAnswerSounds.add(R.raw.answer_help_c);
        helpAnswerSounds.add(R.raw.answer_help_d);

        //Thêm button vào array list để tiện sử dụng
        answerButtons = new ArrayList<>();
        answerButtons.add(btnAnswerA);
        answerButtons.add(btnAnswerB);
        answerButtons.add(btnAnswerC);
        answerButtons.add(btnAnswerD);

        //Thêm textview vào array list để tiện sử dụng
        questionNumberTextViews = new ArrayList<>();
        questionNumberTextViews.add(tvPlayQuestionNumber1);
        questionNumberTextViews.add(tvPlayQuestionNumber2);
        questionNumberTextViews.add(tvPlayQuestionNumber3);
        questionNumberTextViews.add(tvPlayQuestionNumber4);
        questionNumberTextViews.add(tvPlayQuestionNumber5);
        questionNumberTextViews.add(tvPlayQuestionNumber6);
        questionNumberTextViews.add(tvPlayQuestionNumber7);
        questionNumberTextViews.add(tvPlayQuestionNumber8);
        questionNumberTextViews.add(tvPlayQuestionNumber9);
        questionNumberTextViews.add(tvPlayQuestionNumber10);
        questionNumberTextViews.add(tvPlayQuestionNumber11);
        questionNumberTextViews.add(tvPlayQuestionNumber12);
        questionNumberTextViews.add(tvPlayQuestionNumber13);
        questionNumberTextViews.add(tvPlayQuestionNumber14);
        questionNumberTextViews.add(tvPlayQuestionNumber15);

        //Khởi tạo đối tượng SoundManager để phát âm thanh
        soundManager = new SoundManager(PlayActivity.this);

        //Vô hiệu hóa các button không cần thiết
        disableAllButtons();

        //Chạy hàm phổ biến luật chơi
        rulesTelling();

        //Bắt sự kiện khi ấn vào nút số câu hỏi thì hiện ra layout hiện thị danh sách câu hỏi
        btnQuestionNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuestionNumber();
            }
        });

        //Bắt sự kiện khi ấn vào nút sẵn sàng thì hiện ra layout chính
        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerIsReady = true;
                //Ẩn nút sẵn sàng
                btnReady.clearAnimation();
                btnReady.setVisibility(View.GONE);
                soundManager.playSound(R.raw.mc_ready);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Hiển thị các layout, button khi bắt đầu chơi
                        layoutCountDown.setVisibility(View.VISIBLE);
                        tvQuestion.setVisibility(View.VISIBLE);
                        layoutAnswers.setVisibility(View.VISIBLE);
                        iv5050.setVisibility(View.VISIBLE);
                        ivAskAudience.setVisibility(View.VISIBLE);
                        ivCall.setVisibility(View.VISIBLE);
                        //Áp dụng animation để hiển thị layout chơi
                        Animation fadeInAnimation = AnimationUtils.loadAnimation(PlayActivity.this, R.anim.fade_in);
                        layoutPlayMain.startAnimation(fadeInAnimation);
                        //Bắt đầu đọc câu hỏi
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Load câu hỏi đầu tiên sau khi đủ thời gian trễ
                                question = new Question(database.getQuestionByQN(1));
                                loadQuestion(question);
                            }
                        }, 1000);
                    }
                }, 4200);
            }
        });

        //Bắt sự kiện khi bấm vào nút đáp án thì kiểm tra xem đáp án đúng hay không
        btnAnswerA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Xử lý kiểm tra đáp án đúng
                checkAnswer(1);
            }
        });
        btnAnswerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Xử lý kiểm tra đáp án đúng
                checkAnswer(2);
            }
        });
        btnAnswerC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Xử lý kiểm tra đáp án đúng
                checkAnswer(3);
            }
        });
        btnAnswerD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Xử lý kiểm tra đáp án đúng
                checkAnswer(4);
            }
        });

        //Bắt sự kiện khi bấm dùng trợ giúp 50 50
        iv5050.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lifeLine5050();
                //Đặt lại sự kiện khi click vào trợ giúp
                iv5050.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBuyLifeLineDialog(R.id.iv_play_5050);
                    }
                });
            }
        });
        //Bắt sự kiện khi bấm dùng trợ giúp hỏi ý kiến khản giả trong trường quay
        ivAskAudience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lifeLineAskAudience();
                //Đặt lại sự kiện khi click vào trợ giúp hỏi ý kiến khản giả
                ivAskAudience.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBuyLifeLineDialog(R.id.iv_play_ask_audience);
                    }
                });
            }
        });
        //Bắt sự kiện khi bấm dùng trợ giúp gọi điện thoại cho người thân
        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lifeLineCall();
                //Đặt lại sự kiện khi click vào trợ giúp
                ivCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBuyLifeLineDialog(R.id.iv_play_call);
                    }
                });
            }
        });
        //Bắt sự kiện khi bấm dùng trợ giúp tổ tư vấn tại chỗ
        ivAskThreeAudience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lifeLineAskThreeAudience();
                //Đặt lại sự kiện khi click vào trợ giúp
                ivAskThreeAudience.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBuyLifeLineDialog(R.id.iv_play_ask_three_audience);
                    }
                });
            }
        });
        //Khởi tạo đối tượng mobile ads để tạo quảng cáo
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        //Khởi tạo banner quảng cáo góc dưới màn hình
        loadBannerAds();

        //Load quảng cáo trước để hiển thị sau khi hiện dialog gameover
        loadInterstitialAds();
    }

    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = layoutAds.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    private void loadBannerAds() {

        // Create a new ad view.
        AdView adView = new AdView(this);
        adView.setAdSize(getAdSize());
        adView.setAdUnitId("ca-app-pub-3940256099942544/9214589741");

        // Replace ad container with new ad view.
        layoutAds.removeAllViews();
        layoutAds.addView(adView);

        // Start loading the ad in the background.
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void loadInterstitialAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
            new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd;
                    Log.i(TAG, "onAdLoaded");
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    Log.d(TAG, loadAdError.toString());
                    mInterstitialAd = null;
                }
            });
    }

    private void showInterstitialAds() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(PlayActivity.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    private void openQuestionNumber() {
        layoutPlay.openDrawer(GravityCompat.END); // Mở layout ở phía bên phải
    }

    private void closeQuestionNumber() {
        layoutPlay.closeDrawer(GravityCompat.END); // Đóng layout ở phía bên phải
    }

    //Hàm phổ biến luật chơi khi bắt đầu ván chơi mới
    private void rulesTelling() {
        //Phát âm thanh đọc luật chơi
        soundManager.playSound(R.raw.mc_rule);
        //Ẩn các layout, button khi chưa bắt đầu chơi
        layoutCountDown.setVisibility(View.INVISIBLE);
        tvQuestion.setVisibility(View.INVISIBLE);
        layoutAnswers.setVisibility(View.INVISIBLE);
        iv5050.setVisibility(View.GONE);
        ivAskAudience.setVisibility(View.GONE);
        ivCall.setVisibility(View.GONE);
        ivAskThreeAudience.setVisibility(View.GONE);
        //Ẩn background của layout trợ giúp
        layoutLifelines.setBackground(null);
        // Tạo một Handler để định thời gian trễ
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!playerIsReady) {
                    // Hiển thị layout sau khi đủ thời gian trễ
                    openQuestionNumber();
                }
            }
        }, 1000);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!playerIsReady) {
                    // Highlight những câu hỏi quan trọng
                    tvPlayQuestionNumber5.setBackgroundResource(R.drawable.button_answer_selected);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tvPlayQuestionNumber5.setBackgroundResource(R.drawable.question_dark);
                        }
                    }, 400);
                }
            }
        }, 4200);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!playerIsReady) {
                    // Highlight những câu hỏi quan trọng
                    tvPlayQuestionNumber10.setBackgroundResource(R.drawable.button_answer_selected);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tvPlayQuestionNumber10.setBackgroundResource(R.drawable.question_dark);
                        }
                    }, 400);
                }
            }
        }, 4600);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!playerIsReady) {
                    // Highlight những câu hỏi quan trọng
                    tvPlayQuestionNumber15.setBackgroundResource(R.drawable.button_answer_selected);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tvPlayQuestionNumber15.setBackgroundResource(R.drawable.question_dark);
                        }
                    }, 400);
                }
            }
        }, 5000);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!playerIsReady) {
                    // Ẩn layout sau khi đủ thời gian trễ
                    closeQuestionNumber();
                }
            }
        }, 5600);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!playerIsReady) {
                    // Hiển thị trợ giúp sau khi đủ thời gian trễ
                    iv5050.setVisibility(View.VISIBLE);
                }
            }
        }, 7800);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!playerIsReady) {
                    // Hiển thị trợ giúp sau khi đủ thời gian trễ
                    ivAskAudience.setVisibility(View.VISIBLE);
                }
            }
        }, 9000);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!playerIsReady) {
                    // Hiển thị trợ giúp sau khi đủ thời gian trễ
                    ivCall.setVisibility(View.VISIBLE);
                }
            }
        }, 10200);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!playerIsReady) {
                    // Chạy animation sau khi đủ thời gian trễ
                    Animation shakeAnimation = AnimationUtils.loadAnimation(PlayActivity.this, R.anim.shaking);
                    btnReady.startAnimation(shakeAnimation);
                }
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
                //Phát âm thanh hết giờ
                soundManager.playSound(R.raw.bg_timesup);
                soundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        soundManager.removeOnCompletionListener();
                        //Hiển thị màn hình kết thúc trò chơi
                        showGameOverDialog();
                    }
                });
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

    //Hàm lấy ra id của file âm thanh từ tên file
    private int getRawId(String fileName) {
        return getResources().getIdentifier(fileName, "raw", getPackageName());
    }
    //Hàm load câu hỏi
    private void loadQuestion(Question question) {
        hideNavigationBar();
        disableAllButtons();
        //Hiển thị thời gian khi đồng hồ chưa đếm
        tvCountdown.setText("30");
        //Hiện thị số câu hiện tại
        btnQuestionNumber.setText(question.getQuestionNumber() + "/15");
        //Xóa hết nội dung câu hỏi, câu trả lời cũ
        tvQuestion.setText(null);
        btnAnswerA.setText(null);
        btnAnswerB.setText(null);
        btnAnswerC.setText(null);
        btnAnswerD.setText(null);
        //Đọc số câu hỏi hiện tại
        soundManager.playSound(questionNumberSounds.get(question.getQuestionNumber() - 1));
//        tvPrize.setText(new DecimalFormat("###,###,###").format(prizeMoneys.get(question.getQuestionNumber() - 1)));
        soundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                soundManager.removeOnCompletionListener();
                tvQuestion.setText(question.getQuestion());
                // Tạo một Handler để định thời gian trễ
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hiển thị câu trả lời sau khi đủ thời gian trễ
                        btnAnswerA.setText("A: " + question.getAnswer1());
                    }
                }, 1500);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hiển thị câu trả lời sau khi đủ thời gian trễ
                        btnAnswerB.setText("B: " + question.getAnswer2());
                    }
                }, 2000);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hiển thị câu trả lời sau khi đủ thời gian trễ
                        btnAnswerC.setText("C: " + question.getAnswer3());
                    }
                }, 2500);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hiển thị câu trả lời sau khi đủ thời gian trễ
                        btnAnswerD.setText("D: " + question.getAnswer4());
                        //Cho phép trả lời sau khi hiện dủ 4 đáp án
                        enableAllButtons();
                    }
                }, 3000);
                //Đọc nội dung câu hỏi
                soundManager.playSound(getRawId(question.getAudioFileName()));
                soundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        soundManager.removeOnCompletionListener();
                        //Bắt đầu đếm giờ
                        startCountdown();
                    }
                });
            }
        });
    }
    //Hàm kiểm tra đáp án đúng
    private void checkAnswer(int answerIndex) {
        //Dừng đồng hồ đếm ngược
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
        //Ngăn không cho người dùng bấm nút
        disableAllButtons();
        //Highlight đáp án được chọn
        answerButtons.get(answerIndex - 1).setBackgroundResource(R.drawable.button_answer_selected);
        // Khởi tạo một đối tượng của lớp Random
        Random random = new Random();
        // Đặt giá trị tối thiểu và tối đa
        int max = (answerIndex * 3) - 1;
        int min = max - 2;
        // Tạo số nguyên ngẫu nhiên trong khoảng từ min đến max
        int randomNumber = random.nextInt(max - min + 1) + min;
        //Phát âm thanh chọn câu hỏi
        soundManager.playSound(R.raw.bg_select_ans);
        soundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                soundManager.removeOnCompletionListener();
                //Phát ngẫu nhiên 1 trong 3 âm thanh đáp án đã chọn
                soundManager.playSound(answerSounds.get(randomNumber));
                soundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        soundManager.removeOnCompletionListener();
                        //Highlight đáp án đúng
                        Animation flashingAnimation = AnimationUtils.loadAnimation(PlayActivity.this, R.anim.flashing);
                        answerButtons.get(question.getCorrectAnswer() - 1).startAnimation(flashingAnimation);
                        answerButtons.get(question.getCorrectAnswer() - 1).setBackgroundResource(R.drawable.button_answer_correct);
                        //Nếu đáp án đúng
                        if(answerIndex == question.getCorrectAnswer()) {
                            //Đánh dấu câu hỏi đã trả lời xong
                            questionNumberTextViews.get(question.getQuestionNumber() - 1).setBackgroundResource(R.drawable.button_answer_selected);
                            //Thay đổi kết quả tiền thưởng
                            prizeResult = prizeMoneys.get(question.getQuestionNumber() - 1);
                            //Phát âm thanh chúc mừng
                            soundManager.playSound(answerCorrectSounds.get(randomNumber));
                            soundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    soundManager.removeOnCompletionListener();
                                    //Nếu đến câu hỏi số 6 thì phát âm thanh chúc mừng và hiển thị trợ giúp tổ tư vấn tại chỗ
                                    if (question.getQuestionNumber() + 1 == 6) {
                                        //Phát âm thanh chúc mừng
                                        soundManager.playSound(R.raw.mc_congrats_stage1);
                                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                //Hiển thị trợ giúp tổ tư vấn tại chỗ
                                                ivAskThreeAudience.setVisibility(View.VISIBLE);
                                            }
                                        }, 6000);
                                        soundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                            @Override
                                            public void onCompletion(MediaPlayer mp) {
                                                soundManager.removeOnCompletionListener();
                                                //Kích hoạt các button trở lại
                                                enableAllButtons();
                                                //Bỏ highlight đáp án được chọn
                                                answerButtons.get(answerIndex - 1).setBackgroundResource(R.drawable.selector_answer);
                                                //Load câu hỏi tiếp theo
                                                question = database.getQuestionByQN(question.getQuestionNumber() + 1);
                                                loadQuestion(question);
                                            }
                                        });
                                    }
                                    //Nếu đến câu hỏi số 11 thì phát âm thanh chúc mừng
                                    else if (question.getQuestionNumber() + 1 == 11) {
                                        //Phát âm thanh chúc mừng
                                        soundManager.playSound(R.raw.mc_congrats_stage2);
                                        soundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                            @Override
                                            public void onCompletion(MediaPlayer mp) {
                                                soundManager.removeOnCompletionListener();
                                                //Kích hoạt các button trở lại
                                                enableAllButtons();
                                                //Bỏ highlight đáp án được chọn
                                                answerButtons.get(answerIndex - 1).setBackgroundResource(R.drawable.selector_answer);
                                                //Load câu hỏi tiếp theo
                                                question = database.getQuestionByQN(question.getQuestionNumber() + 1);
                                                loadQuestion(question);
                                            }
                                        });
                                    }
                                    //Nếu đã trả lời đúng câu hỏi số 15 thì hiện ra dialog win
                                    else if (question.getQuestionNumber() == 15) {
                                        showWinDialog();
                                    }
                                    else  {
                                        //Kích hoạt các button trở lại
                                        enableAllButtons();
                                        //Bỏ highlight đáp án được chọn
                                        answerButtons.get(answerIndex - 1).setBackgroundResource(R.drawable.selector_answer);
                                        //Load câu hỏi tiếp theo
                                        question = database.getQuestionByQN(question.getQuestionNumber() + 1);
                                        loadQuestion(question);
                                    }
                                }
                            });
                        }
                        //Nếu đáp án sai
                        else  {
                            //Phát âm thanh đáp án sai
                            soundManager.playSound(answerWrongSounds.get(question.getCorrectAnswer() - 1));
                            soundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    soundManager.removeOnCompletionListener();
                                    //Hiển thị dialog kết thúc trò chơi
                                    showGameOverDialog();
                                }
                            });
                        }
                    }
                });
            }
        });
    }
    //Hàm vô hiệu hóa các nút trên màn hình
    private void disableAllButtons() {
//        btnReady.setEnabled(false);
//        btnQuestionNumber.setEnabled(false);
        btnAnswerA.setEnabled(false);
        btnAnswerB.setEnabled(false);
        btnAnswerC.setEnabled(false);
        btnAnswerD.setEnabled(false);
        iv5050.setEnabled(false);
        ivAskAudience.setEnabled(false);
        ivCall.setEnabled(false);
        ivAskThreeAudience.setEnabled(false);
    }
    //Hàm kích hoạt các nút trên màn hình
    private void enableAllButtons() {
//        btnReady.setEnabled(true);
//        btnQuestionNumber.setEnabled(true);
        btnAnswerA.setEnabled(true);
        btnAnswerB.setEnabled(true);
        btnAnswerC.setEnabled(true);
        btnAnswerD.setEnabled(true);
        iv5050.setEnabled(true);
        ivAskAudience.setEnabled(true);
        ivCall.setEnabled(true);
        ivAskThreeAudience.setEnabled(true);
    }
    //Hàm xử lý trợ giúp 5050
    public void lifeLine5050() {
        //Thay đổi src của trợ giúp khi đã được dùng
        iv5050.setImageResource(R.drawable.button_5050_used);
        //Phát âm thanh chọn trợ giúp 50 50
        soundManager.playSound(R.raw.mc_select_50_50);
        soundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                soundManager.removeOnCompletionListener();
                //Phát âm thanh bỏ đi 2 phương án sai
                soundManager.playSound(R.raw.mc_50_50);
                // Tạo một Handler để định thời gian trễ
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 2;) {
                            // Khởi tạo một đối tượng của lớp Random
                            Random random = new Random();
                            // Đặt giá trị tối thiểu và tối đa
                            int max = 4;
                            int min = 1;
                            // Tạo số nguyên ngẫu nhiên trong khoảng từ min đến max
                            int randomNumber = random.nextInt(max - min + 1) + min;
                            //Nếu random ra đáp án sai
                            if(randomNumber != question.getCorrectAnswer()) {
                                AppCompatButton answerButton = answerButtons.get(randomNumber - 1);
                                //Nếu đáp án đó chưa được loại bỏ
                                if (answerButton.isEnabled()) {
                                    //Loại bỏ đáp án
                                    answerButton.setText(null);
                                    answerButton.setEnabled(false);
                                    i++;
                                }
                            }
                        }
                    }
                }, 2900);
            }
        });
    }
    //Hàm xử lý trợ giúp hỏi ý kiến khán giả
    public void lifeLineAskAudience() {
        //Dừng đồng hồ đếm ngược
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
        //Thay đổi src của trợ giúp khi đã được dùng
        ivAskAudience.setImageResource(R.drawable.button_ask_audience_used);
        //Phát âm thanh chọn trợ giúp
        soundManager.playSound(R.raw.mc_select_ask_audience);
        soundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                soundManager.removeOnCompletionListener();
                //Phát âm thanh nền của trợ giúp
                soundManager.playSound(R.raw.bg_ask_audience);
                //Random % đáp án sai
                ArrayList<Integer> percents = new ArrayList<>();
                int correctPercent = 100;
                // Khởi tạo một đối tượng của lớp Random
                Random random = new Random();
                for (int i = 0; i < 3; i++) {
                    // Tạo số nguyên ngẫu nhiên trong khoảng từ 0 đến 20
                    int randomNumber = random.nextInt(20 + 1);
                    percents.add(randomNumber);
                    correctPercent -= randomNumber;
                }
                percents.add(correctPercent);
                //Sắp xếp các % vào trong mảng theo từng đáp án
                if(question.getCorrectAnswer() != 4) {
                    int tmp = percents.get(question.getCorrectAnswer() - 1);
                    percents.set(question.getCorrectAnswer() - 1, percents.get(3));
                    percents.set(3, tmp);
                }
                //Hiển thị dialog hỏi ý kiến khán giả trong trường quay
                FullScreenDialog askAudienceDialog = new FullScreenDialog(PlayActivity.this, R.layout.dialog_ask_audience);
                askAudienceDialog.setCancelable(false);
                askAudienceDialog.show();
                //Khởi tạo mảng chứa các view để tiện sử dụng
                ArrayList<View> columns = new ArrayList<>();
                columns.add(askAudienceDialog.findViewById(R.id.view_ask_audience_column1));
                columns.add(askAudienceDialog.findViewById(R.id.view_ask_audience_column2));
                columns.add(askAudienceDialog.findViewById(R.id.view_ask_audience_column3));
                columns.add(askAudienceDialog.findViewById(R.id.view_ask_audience_column4));
                ArrayList<TextView> textviews = new ArrayList<>();
                textviews.add(askAudienceDialog.findViewById(R.id.tv_ask_audience_column1));
                textviews.add(askAudienceDialog.findViewById(R.id.tv_ask_audience_column2));
                textviews.add(askAudienceDialog.findViewById(R.id.tv_ask_audience_column3));
                textviews.add(askAudienceDialog.findViewById(R.id.tv_ask_audience_column4));
                for (int i = 0; i < 4; i++) {
                    // Lấy LayoutParams hiện tại của view
                    //Vì view ở trong linear layout nên phải dùng "LinearLayout.LayoutParams"
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) columns.get(i).getLayoutParams();
                    // Thay đổi chiều cao của LayoutParams
                    layoutParams.height = (percents.get(i) * 5); // Chiều cao mới * 5 cho cao đẹp hơn
                    // Cập nhật LayoutParams mới cho view
                    columns.get(i).setLayoutParams(layoutParams);
                    //Set phần trăm cho text view
                    textviews.get(i).setText(percents.get(i) + "%");
                    textviews.get(i).setVisibility(View.INVISIBLE);
                    //Tạo animation hiển thị cho các các cột
                    Animation animation = AnimationUtils.loadAnimation(PlayActivity.this, R.anim.growing);
                    //Sử dụng animation cho các các cột
                    columns.get(i).startAnimation(animation);
                }
                //Hiển thị các text view khi đủ thời gian
                textviews.forEach(textView -> {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textView.setVisibility(View.VISIBLE);
                        }
                    }, 4800);
                });
                //Khi bấm nút trở về thì đóng dialog
                AppCompatButton btnReturn = askAudienceDialog.findViewById(R.id.btn_ask_audience_return);
                btnReturn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Tiếp tục đếm ngược
                        countDownTimer.start();
                        //Thoát dialog
                        askAudienceDialog.dismiss();
                        hideNavigationBar();
                    }
                });
            }
        });
    }
    //Hàm xử lý trợ giúp gọi điện thoại cho người thân
    public void lifeLineCall() {
        //Dừng đồng hồ đếm ngược
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
        //Thay đổi src của trợ giúp khi đã được dùng
        ivCall.setImageResource(R.drawable.button_call_used);
        //Phát âm thanh chọn trợ giúp
        soundManager.playSound(R.raw.mc_call);
        soundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                soundManager.removeOnCompletionListener();
                //Phát âm thanh kết nối điện thoại
                soundManager.playSound(R.raw.mc_conect_call);
                soundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        soundManager.removeOnCompletionListener();
                        //Phát âm thanh trả lời của người thân
                        soundManager.playSound(helpAnswerSounds.get(question.getCorrectAnswer() - 1));
                        //Hiển thị dialog gọi điện thoại cho người thân
                        FullScreenDialog callDialog = new FullScreenDialog(PlayActivity.this, R.layout.dialog_call);
                        callDialog.setCancelable(false);
                        callDialog.show();
                        //Hiện thị đáp án của người thân
                        TextView tvAnswer = callDialog.findViewById(R.id.tv_call_answer);
                        tvAnswer.setText("Câu trả lời của tôi là: " + question.getCorrectAnswerString());
                        //Khi bấm nút trở về thì đóng dialog
                        AppCompatButton btnReturn = callDialog.findViewById(R.id.btn_call_return);
                        btnReturn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Tiếp tục đếm ngược
                                countDownTimer.start();
                                //Thoát dialog
                                callDialog.dismiss();
                                hideNavigationBar();
                            }
                        });
                    }
                });
            }
        });
    }
    //Hàm xử lý trợ giúp tổ tư vấn tại chỗ
    public void lifeLineAskThreeAudience() {
        //Dừng đồng hồ đếm ngược
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
        //Thay đổi src của trợ giúp khi đã được dùng
        ivAskThreeAudience.setImageResource(R.drawable.button_ask_three_audience_used);
        //Phát âm thanh chọn trợ giúp
        soundManager.playSound(R.raw.mc_select_ask_audience);
        soundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                soundManager.removeOnCompletionListener();
                //Phát âm thanh hỏi khán giả
                soundManager.playSound(R.raw.mc_ask_three_audience);
                soundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        soundManager.removeOnCompletionListener();
                        //Tạo một mảng chứa các đáp án random
                        ArrayList<Integer> randomAnswers = new ArrayList<>();
                        //Random đáp án với tỉ lệ chọn đáp án đúng là 60% + 1/4
                        Random random = new Random();
                        for (int i = 0; i < 3; i++) {
                            // Tạo số ngẫu nhiên từ 0 đến 1
                            double randomNumber = random.nextDouble();
                            // Nếu số ngẫu nhiên nhỏ hơn 0.6, trả ra đáp án đúng
                            if (randomNumber < 0.6) {
                                randomAnswers.add(question.getCorrectAnswer());
                            }
                            // Nếu không trả ra đáp án ngẫu nhiên
                            else {
                                randomAnswers.add(random.nextInt(4) + 1); //1, 2, 3, 4
                            }
                        }
                        //Hiển thị dialog tổ tư vấn tại chỗ
                        FullScreenDialog askThreeAudienceDialog = new FullScreenDialog(PlayActivity.this, R.layout.dialog_ask_three_audience);
                        askThreeAudienceDialog.setCancelable(false);
                        askThreeAudienceDialog.show();
                        //Hiện thị đáp án của tổ tư vấn
                        TextView tvAnswer1 = askThreeAudienceDialog.findViewById(R.id.tv_ask_three_audience_1);
                        TextView tvAnswer2 = askThreeAudienceDialog.findViewById(R.id.tv_ask_three_audience_2);
                        TextView tvAnswer3 = askThreeAudienceDialog.findViewById(R.id.tv_ask_three_audience_3);
                        //Lần lượt phát và hiển thị 3 đáp án được chọn
                        tvAnswer1.setText("Câu trả lời của tôi là: " + question.getCorrectAnswerString(randomAnswers.get(0)));
                        soundManager.playSound(helpAnswerSounds.get(randomAnswers.get(0) - 1));
                        soundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                soundManager.removeOnCompletionListener();
                                soundManager.playSound(helpAnswerSounds.get(randomAnswers.get(1) - 1));
                                tvAnswer2.setText("Câu trả lời của tôi là: " + question.getCorrectAnswerString(randomAnswers.get(1)));
                                soundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        soundManager.removeOnCompletionListener();
                                        soundManager.playSound(helpAnswerSounds.get(randomAnswers.get(2) - 1));
                                        tvAnswer3.setText("Câu trả lời của tôi là: " + question.getCorrectAnswerString(randomAnswers.get(2)));
                                    }
                                });
                            }
                        });
                        //Khi bấm nút trở về thì đóng dialog
                        AppCompatButton btnReturn = askThreeAudienceDialog.findViewById(R.id.btn_ask_three_audience_return);
                        btnReturn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Tiếp tục đếm ngược
                                countDownTimer.start();
                                //Thoát dialog
                                askThreeAudienceDialog.dismiss();
                                hideNavigationBar();
                            }
                        });
                    }
                });
            }
        });
    }
    //Hàm hiển thị dialog kết thúc trò chơi
    private void showGameOverDialog() {
        //Hiện quảng cáo gián đoạn
        showInterstitialAds();
        //Load quảng cáo để chuẩn bị cho lần hiện tiếp theo
        loadInterstitialAds();
        //Cộng tiền cho người chơi
        database.addMoney(prizeResult);
        //Phát âm thanh kết thúc trò chơi
        soundManager.playSound(R.raw.mc_game_over);
        //Hiển thị dialog kết thúc trò chơi
        FullScreenDialog gameoverDialog = new FullScreenDialog(PlayActivity.this, R.layout.dialog_game_over);
        gameoverDialog.setCancelable(false);
        gameoverDialog.show();
        TextView tvPrizeResult = gameoverDialog.findViewById(R.id.tv_gameover_prize);
        TextView tvQuestionNumber = gameoverDialog.findViewById(R.id.tv_gameover_question_number);
        TextView tvHighQN = gameoverDialog.findViewById(R.id.tv_gameover_high_question_number);
        AppCompatButton btnGoHome = gameoverDialog.findViewById(R.id.btn_gameover_home);
        AppCompatButton btnTryAgain = gameoverDialog.findViewById(R.id.btn_gameover_try_again);
        //Hiển thị số tiền thắng
        tvPrizeResult.setText(new DecimalFormat("###,###,###").format(prizeResult) + " VND");
        //Hiển thị số câu và kỉ lục
        tvQuestionNumber.setText("Câu số: " + (question.getQuestionNumber() - 1));
        tvHighQN.setText("Kỉ lục: " + 15);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayActivity.this.finish();
            }
        });
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bỏ highlight đáp án đúng
                answerButtons.get(question.getCorrectAnswer() - 1).setBackgroundResource(R.drawable.selector_answer);
                //Bỏ highlight đáp án được chọn
                answerButtons.forEach(button -> {
                    button.setBackgroundResource(R.drawable.selector_answer);
                });
                //Ẩn dialog kết thúc trò chơi
                gameoverDialog.dismiss();
                // Ẩn thanh công cụ (navigation bar)
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                decorView.setSystemUiVisibility(uiOptions);
                //Kích hoạt các button trở lại
                enableAllButtons();
                //Load lại câu hỏi
                question = database.getQuestionByQN(question.getQuestionNumber());
                loadQuestion(question);
            }
        });
    }
    //Hàm hiển thị dialog chiến thắng trò chơi
    private void showWinDialog() {
        //Hiện quảng cáo gián đoạn
        showInterstitialAds();
        //Load quảng cáo để chuẩn bị cho lần hiện tiếp theo
        loadInterstitialAds();
        //Cộng tiền cho người chơi
        database.addMoney(prizeResult);
        //Phát âm thanh chiến thắng trò chơi
        soundManager.playSound(R.raw.mc_win);
        //Hiển thị dialog chiến thắng trò chơi
        FullScreenDialog winDialog = new FullScreenDialog(PlayActivity.this, R.layout.dialog_win);
        winDialog.setCancelable(false);
        winDialog.show();
        TextView tvPrizeResult = winDialog.findViewById(R.id.tv_win_prize);
        TextView tvQuestionNumber = winDialog.findViewById(R.id.tv_win_question_number);
        TextView tvHighQN = winDialog.findViewById(R.id.tv_win_high_question_number);
        AppCompatButton btnGoHome = winDialog.findViewById(R.id.btn_win_home);
        AppCompatButton btnShare = winDialog.findViewById(R.id.btn_win_share);
        //Hiển thị số tiền thắng
        tvPrizeResult.setText(new DecimalFormat("###,###,###").format(prizeResult) + " VND");
        //Hiển thị số câu và kỉ lục
        tvQuestionNumber.setText("Câu số: " + question.getQuestionNumber());
        tvHighQN.setText("Kỉ lục: " + 15);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayActivity.this.finish();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Làm chia sẻ kết quả
            }
        });
    }
    //Hàm hiển thị dialog mua trợ giúp
    private void showBuyLifeLineDialog(int lifeLineId) {
        //Hiển thị dialog mua trợ giúp
        FullScreenDialog buyLifeLineDialog = new FullScreenDialog(PlayActivity.this, R.layout.dialog_buy_lifeline);
        buyLifeLineDialog.setCancelable(false);
        buyLifeLineDialog.show();
        TextView tvPrice = buyLifeLineDialog.findViewById(R.id.tv_buy_lifeline_price);
        AppCompatButton btnClose = buyLifeLineDialog.findViewById(R.id.btn_buy_lifeline_close);
        AppCompatButton btnBuy = buyLifeLineDialog.findViewById(R.id.btn_buy_lifeline_buy);
        //Xác định người dùng đang muốn mua trợ giúp nào
        int price = 0;
        if (lifeLineId == R.id.iv_play_5050) {
            price = 50;
            int finalPrice = price;
            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Trừ kim cương của người chơi
                    //Nếu trừ thành công thì cho người dùng tiếp tục sử dụng trợ giúp
                    if(database.minusDiamond(finalPrice)) {
                        //Thay đổi src của trợ giúp có thể được dùng
                        iv5050.setImageResource(R.drawable.button_5050);
                        //Đặt lại sự kiện onClick cho trợ giúp
                        iv5050.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                lifeLine5050();
                                //Đặt lại sự kiện khi click vào trợ giúp
                                iv5050.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(PlayActivity.this, "Bạn đã hết lượt sử dụng trợ giúp này", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        //Đóng dialog
                        buyLifeLineDialog.dismiss();
                    }
                    else {
                        Toast.makeText(PlayActivity.this, "KC không đủ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else if (lifeLineId == R.id.iv_play_ask_audience) {
            price = 100;
            int finalPrice = price;
            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Trừ kim cương của người chơi
                    //Nếu trừ thành công thì cho người dùng tiếp tục sử dụng trợ giúp
                    if(database.minusDiamond(finalPrice)) {
                        //Thay đổi src của trợ giúp có thể được dùng
                        ivAskAudience.setImageResource(R.drawable.button_ask_audience);
                        //Đặt lại sự kiện onClick cho trợ giúp
                        ivAskAudience.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                lifeLineAskAudience();
                                //Đặt lại sự kiện khi click vào trợ giúp
                                ivAskAudience.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(PlayActivity.this, "Bạn đã hết lượt sử dụng trợ giúp này", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        //Đóng dialog
                        buyLifeLineDialog.dismiss();
                    }
                    else {
                        Toast.makeText(PlayActivity.this, "KC không đủ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else if (lifeLineId == R.id.iv_play_call) {
            price = 100;
            int finalPrice = price;
            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Trừ kim cương của người chơi
                    //Nếu trừ thành công thì cho người dùng tiếp tục sử dụng trợ giúp
                    if(database.minusDiamond(finalPrice)) {
                        //Thay đổi src của trợ giúp có thể được dùng
                        ivCall.setImageResource(R.drawable.button_ask_audience);
                        //Đặt lại sự kiện onClick cho trợ giúp
                        ivCall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                lifeLineCall();
                                //Đặt lại sự kiện khi click vào trợ giúp
                                ivCall.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(PlayActivity.this, "Bạn đã hết lượt sử dụng trợ giúp này", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        //Đóng dialog
                        buyLifeLineDialog.dismiss();
                    }
                    else {
                        Toast.makeText(PlayActivity.this, "KC không đủ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else if (lifeLineId == R.id.iv_play_ask_three_audience) {
            price = 100;
            int finalPrice = price;
            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Trừ kim cương của người chơi
                    //Nếu trừ thành công thì cho người dùng tiếp tục sử dụng trợ giúp
                    if(database.minusDiamond(finalPrice)) {
                        //Thay đổi src của trợ giúp có thể được dùng
                        ivAskThreeAudience.setImageResource(R.drawable.button_ask_audience);
                        //Đặt lại sự kiện onClick cho trợ giúp
                        ivAskThreeAudience.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                lifeLineAskThreeAudience();
                                //Đặt lại sự kiện khi click vào trợ giúp
                                ivAskThreeAudience.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(PlayActivity.this, "Bạn đã hết lượt sử dụng trợ giúp này", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        //Đóng dialog
                        buyLifeLineDialog.dismiss();
                    }
                    else {
                        Toast.makeText(PlayActivity.this, "KC không đủ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        //Hiển thị giá của trợ giúp
        tvPrice.setText(price + "");
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyLifeLineDialog.dismiss();
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