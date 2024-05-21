package studies.foxie.ailatrieuphu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlayerInfoAdapter adapter;
    private FirebaseDatabase firebaseDatabase;
    private List<PlayerInfo> playerInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        playerInfoList = new ArrayList<>();
        adapter = new PlayerInfoAdapter(playerInfoList);
        recyclerView.setAdapter(adapter);

        firebaseDatabase = FirebaseDatabase.getInstance();

        getLeaderboardData();
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
                        Toast.makeText(LeaderboardActivity.this, "" + playerInfo.getId(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.w("TAG", "Null PlayerInfo received from database");
                    }
                }
                // Sắp xếp ngược lại vì `limitToLast` lấy từ thấp đến cao
                Collections.reverse(playerInfoList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "loadLeaderboard:onCancelled", databaseError.toException());
                Toast.makeText(LeaderboardActivity.this, "Failed to load leaderboard data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}