package studies.foxie.ailatrieuphu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {
    private DB database;
    private TextView tvMoney, tvDiamond;
    RecyclerView recyclerView;
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
        // Ánh xạ các thành phần trong layout
        tvMoney = findViewById(R.id.tv_shop_money);
        tvDiamond = findViewById(R.id.tv_shop_diamond);
        recyclerView = findViewById(R.id.rv_shop_items);
        //Khởi tạo đối tượng database và khởi tạo database
        database = new DB(ShopActivity.this.getApplicationContext());
        database.createDatabase();
        //Hiện thị số tiền đang có của người chơi
        showPlayerMoney();
        //Hiện thị số kim cương đang có của người chơi
        showPlayerDiamond();
        //Khởi tạo ArrayList chứa dữ liệu của những item trong cửa hàng
        ArrayList<ShopItem> shopItems = new ArrayList<>();
        shopItems.add(new ShopItem(R.drawable.item_motorbike_1, 1000000));
        shopItems.add(new ShopItem(R.drawable.item_motorbike_2, 100000));
        shopItems.add(new ShopItem(R.drawable.item_motorbike_3, 10000));
        shopItems.add(new ShopItem(R.drawable.item_motorbike_4, 100000000));
        shopItems.add(new ShopItem(R.drawable.item_motorbike_5, 100000000));
        shopItems.add(new ShopItem(R.drawable.item_motorbike_6, 100000000));
        shopItems.add(new ShopItem(R.drawable.item_house_1, 100000000));
        shopItems.add(new ShopItem(R.drawable.item_house_2, 100000000));
        shopItems.add(new ShopItem(R.drawable.item_house_3, 100000000));
        shopItems.add(new ShopItem(R.drawable.item_ring_1, 100000000));
        shopItems.add(new ShopItem(R.drawable.item_ring_2, 100000000));
        shopItems.add(new ShopItem(R.drawable.item_ring_3, 100000000));
        shopItems.add(new ShopItem(R.drawable.item_car_1, 100000000));
        shopItems.add(new ShopItem(R.drawable.item_car_2, 100000000));
        shopItems.add(new ShopItem(R.drawable.item_car_3, 100000000));
        shopItems.add(new ShopItem(R.drawable.item_car_4, 100000000));
        shopItems.add(new ShopItem(R.drawable.item_car_5, 100000000));
        shopItems.add(new ShopItem(R.drawable.item_car_6, 100000000));
        //Hiển thị các item trong cửa hàng lên recyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        ShopItemAdapter shopItemAdapter = new ShopItemAdapter(this, shopItems, new ShopItemAdapter.OnItemClickListener() {
            @Override
            public void onBuyItemClick(ShopItem item) {
                database.minusMoney(item.getPrice());
                showPlayerMoney();
            }
        });
        recyclerView.setAdapter(shopItemAdapter);
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
}