package studies.foxie.ailatrieuphu;

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
import android.util.Log;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class PlayActivity extends AppCompatActivity {
    private DB database;
    private SoundManager soundManager;
    private boolean playerIsReady = false;
    private Question question;
    private ArrayList<Integer> questionNumberSounds, answerSounds, answerCorrectSounds, answerWrongSounds;
    private ArrayList<AppCompatButton> answerButtons;
    private ArrayList<Integer> prizeMoneys = new ArrayList<>(Arrays.asList(200, 400, 600,
            1000, 2000, 3000, 6000, 10000, 14000, 22000, 30000, 40000, 60000, 85000, 150000));
    private GradientTextView tvCountdown;
    private ImageView imgCountdown;
    private CountDownTimer countDownTimer;
    private final long totalTimeInMillis = 35000; // 35 seconds
    private DrawerLayout layoutPlay;
    private LinearLayout layoutPlayMain, layoutAnswers, layoutLifelines;
    private RelativeLayout layoutCountDown, layoutQuestion;
    private TextView tvPrize, tvQuestion, tvPlayQuestionNumber1, tvPlayQuestionNumber2, tvPlayQuestionNumber3,
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
        tvPrize = findViewById(R.id.tv_play_prize);
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

        //Thêm button vào array list để tiện sử dụng
        answerButtons = new ArrayList<>();
        answerButtons.add(btnAnswerA);
        answerButtons.add(btnAnswerB);
        answerButtons.add(btnAnswerC);
        answerButtons.add(btnAnswerD);

        //Khởi tạo đối tượng SoundManager để phát âm thanh
        soundManager = new SoundManager(PlayActivity.this);

        //Đặt ảnh gif làm bg cho ImageView
        Glide.with(this).asGif().load(R.drawable.countdown).into(imgCountdown);

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
                Toast.makeText(PlayActivity.this, "click", Toast.LENGTH_SHORT).show();
                btnReady.clearAnimation();
                btnReady.setVisibility(View.GONE);
                soundManager.playSound(R.raw.mc_ready);
                soundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        soundManager.removeOnCompletionListener();
                        //Hiển thị các layout, button khi bắt đầu chơi
                        layoutCountDown.setVisibility(View.VISIBLE);
                        layoutQuestion.setVisibility(View.VISIBLE);
                        layoutAnswers.setVisibility(View.VISIBLE);
                        btn5050.setVisibility(View.VISIBLE);
                        btnAskAudience.setVisibility(View.VISIBLE);
                        btnCall.setVisibility(View.VISIBLE);
                        //Hiển thị backgound của layout trợ giúp
                        layoutLifelines.setBackgroundResource(R.drawable.bg_style_3);
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
                });
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

        //Test các trợ giúp
        btn5050.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 2;) {
                    // Khởi tạo một đối tượng của lớp Random
                    Random random = new Random();
                    // Đặt giá trị tối thiểu và tối đa
                    int max = 4;
                    int min = 1;
                    // Tạo số nguyên ngẫu nhiên trong khoảng từ min đến max
                    int randomNumber = random.nextInt(max - min + 1) + min;
                    if(randomNumber != question.getCorrectAnswer()) {
                        AppCompatButton answerButton = answerButtons.get(randomNumber - 1);
                        if (answerButton.getText() != null) {
                            answerButton.setText(null);
                            answerButton.setEnabled(false);
                            i++;
                        }
                    }
                }
            }
        });
        btnAskAudience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayActivity.this, "Tổ tư vấn: " + question.getCorrectAnswer(), Toast.LENGTH_SHORT).show();
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayActivity.this, "Người thân: " + question.getCorrectAnswer(), Toast.LENGTH_SHORT).show();
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
        //Phát âm thanh đọc luật chơi
        soundManager.playSound(R.raw.mc_rule);
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
                    tvPlayQuestionNumber5.setBackgroundResource(R.drawable.question_selected);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tvPlayQuestionNumber5.setBackgroundResource(R.drawable.question_normal);
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
                    tvPlayQuestionNumber10.setBackgroundResource(R.drawable.question_selected);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tvPlayQuestionNumber10.setBackgroundResource(R.drawable.question_normal);
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
                    tvPlayQuestionNumber15.setBackgroundResource(R.drawable.question_selected);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tvPlayQuestionNumber15.setBackgroundResource(R.drawable.question_normal);
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
                    btn5050.setVisibility(View.VISIBLE);
                }
            }
        }, 7800);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!playerIsReady) {
                    // Hiển thị trợ giúp sau khi đủ thời gian trễ
                    btnAskAudience.setVisibility(View.VISIBLE);
                }
            }
        }, 9000);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!playerIsReady) {
                    // Hiển thị trợ giúp sau khi đủ thời gian trễ
                    btnCall.setVisibility(View.VISIBLE);
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

    //Hàm lấy ra id của file âm thanh từ tên file
    private int getRawId(String fileName) {
        return getResources().getIdentifier(fileName, "raw", getPackageName());
    }

    //Hàm load câu hỏi
    private void loadQuestion(Question question) {
        //Xóa hết nội dung câu hỏi, câu trả lời cũ
        tvQuestion.setText(null);
        btnAnswerA.setText(null);
        btnAnswerB.setText(null);
        btnAnswerC.setText(null);
        btnAnswerD.setText(null);
        //Bắt đầu đếm giờ
        startCountdown();
        //Đọc số câu hỏi hiện tại
        soundManager.playSound(questionNumberSounds.get(question.getQuestionNumber() - 1));
        tvPrize.setText(new DecimalFormat("###,###,###").format(prizeMoneys.get(question.getQuestionNumber() - 1)) + ",000");
        soundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                soundManager.removeOnCompletionListener();
                //Đọc nội dung câu hỏi
                soundManager.playSound(getRawId(question.getAudioFileName()));
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
                    }
                }, 3000);
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
        answerButtons.get(answerIndex - 1).setBackgroundResource(R.drawable.question_selected);
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
                        answerButtons.get(question.getCorrectAnswer() - 1).setBackgroundResource(R.drawable.question_correct);
                        //Nếu đáp án đúng
                        if(answerIndex == question.getCorrectAnswer()) {
                            //Phát âm thanh chúc mừng
                            soundManager.playSound(answerCorrectSounds.get(randomNumber));
                            soundManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    soundManager.removeOnCompletionListener();
                                    //Kích hoạt các button trở lại
                                    enableAllButtons();
                                    //Bỏ highlight đáp án được chọn
                                    answerButtons.get(answerIndex - 1).setBackgroundResource(R.drawable.button_selector);
                                    //Load câu hỏi tiếp theo
                                    question = database.getQuestionByQN(question.getQuestionNumber() + 1);
                                    loadQuestion(question);
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
        btnReady.setEnabled(false);
        btnQuestionNumber.setEnabled(false);
        btnAnswerA.setEnabled(false);
        btnAnswerB.setEnabled(false);
        btnAnswerC.setEnabled(false);
        btnAnswerD.setEnabled(false);
        btn5050.setEnabled(false);
        btnAskAudience.setEnabled(false);
        btnCall.setEnabled(false);
        btnChangeQuestion.setEnabled(false);
    }
    //Hàm kích hoạt các nút trên màn hình
    private void enableAllButtons() {
        btnReady.setEnabled(true);
        btnQuestionNumber.setEnabled(true);
        btnAnswerA.setEnabled(true);
        btnAnswerB.setEnabled(true);
        btnAnswerC.setEnabled(true);
        btnAnswerD.setEnabled(true);
        btn5050.setEnabled(true);
        btnAskAudience.setEnabled(true);
        btnCall.setEnabled(true);
        btnChangeQuestion.setEnabled(true);
    }
    //Hàm hiển thị dialog kết thúc trò chơi
    private void showGameOverDialog() {
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
        tvPrizeResult.setText(tvPrize.getText() + " VND");
        //Hiển thị số câu và kỉ lục
        tvQuestionNumber.setText("Câu số: " + question.getQuestionNumber());
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
                answerButtons.get(question.getCorrectAnswer() - 1).setBackgroundResource(R.drawable.button_selector);
                //Bỏ highlight đáp án được chọn
                answerButtons.forEach(button -> {
                    button.setBackgroundResource(R.drawable.button_selector);
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
}