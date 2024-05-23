package studies.foxie.ailatrieuphu;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentLeaderboard extends Fragment {
    public String infoName;
    private DB database;
    private ImageView ivAvatarTop1, ivAvatarTop2, ivAvatarTop3, ivImageTop1, ivImageTop2, ivImageTop3;
    private TextView tvNameTop1, tvNameTop2, tvNameTop3, tvInfoTop1, tvInfoTop2, tvInfoTop3;
    private RecyclerView recyclerView;
    private PlayerInfoAdapter adapter;
    private FirebaseDatabase firebaseDatabase;
    private List<PlayerInfo> playerInfoList;
    public FragmentLeaderboard(String infoName) {
        this.infoName = infoName;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        recyclerView = view.findViewById(R.id.rv_leaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ivAvatarTop1 = view.findViewById(R.id.iv_leaderboard_avatar_top_1);
        ivAvatarTop2 = view.findViewById(R.id.iv_leaderboard_avatar_top_2);
        ivAvatarTop3 = view.findViewById(R.id.iv_leaderboard_avatar_top_3);
        ivImageTop1 = view.findViewById(R.id.iv_leaderboard_image_top_1);
        ivImageTop2 = view.findViewById(R.id.iv_leaderboard_image_top_2);
        ivImageTop3 = view.findViewById(R.id.iv_leaderboard_image_top_3);
        tvNameTop1 = view.findViewById(R.id.tv_leaderboard_name_top_1);
        tvNameTop2 = view.findViewById(R.id.tv_leaderboard_name_top_2);
        tvNameTop3 = view.findViewById(R.id.tv_leaderboard_name_top_3);
        tvInfoTop1 = view.findViewById(R.id.tv_leaderboard_info_top_1);
        tvInfoTop2 = view.findViewById(R.id.tv_leaderboard_info_top_2);
        tvInfoTop3 = view.findViewById(R.id.tv_leaderboard_info_top_3);

        playerInfoList = new ArrayList<>();
        adapter = new PlayerInfoAdapter(getContext(), playerInfoList, infoName);
        recyclerView.setAdapter(adapter);

        database = new DB(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();

        getLeaderboardData();
        return view;
    }
    private void getLeaderboardData() {
        DatabaseReference usersRef = firebaseDatabase.getReference().child("users");
        usersRef.orderByChild("money").limitToLast(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                playerInfoList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PlayerInfo playerInfo = snapshot.getValue(PlayerInfo.class);
                    if (playerInfo != null) {
                        playerInfoList.add(playerInfo);
                    } else {
                        Log.w("TAG", "Null PlayerInfo received from database");
                    }
                }
                // Sắp xếp ngược lại vì `limitToLast` lấy từ thấp đến cao
                Collections.reverse(playerInfoList);
                if(playerInfoList.size() >= 3) {
                    tvNameTop1.setText(playerInfoList.get(0).getPlayerName());
                    tvNameTop2.setText(playerInfoList.get(1).getPlayerName());
                    tvNameTop3.setText(playerInfoList.get(2).getPlayerName());
                    // Lấy id của hình ảnh từ tên hình ảnh trong drawable
                    int resourceId = getContext().getResources().getIdentifier(database.getItemById(playerInfoList.get(0).getAvatarId()).getImage(), "drawable", getContext().getPackageName());
                    ivAvatarTop1.setImageResource(resourceId);
                    int resourceId2 = getContext().getResources().getIdentifier(database.getItemById(playerInfoList.get(1).getAvatarId()).getImage(), "drawable", getContext().getPackageName());
                    ivAvatarTop2.setImageResource(resourceId2);
                    int resourceId3 = getContext().getResources().getIdentifier(database.getItemById(playerInfoList.get(2).getAvatarId()).getImage(), "drawable", getContext().getPackageName());
                    ivAvatarTop3.setImageResource(resourceId3);
                    switch (infoName) {
                        case ("money"):
                            tvInfoTop1.setText(DB.formatNumber(playerInfoList.get(0).getMoney()));
                            tvInfoTop2.setText(DB.formatNumber(playerInfoList.get(1).getMoney()));
                            tvInfoTop3.setText(DB.formatNumber(playerInfoList.get(2).getMoney()));
                            tvInfoTop1.setTextColor(Color.parseColor("#1ACF14"));
                            tvInfoTop2.setTextColor(Color.parseColor("#1ACF14"));
                            tvInfoTop3.setTextColor(Color.parseColor("#1ACF14"));
                            tvInfoTop1.setShadowLayer(16, 0, 0, Color.parseColor("#1ACF14"));
                            tvInfoTop2.setShadowLayer(16, 0, 0, Color.parseColor("#1ACF14"));
                            tvInfoTop3.setShadowLayer(16, 0, 0, Color.parseColor("#1ACF14"));
                            ivImageTop1.setImageResource(R.drawable.money);
                            ivImageTop2.setImageResource(R.drawable.money);
                            ivImageTop3.setImageResource(R.drawable.money);
                            break;
                        case ("diamond"):
                            tvInfoTop1.setText(DB.formatNumber(playerInfoList.get(0).getDiamond()));
                            tvInfoTop2.setText(DB.formatNumber(playerInfoList.get(1).getDiamond()));
                            tvInfoTop3.setText(DB.formatNumber(playerInfoList.get(2).getDiamond()));
                            tvInfoTop1.setTextColor(Color.parseColor("#38B6FF"));
                            tvInfoTop2.setTextColor(Color.parseColor("#38B6FF"));
                            tvInfoTop3.setTextColor(Color.parseColor("#38B6FF"));
                            tvInfoTop1.setShadowLayer(16, 0, 0, Color.parseColor("#38B6FF"));
                            tvInfoTop2.setShadowLayer(16, 0, 0, Color.parseColor("#38B6FF"));
                            tvInfoTop3.setShadowLayer(16, 0, 0, Color.parseColor("#38B6FF"));
                            ivImageTop1.setImageResource(R.drawable.diamond);
                            ivImageTop2.setImageResource(R.drawable.diamond);
                            ivImageTop3.setImageResource(R.drawable.diamond);
                            break;
                    }
                    playerInfoList.remove(0);
                    playerInfoList.remove(0);
                    playerInfoList.remove(0);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "loadLeaderboard:onCancelled", databaseError.toException());
                Toast.makeText(getContext(), "Failed to load leaderboard data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}