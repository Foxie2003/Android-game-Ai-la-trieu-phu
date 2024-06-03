package studies.foxie.ailatrieuphu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoomActivity extends AppCompatActivity {
    private DB database;
    private String userId, roomId;
    private Room room;
    private ImageView ivP1Avatar, ivP2Avatar, ivReady, ivCancelReady, ivStart, ivP1Kick, ivP2Kick, ivExit;
    private TextView tvRoomId, tvP1Name, tvP2Name, tvP1Ready, tvP2Ready;
    private ValueEventListener valueEventListener;
    private DatabaseReference roomRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ẩn thanh tiêu đề
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Đặt cờ cho cửa sổ
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideNavigationBar();
        setContentView(R.layout.activity_room);

        // Ánh xạ các thành phần trong layout
        ivP1Avatar = findViewById(R.id.iv_room_p1_avatar);
        ivP2Avatar = findViewById(R.id.iv_room_p2_avatar);
        tvRoomId = findViewById(R.id.tv_room_id);
        tvP1Name = findViewById(R.id.iv_room_p1_name);
        tvP2Name = findViewById(R.id.iv_room_p2_name);
        tvP1Ready = findViewById(R.id.iv_room_p1_ready);
        tvP2Ready = findViewById(R.id.iv_room_p2_ready);
        ivReady = findViewById(R.id.iv_room_ready);
        ivCancelReady = findViewById(R.id.iv_room_cancel_ready);
        ivStart = findViewById(R.id.iv_room_start);
        ivP1Kick = findViewById(R.id.iv_room_p1_kick);
        ivP2Kick = findViewById(R.id.iv_room_p2_kick);
        ivExit = findViewById(R.id.iv_room_exit);

        //Khởi tạo đối tượng database
        database = new DB(RoomActivity.this.getApplicationContext());

        // Nhận thông tin phòng chơi từ Intent
        Intent intent = getIntent();
        roomId = intent.getStringExtra("roomId");
        userId = intent.getStringExtra("userId");

        //Bắt sự kiện khi bấm nút sẵn sàng
        ivReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerReady(userId, roomId, true);
            }
        });
        //Bắt sự kiện khi bấm nút hủy sẵn sàng
        ivCancelReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerReady(userId, roomId, false);
            }
        });
        //Bắt sự kiện khi bấm nút kick người chơi
        ivP1Kick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kickPlayer(1, roomId);
            }
        });
        ivP2Kick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kickPlayer(2, roomId);
            }
        });
        //Bắt sự kiện khi bấm nút thoát phòng
        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitRoom(userId, roomId);
            }
        });
        //Bắt sự kiện khi bấm nút bắt đầu
        ivStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(userId, roomId);
            }
        });

        listenToRoomUpdates(roomId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        roomRef.removeEventListener(valueEventListener);
    }

    //Hàm ẩn thanh công cụ navigation bar
    private void hideNavigationBar() {
        // Ẩn thanh công cụ (navigation bar)
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }
    public void listenToRoomUpdates(String roomId) {
        Toast.makeText(this, "Data change", Toast.LENGTH_SHORT).show();
        roomRef = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomId);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                room = dataSnapshot.getValue(Room.class);
                if (room != null) {
                    //Hiển thị id phòng
                    tvRoomId.setText("Mã phòng: " + roomId);
                    //Hiển thị thông tin người chơi
                    showPlayer1Info(RoomActivity.this, room.player1Id);
                    showPlayer2Info(RoomActivity.this, room.player2Id);
                    //Hiển thị trạng thái sẵn sàng của người chơi
                    tvP1Ready.setText(room.player1Ready ? "Sẵn sàng" : "Chưa sẵn sàng");
                    tvP2Ready.setText(room.player2Ready ? "Sẵn sàng" : "Chưa sẵn sàng");
                    //Hiển thị button sẵn sàng và hủy sẵn sàng
                    if (userId.equals(room.player1Id)) {
                        ivReady.setVisibility(room.player1Ready ? View.GONE : View.VISIBLE);
                        ivCancelReady.setVisibility(room.player1Ready ? View.VISIBLE : View.GONE);
                    }
                    else if (userId.equals(room.player2Id)) {
                        ivReady.setVisibility(room.player2Ready ? View.GONE : View.VISIBLE);
                        ivCancelReady.setVisibility(room.player2Ready ? View.VISIBLE : View.GONE);
                    }
                    //Nếu userId không có trong phòng thì hiển thị thông báo đã bị kick
                    else {
                        Toast.makeText(RoomActivity.this, "Bạn đã bị kick khỏi phòng", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    //Hiển thị nút kick và nút bắt đầu
                    if (userId.equals(room.roomOwnerId)) {
                        if(userId.equals(room.player1Id)) {
                            ivP2Kick.setVisibility(room.player2Ready || room.player2Id == null ? View.GONE : View.VISIBLE);
                            ivP1Kick.setVisibility(View.GONE);
                        }
                        else if (userId.equals(room.player2Id)) {
                            ivP1Kick.setVisibility(room.player1Ready || room.player1Id == null ? View.GONE : View.VISIBLE);
                            ivP2Kick.setVisibility(View.GONE);
                        }
                        ivStart.setVisibility(room.player1Ready && room.player2Ready ? View.VISIBLE : View.GONE);
                    }
                    else {
                        ivP1Kick.setVisibility(View.GONE);
                        ivP2Kick.setVisibility(View.GONE);
                        ivStart.setVisibility(View.GONE);
                    }
                    if (room.status.equals("ongoing")) {
                        roomRef.removeEventListener(valueEventListener);
                        Intent intent = new Intent(RoomActivity.this, ChallengeGameActivity.class);
                        intent.putExtra("userId", userId);
                        intent.putExtra("roomId", roomId);
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    Toast.makeText(RoomActivity.this, "Room is NULL", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Toast.makeText(RoomActivity.this, "Lỗi khi lấy dữ liệu trên firebase", Toast.LENGTH_SHORT).show();
            }
        };
        roomRef.addValueEventListener(valueEventListener);
    }
    public void showPlayer1Info(Context context, String userId) {
        //Nếu chưa có người chơi thì ẩn avatar và tên
        if(userId == null) {
            ivP1Avatar.setVisibility(View.GONE);
            tvP1Name.setVisibility(View.GONE);
            tvP1Ready.setVisibility(View.GONE);
            return;
        }
        else {
            ivP1Avatar.setVisibility(View.VISIBLE);
            tvP1Name.setVisibility(View.VISIBLE);
            tvP1Ready.setVisibility(View.VISIBLE);
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.getReference().child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        //Nếu thông tin người dùng đã tồn tại
                        if (dataSnapshot.exists()) {
                            // Lấy thông tin người chơi từ Firebase
                            PlayerInfo playerInfo = dataSnapshot.getValue(PlayerInfo.class);
                            if (playerInfo != null) {
                                // Cập nhật thông tin người chơi vào SQLite
                                playerInfo.setPlayerName(playerInfo.getPlayerName());
                                playerInfo.setAvatarId(playerInfo.getAvatarId());
                                playerInfo.setFrameId(playerInfo.getFrameId());
                                playerInfo.setMoney(playerInfo.getMoney());
                                playerInfo.setDiamond(playerInfo.getDiamond());
                                playerInfo.setHighestQuestionNumber(playerInfo.getHighestQuestionNumber());
                                playerInfo.setAnsweredQuestion(playerInfo.getAnsweredQuestion());
                                playerInfo.setCorrectAnsweredQuestion(playerInfo.getCorrectAnsweredQuestion());
                                playerInfo.setBoughtItems(playerInfo.getBoughtItems());

                                tvP1Name.setText(playerInfo.getPlayerName());

                                ArrayList<ShopItem> avatars = database.getItemsByCategory(2);
                                ArrayList<ShopItem> frames = database.getItemsByCategory(3);
                                avatars.forEach(item -> {
                                    if(item.getId() == playerInfo.getAvatarId()) {
                                        // Lấy id của hình ảnh từ tên hình ảnh trong drawable
                                        int resourceId = context.getResources().getIdentifier(item.getImage(), "drawable", context.getPackageName());
                                        // Kiểm tra xem id của hình ảnh có hợp lệ không
                                        if (resourceId != 0) {
                                            // Nếu id hợp lệ, đặt hình ảnh vào ImageView
                                            ivP1Avatar.setImageResource(resourceId);
                                        } else {
                                            // Nếu không tìm thấy hình ảnh, xử lý theo nhu cầu của bạn, ví dụ đặt một hình ảnh mặc định
                                            //                holder.ivImage.setImageResource(R.drawable.default_image);
                                        }
                                    }
                                });
                                frames.forEach(item -> {
                                    if(item.getId() == playerInfo.getFrameId()) {
                                        // Lấy id của hình ảnh từ tên hình ảnh trong drawable
                                        int resourceId = context.getResources().getIdentifier(item.getImage(), "drawable", context.getPackageName());
                                        // Kiểm tra xem id của hình ảnh có hợp lệ không
                                        if (resourceId != 0) {
                                            // Nếu id hợp lệ, đặt hình ảnh vào ImageView
                                            ivP1Avatar.setBackgroundResource(resourceId);
                                        } else {
                                            // Nếu không tìm thấy hình ảnh, xử lý theo nhu cầu của bạn, ví dụ đặt một hình ảnh mặc định
                                            //                holder.ivImage.setImageResource(R.drawable.default_image);
                                        }
                                    }
                                });
                            }
                        }
                        //Nếu thông tin người dùng chưa tồn tại
                        else {
                            Toast.makeText(RoomActivity.this, "Thông tin người chơi không tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
    public void showPlayer2Info(Context context, String userId) {
        //Nếu chưa có người chơi thì ẩn avatar và tên
        if(userId == null) {
            ivP2Avatar.setVisibility(View.GONE);
            tvP2Name.setVisibility(View.GONE);
            tvP2Ready.setVisibility(View.GONE);
        }
        else {
            ivP2Avatar.setVisibility(View.VISIBLE);
            tvP2Name.setVisibility(View.VISIBLE);
            tvP2Ready.setVisibility(View.VISIBLE);
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.getReference().child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        //Nếu thông tin người dùng đã tồn tại
                        if (dataSnapshot.exists()) {
                            // Lấy thông tin người chơi từ Firebase
                            PlayerInfo playerInfo = dataSnapshot.getValue(PlayerInfo.class);
                            if (playerInfo != null) {
                                // Cập nhật thông tin người chơi vào SQLite
                                playerInfo.setPlayerName(playerInfo.getPlayerName());
                                playerInfo.setAvatarId(playerInfo.getAvatarId());
                                playerInfo.setFrameId(playerInfo.getFrameId());
                                playerInfo.setMoney(playerInfo.getMoney());
                                playerInfo.setDiamond(playerInfo.getDiamond());
                                playerInfo.setHighestQuestionNumber(playerInfo.getHighestQuestionNumber());
                                playerInfo.setAnsweredQuestion(playerInfo.getAnsweredQuestion());
                                playerInfo.setCorrectAnsweredQuestion(playerInfo.getCorrectAnsweredQuestion());
                                playerInfo.setBoughtItems(playerInfo.getBoughtItems());

                                tvP2Name.setText(playerInfo.getPlayerName());

                                ArrayList<ShopItem> avatars = database.getItemsByCategory(2);
                                ArrayList<ShopItem> frames = database.getItemsByCategory(3);
                                avatars.forEach(item -> {
                                    if(item.getId() == playerInfo.getAvatarId()) {
                                        // Lấy id của hình ảnh từ tên hình ảnh trong drawable
                                        int resourceId = context.getResources().getIdentifier(item.getImage(), "drawable", context.getPackageName());
                                        // Kiểm tra xem id của hình ảnh có hợp lệ không
                                        if (resourceId != 0) {
                                            // Nếu id hợp lệ, đặt hình ảnh vào ImageView
                                            ivP2Avatar.setImageResource(resourceId);
                                        } else {
                                            // Nếu không tìm thấy hình ảnh, xử lý theo nhu cầu của bạn, ví dụ đặt một hình ảnh mặc định
                                            //                holder.ivImage.setImageResource(R.drawable.default_image);
                                        }
                                    }
                                });
                                frames.forEach(item -> {
                                    if(item.getId() == playerInfo.getFrameId()) {
                                        // Lấy id của hình ảnh từ tên hình ảnh trong drawable
                                        int resourceId = context.getResources().getIdentifier(item.getImage(), "drawable", context.getPackageName());
                                        // Kiểm tra xem id của hình ảnh có hợp lệ không
                                        if (resourceId != 0) {
                                            // Nếu id hợp lệ, đặt hình ảnh vào ImageView
                                            ivP2Avatar.setBackgroundResource(resourceId);
                                        } else {
                                            // Nếu không tìm thấy hình ảnh, xử lý theo nhu cầu của bạn, ví dụ đặt một hình ảnh mặc định
                                            //                holder.ivImage.setImageResource(R.drawable.default_image);
                                        }
                                    }
                                });
                            }
                        }
                        //Nếu thông tin người dùng chưa tồn tại
                        else {
                            Toast.makeText(RoomActivity.this, "Thông tin người chơi không tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
    private void playerReady(String userId, String roomId, boolean ready) {
        DatabaseReference roomRef = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomId);
        roomRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        Room room = dataSnapshot.getValue(Room.class);
                        if (room != null) {
                            if (userId.equals(room.player1Id)) {
                                roomRef.child("player1Ready").setValue(ready);
                            }
                            else if (userId.equals(room.player2Id)) {
                                roomRef.child("player2Ready").setValue(ready);
                            }
                        }
                    } else {
                        // Xử lý trường hợp dataSnapshot không tồn tại
                        Log.d("FirebaseData", "Room does not exist");
                    }
                } else {
                    // Xử lý lỗi khi task không thành công
                    Log.d("FirebaseData", "Error getting data", task.getException());
                }
            }
        });
    }
    private void kickPlayer(int player, String roomId) {
        DatabaseReference roomRef = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomId);
        roomRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        Room room = dataSnapshot.getValue(Room.class);
                        if (room != null) {
                            if (player == 1) {
                                roomRef.child("player1Id").removeValue();
                            }
                            else if (player == 2) {
                                roomRef.child("player2Id").removeValue();
                            }
                        }
                    } else {
                        // Xử lý trường hợp dataSnapshot không tồn tại
                        Log.d("FirebaseData", "Room does not exist");
                    }
                } else {
                    // Xử lý lỗi khi task không thành công
                    Log.d("FirebaseData", "Error getting data", task.getException());
                }
            }
        });
    }
    private void exitRoom(String userId, String roomId) {
        DatabaseReference roomRef = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomId);
        roomRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        Room room = dataSnapshot.getValue(Room.class);
                        if (room != null) {
                            if(room.player1Id == null || room.player2Id == null) {
                                roomRef.removeValue();
                            }
                            else if (userId.equals(room.player1Id)) {
                                if(userId.equals(room.roomOwnerId)) {
                                    roomRef.child("roomOwnerId").setValue(room.player2Id);
                                }
                                roomRef.child("player1Id").removeValue();
                            }
                            else if (userId.equals(room.player2Id)) {
                                if(userId.equals(room.roomOwnerId)) {
                                    roomRef.child("roomOwnerId").setValue(room.player1Id);
                                }
                                roomRef.child("player2Id").removeValue();
                            }
                            roomRef.removeEventListener(valueEventListener);
                            finish();
                        }
                    } else {
                        // Xử lý trường hợp dataSnapshot không tồn tại
                        Log.d("FirebaseData", "Room does not exist");
                    }
                } else {
                    // Xử lý lỗi khi task không thành công
                    Log.d("FirebaseData", "Error getting data", task.getException());
                }
            }
        });
    }
    private void startGame(String userId, String roomId) {
        DatabaseReference roomRef = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomId);
        roomRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        Room room = dataSnapshot.getValue(Room.class);
                        if (room != null) {
                            roomRef.child("status").setValue("ongoing");
                            roomRef.removeEventListener(valueEventListener);
                            Intent intent = new Intent(RoomActivity.this, ChallengeGameActivity.class);
                            intent.putExtra("userId", userId);
                            intent.putExtra("roomId", roomId);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        // Xử lý trường hợp dataSnapshot không tồn tại
                        Log.d("FirebaseData", "Room does not exist");
                    }
                } else {
                    // Xử lý lỗi khi task không thành công
                    Log.d("FirebaseData", "Error getting data", task.getException());
                }
            }
        });
    }
}