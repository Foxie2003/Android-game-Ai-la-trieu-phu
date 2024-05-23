package studies.foxie.ailatrieuphu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_leaderboard);

        ViewPager viewPager = findViewById(R.id.vp_leaderboard_viewpager);
        TabLayout tabLayout = findViewById(R.id.tl_leaderboard_tablayout);

        adapter = new CategoryPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentLeaderboard("money"), "BXH tiền");
        adapter.addFragment(new FragmentLeaderboard("diamond"), "BXH kim cương");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setupCustomTabLayout();
    }
    //Hàm ẩn thanh công cụ navigation bar
    private void hideNavigationBar() {
        // Ẩn thanh công cụ (navigation bar)
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }
    private void setupCustomTabLayout() {
        TabLayout tabLayout = findViewById(R.id.tl_leaderboard_tablayout);

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
                    }
                }
            }
        }
    }
}