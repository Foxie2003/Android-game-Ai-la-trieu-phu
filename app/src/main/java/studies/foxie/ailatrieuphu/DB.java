package studies.foxie.ailatrieuphu;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DB {
    DataBaseHelper db;
    public DB(Context applicationContext) {
        db = new DataBaseHelper(applicationContext, "ALTP.sql", null, 1);
    }
    public void createDatabase() {
        //Tạo bảng PlayerInfo nếu nó chưa tồn tại
        Cursor playerInfoCursor = db.GetData("SELECT name FROM sqlite_master WHERE type='table' AND name='PlayerInfo'");
        if (playerInfoCursor == null || playerInfoCursor.getCount() <= 0) {
            db.QueryData("CREATE TABLE IF NOT EXISTS PlayerInfo (Money Integer, Diamond Integer)");
            db.QueryData("INSERT INTO PlayerInfo VALUES (0, 0)");
        }
        //Tạo bảng Questions nếu nó chưa tồn tại
        Cursor QuestionsCursor = db.GetData("SELECT name FROM sqlite_master WHERE type='table' AND name='Questions'");
        if (QuestionsCursor == null || QuestionsCursor.getCount() <= 0) {
            //questionNumber: câu hỏi có thể xuất hiện ở câu số ...
            db.QueryData("CREATE TABLE IF NOT EXISTS Questions (id INTEGER PRIMARY KEY AUTOINCREMENT, questionNumber Integer, question NVARCHAR(200), answer1 NVARCHAR(50), answer2 NVARCHAR(50), answer3 NVARCHAR(50), answer4 NVARCHAR(50), correctAnswer Integer, audioFileName NVARCHAR(50))");
            db.QueryData("INSERT INTO Questions  VALUES (null, 1, 'Sông Nile chảy ra biển nào?', 'Biển Đỏ', 'Biển Địa Trung Hải', 'Biển Đen', 'Biển Ban Tích', 2, 'q1_1')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 2, 'Chú bé rắc rối là tác phẩm của nhà văn nào sau đây?', 'Nguyễn Việt Hà', 'Nguyễn Ngọc Tư', 'Nguyễn Thị Thu Huệ', 'Nguyễn Nhật Ánh', 4, 'q1_2')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 3, 'Cây cầu đẹp và lãng mạn nào nằm trên dòng sông Vltava ở thủ đô CH Séc?', 'Cầu Charles', 'Cầu Sick', 'Cầu Alexander III', 'Cầu Rianto', 1, 'q1_3')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 4, 'Hình nào sau đây có ba cạnh?', 'Tam giác', 'Tứ giác', 'Ngũ giác', 'Lục giác', 1, 'q1_4')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 5, 'Theo luật giao thông đường bộ loại xe nào sau đây được ưu tiên khi đang làm nhiệm vụ?', 'Xe lu', 'Xe ba gác', 'Xe cứu thương', 'Xe thồ', 3, 'q1_5')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 6, 'Đâu là một thành phố của nước Nga?', 'Vancouver', 'Amsterdam', 'Berlin', 'Sant Peterburg', 4, 'q1_6')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 7, 'Quả nào khi còn xanh thường để làm món nộm?', 'Hồng Xiêm', 'Cau', 'Bơ', 'Đu đủ', 4, 'q1_7')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 8, 'Nhân vật nào sau đây gắn với truyền thuyết về bánh chưng, bánh giày?', 'Trọng Thủy', 'Sơn Tinh', 'Lang Liêu', 'Trương Chi', 3, 'q1_8')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 9, 'Đèo Ô Quy Hồ nối liền tỉnh Lào Cai với tỉnh nào?', 'Lai Châu', 'Hà Giang', 'Yên Bái', 'Tuyên Quang', 1, 'q1_9')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 10, 'Nhà Thơ nào đã tự ví \"Lòng ta là những hàng thành quách cũ Từ ngàn năm bỗng vẳng tiếng loa xưa\"?', 'Vũ Đình Liên', 'Huy Cận', 'Tản Đà', 'Chế Lan Viên', 1, 'q1_10')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 11, 'Alexandre Dumas là ai?', 'Cầu thủ bóng chuyền', 'VĐV leo núi', 'Tay đua công thức 1', 'Nhà văn', 4, 'q2_1')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 12, 'Đâu là tên một loại đàn?', 'Bí', 'Mướp', 'Bầu', 'Dưa', 3, 'q2_2')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 13, 'Loài hoa nào thường nở vào buổi tối?', 'Hoa 10 giờ', 'Hoa quỳnh', 'Hoa Đồng Tiền', 'Hoa Hồng', 2, 'q2_3')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 14, 'Từ nào sau đây là động từ?', 'Nhất', 'Lục', 'Cửu', 'Tứ', 2, 'q2_4')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 15, 'Đâu là tên một ca khúc của nhạc sĩ Phó Đức Phương?', 'Chảy đi tóc ơi', 'Chảy đi nước ơi', 'Chảy đi sông ơi', 'Chảy đi gió ơi', 3, 'q2_5')");
//            db.QueryData("INSERT INTO Questions  VALUES (null, 1, '?', '', '', '', '', 4, 'q2_')");
        }
    }
    public int getMoney(){
        //Lấy bản ghi đầu tiên của cột Money trong bảng PlayerInfo
        Cursor data = db.GetData("SELECT Money FROM PlayerInfo LIMIT 1");
        int money = 0;
        if (data != null && data.moveToFirst()) {
            money = data.getInt(0);
        }
        return money;
    }
    public Question getQuestionByQN(int questionNumber) {
        //Lấy bản ghi ngẫu nhiên trong bảng Question với questionNumber
        Cursor data = db.GetData("SELECT * FROM Questions where questionNumber = " + questionNumber + " ORDER BY RANDOM() LIMIT 1");
        Question question = new Question();
        if (data != null && data.moveToFirst()) {
            question.setId(data.getInt(0));
            question.setQuestionNumber(data.getInt(1));
            question.setQuestion(data.getString(2));
            question.setAnswer1(data.getString(3));
            question.setAnswer2(data.getString(4));
            question.setAnswer3(data.getString(5));
            question.setAnswer4(data.getString(6));
            question.setCorrectAnswer(data.getInt(7));
            question.setAudioFileName(data.getString(8));
        }
        return question;
    }
}
class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //truy vấn không trả kết quả:insert, update,delete
    public void QueryData (String sql){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }
    //truy vấn có trả kết quả
    public Cursor GetData(String sql){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql,null);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}