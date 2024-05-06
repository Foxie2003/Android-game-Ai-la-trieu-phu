package studies.foxie.ailatrieuphu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ShopActivity extends AppCompatActivity {
    private static DB database;
    private static TextView tvMoney;
    private TextView tvDiamond;

    SharedPreferences prefs;

    private ImageView ivWatchAds;
    private ImageButton btnBack;
    private RewardedAd rewardedAd;
    private final String TAG = "ShopActivity";
    private CategoryPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ẩn thanh tiêu đề
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Đặt cờ cho cửa sổ
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideNavigationBar();
        setContentView(R.layout.activity_shop);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        // Ánh xạ các thành phần trong layout
        tvMoney = findViewById(R.id.tv_shop_money);
        tvDiamond = findViewById(R.id.tv_shop_diamond);
        ivWatchAds = findViewById(R.id.iv_shop_watch_ads);
        btnBack = findViewById(R.id.ibtn_shop_back);
        //Khởi tạo đối tượng database và khởi tạo database
        database = new DB(ShopActivity.this.getApplicationContext());
        database.createDatabase();
        //Hiện thị số tiền đang có của người chơi
        showPlayerMoney();
        //Hiện thị số kim cương đang có của người chơi
        showPlayerDiamond();
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        adapter = new CategoryPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentTaiSan(), "Tài sản");
        adapter.addFragment(new FragmentAvatar(), "Avatar");
        adapter.addFragment(new FragmentKhung(), "Khung");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setupCustomTabLayout();


        loadRewardedAd();
        ivWatchAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rewardedAd != null) {
                    Activity activityContext = ShopActivity.this;
                    rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            database.addDiamond(20);
                            showPlayerDiamond();
                            Toast.makeText(activityContext, "+20, Cảm ơn bạn đã xem QC", Toast.LENGTH_SHORT).show();
                        }
                    });
                    loadRewardedAd();
                } else {
                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
    //Hàm hiển thị số tiền đang có của người chơi
    public static void showPlayerMoney() {
        tvMoney.setText(database.getStringMoney());
    }
    //Hàm hiển thị số kim cương đang có của người chơi
    private void showPlayerDiamond() {
        tvDiamond.setText(String.valueOf(database.getDiamond()));
    }
    private void loadRewardedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
            adRequest, new RewardedAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error.
                    Log.d(TAG, loadAdError.toString());
                    rewardedAd = null;
                }

                @Override
                public void onAdLoaded(@NonNull RewardedAd ad) {
                    rewardedAd = ad;
                    Log.d(TAG, "Ad was loaded.");
                }
            });
    }
    private void setupCustomTabLayout() {
        TabLayout tabLayout = findViewById(R.id.tabLayout);

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
}