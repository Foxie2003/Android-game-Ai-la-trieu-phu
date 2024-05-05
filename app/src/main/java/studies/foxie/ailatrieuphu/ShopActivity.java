package studies.foxie.ailatrieuphu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ShopActivity extends AppCompatActivity {
    private DB database;
    private TextView tvMoney, tvDiamond;
    RecyclerView recyclerView;
    SharedPreferences prefs;
    private ArrayList<ShopItem> items;
    private ShopItemAdapter shopItemAdapter;
    private ImageView ivWatchAds;
    private ImageButton btnBack;
    private RewardedAd rewardedAd;
    private final String TAG = "ShopActivity";
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
        recyclerView = findViewById(R.id.rv_shop_items);
        ivWatchAds = findViewById(R.id.iv_shop_watch_ads);
        btnBack = findViewById(R.id.ibtn_shop_back);
        //Khởi tạo đối tượng database và khởi tạo database
        database = new DB(ShopActivity.this.getApplicationContext());
        database.createDatabase();
        //Hiện thị số tiền đang có của người chơi
        showPlayerMoney();
        //Hiện thị số kim cương đang có của người chơi
        showPlayerDiamond();
        //Khởi tạo ArrayList chứa dữ liệu của những item trong cửa hàng
        items = database.getItemsByCategory(1);
        Log.e(TAG, "onCreate: " + items.size());

//        ArrayList<ShopItem> shopItems = new ArrayList<>();
//        shopItems.add(new ShopItem(1, 1, "Xe máy 1",1000000, "item_motorbike_1", false));
//        shopItems.add(new ShopItem(2, R.drawable.item_motorbike_2, 100000));
//        shopItems.add(new ShopItem(3, R.drawable.item_motorbike_3, 10000));
//        shopItems.add(new ShopItem(4, R.drawable.item_motorbike_4, 100000000));
//        shopItems.add(new ShopItem(5, R.drawable.item_motorbike_5, 100000000));
//        shopItems.add(new ShopItem(6, R.drawable.item_motorbike_6, 100000000));
//        shopItems.add(new ShopItem(7, R.drawable.item_house_1, 100000000));
//        shopItems.add(new ShopItem(8, R.drawable.item_house_2, 100000000));
//        shopItems.add(new ShopItem(9, R.drawable.item_house_3, 100000000));
//        shopItems.add(new ShopItem(10, R.drawable.item_ring_1, 100000000));
//        shopItems.add(new ShopItem(11, R.drawable.item_ring_2, 100000000));
//        shopItems.add(new ShopItem(12, R.drawable.item_ring_3, 100000000));
//        shopItems.add(new ShopItem(13, R.drawable.item_car_1, 100000000));
//        shopItems.add(new ShopItem(14, R.drawable.item_car_2, 100000000));
//        shopItems.add(new ShopItem(15, R.drawable.item_car_3, 100000000));
//        shopItems.add(new ShopItem(16, R.drawable.item_car_4, 100000000));
//        shopItems.add(new ShopItem(17, R.drawable.item_car_5, 100000000));
//        shopItems.add(new ShopItem(18, R.drawable.item_car_6, 100000000));

        //Hiển thị các item trong cửa hàng lên recyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        shopItemAdapter = new ShopItemAdapter(this, items, new ShopItemAdapter.OnItemClickListener() {
            @Override
            public void onBuyItemClick(ShopItem item) {
                if(item.isBought()) {
                    Toast.makeText(ShopActivity.this, "Vật phẩm này đã được mua", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(database.minusMoney(item.getPrice())) {
                        showPlayerMoney();
                        item.setBought(true);

                        // Thông báo cho Adapter biết rằng một mục đã thay đổi
                        shopItemAdapter.notifyItemChanged(items.indexOf(item));
                    }
                    else {
                        Toast.makeText(ShopActivity.this, "Bạn không đủ tiền", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        recyclerView.setAdapter(shopItemAdapter);

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
    private void showPlayerMoney() {
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
}