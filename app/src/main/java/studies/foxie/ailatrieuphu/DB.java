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
            db.QueryData("CREATE TABLE IF NOT EXISTS Questions (id INTEGER PRIMARY KEY AUTOINCREMENT, questionNumber Integer, question NVARCHAR(200), answer1 NVARCHAR(50), answer2 NVARCHAR(50), answer3 NVARCHAR(50), answer4 NVARCHAR(50), correctAnswer Integer)");
            db.QueryData("INSERT INTO Questions  VALUES (null, 1, 'Sông Nile chảy ra biển nào?', 'Biển Đỏ', 'Biển Địa Trung Hải', 'Biển Đen', 'Biển BaTick', 2)");
            db.QueryData("INSERT INTO Questions  VALUES (null, 1, 'Sông Nile chảy ra biển nào? 2', 'Biển Đỏ', 'Biển Địa Trung Hải', 'Biển Đen', 'Biển BaTick', 2)");
            db.QueryData("INSERT INTO Questions  VALUES (null, 1, 'Sông Nile chảy ra biển nào? 3', 'Biển Đỏ', 'Biển Địa Trung Hải', 'Biển Đen', 'Biển BaTick', 2)");
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