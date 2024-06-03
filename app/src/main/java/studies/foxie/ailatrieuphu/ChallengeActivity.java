package studies.foxie.ailatrieuphu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Random;

public class ChallengeActivity extends AppCompatActivity {
    private DB database;
    private String userId, roomId;
    private Room room;
    private ImageView btnFindMatch, btnCreateRoom, btnFindRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ẩn thanh tiêu đề
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Đặt cờ cho cửa sổ
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideNavigationBar();
        setContentView(R.layout.activity_challenge);

        //Khởi tạo đối tượng database và khởi tạo database
        database = new DB(ChallengeActivity.this.getApplicationContext());

        //Ánh xạ các thành phần trong layout
        btnFindMatch = findViewById(R.id.btn_challenge_find_match);
        btnCreateRoom = findViewById(R.id.btn_challenge_create_room);
        btnFindRoom = findViewById(R.id.btn_challenge_find_room);

        //Lấy userId của người đang đăng nhập
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Bắt sự kiện khi bấm vào nút 'Tìm đối thủ'
        btnFindMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findOrCreateRoom(ChallengeActivity.this, userId);
            }
        });
        //Bắt sự kiện khi bấm vào nút 'Tạo phòng'
        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRoom(userId);
            }
        });
        //Bắt sự kiện khi bấm vào nút 'Tìm phòng'
        btnFindRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEnterRoomDialog();
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
    private void showEnterRoomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_enter_room, null);
        builder.setView(dialogView);

        EditText roomNumberEditText = dialogView.findViewById(R.id.roomNumberEditText);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton);

        AlertDialog dialog = builder.create();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomNumber = roomNumberEditText.getText().toString();

                if (!TextUtils.isEmpty(roomNumber) && roomNumber.length() == 6) {
                    // Handle room number input (e.g., search for room)
                    dialog.dismiss();
                    findRoomById(ChallengeActivity.this, userId, roomNumber);
                } else {
                    Toast.makeText(ChallengeActivity.this, "Please enter a valid 6-digit room number.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void findOrCreateRoom(Context context, String userId) {
        if (!isNetworkAvailable(context)) {
            // Hiển thị thông báo lỗi cho người dùng
            Toast.makeText(context, "Không có kết nối mạng. Vui lòng kiểm tra kết nối và thử lại.", Toast.LENGTH_LONG).show();
            return;
        }

        DatabaseReference roomsRef = FirebaseDatabase.getInstance().getReference().child("rooms");

        roomsRef.orderByChild("status").equalTo("waiting").limitToFirst(1).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        // Có phòng chờ, người chơi sẽ tham gia phòng này
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            roomId = snapshot.getKey();
                            joinRoom(roomId, userId);
                            break;
                        }
                    }
                    else {
                        // Không có phòng chờ, tạo phòng mới
                        createRoom(userId);
                    }
                }
            }
        });
    }
    public void joinRoom(String roomId, String userId) {
        DatabaseReference roomRef = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomId);
        roomRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if(dataSnapshot.exists()) {
                        room = dataSnapshot.getValue(Room.class);
                        if (room.player1Id == null) {
                            roomRef.child("player1Id").setValue(userId);
                        }
                        else if (room.player2Id == null) {
                            roomRef.child("player2Id").setValue(userId);
                        }
                    }
                }
            }
        });
        //Chuyển đến màn hình phòng chơi
        Intent intent = new Intent(ChallengeActivity.this, RoomActivity.class);
        intent.putExtra("roomId", roomId);
        intent.putExtra("userId", userId);
        startActivity(intent);
//        listenToRoomUpdates(roomId);
//        showWaiting("Đang vào phòng " + roomId);
    }

    public void createRoom(String userId) {
        final DatabaseReference roomsRef = FirebaseDatabase.getInstance().getReference().child("rooms");
        generateUniqueRoomId(roomsRef, new RoomIdCallback() {
            @Override
            public void onRoomIdGenerated(String id) {
                roomId = id;
                Room room = new Room(userId, userId, null, false, false, 1, 0, 0, database.getRandomQuestionList(), userId, "waiting");
                roomsRef.child(roomId).setValue(room);
                //Chuyển đến màn hình phòng chơi
                Intent intent = new Intent(ChallengeActivity.this, RoomActivity.class);
                intent.putExtra("roomId", roomId);
                intent.putExtra("userId", userId);
                startActivity(intent);
//                listenToRoomUpdates(roomId);
//                showWaiting("Đang tìm đối thủ...\nID phòng: " + roomId);
            }
        });
    }
    public void findRoomById(Context context, String userId, String id) {
        if (!isNetworkAvailable(context)) {
            // Hiển thị thông báo lỗi cho người dùng
            Toast.makeText(context, "Không có kết nối mạng. Vui lòng kiểm tra kết nối và thử lại.", Toast.LENGTH_LONG).show();
            return;
        }
        DatabaseReference roomsRef = FirebaseDatabase.getInstance().getReference().child("rooms").child(id);
        roomsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if(dataSnapshot.exists()) {
                        room = dataSnapshot.getValue(Room.class);
                        if(room.status.equals("waiting")) {
                            roomId = dataSnapshot.getKey();
                            joinRoom(roomId, userId);
                        }
                        else {
                            if(!id.equals(roomId)) {
                                Toast.makeText(context, "Không tìm thấy phòng chơi" + roomId + " = " + id, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else {
                        if(!id.equals(roomId)) {
                            Toast.makeText(context, "Không tìm thấy phòng chơi" + roomId + " = " + id, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
    private void generateUniqueRoomId(final DatabaseReference roomsRef, final RoomIdCallback callback) {
        final String roomId = KeyGenerator.generateKey();
        roomsRef.child(roomId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    //Nếu thông tin phòng đã tồn tại
                    if (dataSnapshot.exists()) {
                        // Key already exists, generate a new one
                        generateUniqueRoomId(roomsRef, callback);
                    } else {
                        // Key is unique
                        callback.onRoomIdGenerated(roomId);
                    }
                }
            }
        });
    }
}
class KeyGenerator {
    private static final String CHAR_POOL = "0123456789";
    private static final int KEY_LENGTH = 6;
    private static Random random = new Random();

    public static String generateKey() {
        StringBuilder key = new StringBuilder(KEY_LENGTH);
        for (int i = 0; i < KEY_LENGTH; i++) {
            key.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
        }
        return key.toString();
    }
}
interface RoomIdCallback {
    void onRoomIdGenerated(String roomId);
}
