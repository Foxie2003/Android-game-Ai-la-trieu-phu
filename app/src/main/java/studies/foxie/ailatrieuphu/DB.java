package studies.foxie.ailatrieuphu;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DB {
    DataBaseHelper db;
    public DB(Context applicationContext) {
        db = new DataBaseHelper(applicationContext, "ALTP.sql", null, 1);
    }
    public void createDatabase() {
        //Tạo bảng PlayerInfo nếu nó chưa tồn tại
        Cursor playerInfoCursor = db.GetData("SELECT name FROM sqlite_master WHERE type='table' AND name='PlayerInfo'");
        if (playerInfoCursor == null || playerInfoCursor.getCount() <= 0) {
            db.QueryData("CREATE TABLE IF NOT EXISTS PlayerInfo (PlayerName NVARCHAR(20), AvatarId INTEGER, FrameId INTEGER, Money Integer, Diamond Integer, HighestQuestionNumber Integer, AnsweredQuestion Integer, CorrectAnsweredQuestion Integer)");
            db.QueryData("INSERT INTO PlayerInfo VALUES ('Khách', 1, 1, 12345678, 100, 0, 0, 0)");
        }
        //Tạo bảng ItemCategory nếu nó chưa tồn tại
        Cursor itemCategoryCursor = db.GetData("SELECT name FROM sqlite_master WHERE type='table' AND name='ItemCategory'");
        if (itemCategoryCursor == null || itemCategoryCursor.getCount() <= 0) {
            db.QueryData("CREATE TABLE IF NOT EXISTS ItemCategoryCursor (id INTEGER PRIMARY KEY AUTOINCREMENT, categoryName NVARCHAR(20))");
            db.QueryData("INSERT INTO ItemCategoryCursor VALUES (null, 'Tài sản')");
            db.QueryData("INSERT INTO ItemCategoryCursor VALUES (null, 'Avatar')");
            db.QueryData("INSERT INTO ItemCategoryCursor VALUES (null, 'Khung')");
        }
        //Tạo bảng Items nếu nó chưa tồn tại
        Cursor itemsCursor = db.GetData("SELECT name FROM sqlite_master WHERE type='table' AND name='Items'");
        if (itemsCursor == null || itemsCursor.getCount() <= 0) {
            db.QueryData("CREATE TABLE IF NOT EXISTS Items (id INTEGER PRIMARY KEY AUTOINCREMENT, categoryId INTEGER, name NVARCHAR(20), price INTEGER, image VARCHAR, isBought INTEGER, FOREIGN KEY (categoryId) REFERENCES ItemCategory(id))");
//            db.QueryData("INSERT INTO Items VALUES (null, 2, 'Mặc định', 0, 'image', 1)");
//            db.QueryData("INSERT INTO Items VALUES (null, 3, 'Mặc định', 0, 'image', 1)");
            db.QueryData("INSERT INTO Items VALUES (null, 1, 'Xe máy 1',1000000, 'item_motorbike_1', 0)");
            db.QueryData("INSERT INTO Items VALUES (null, 1, 'Xe máy 2',1000000, 'item_motorbike_2', 0)");
            db.QueryData("INSERT INTO Items VALUES (null, 1, 'Xe máy 3',1000000, 'item_motorbike_3', 0)");
            db.QueryData("INSERT INTO Items VALUES (null, 1, 'Xe máy 4',1000000, 'item_motorbike_4', 0)");
            db.QueryData("INSERT INTO Items VALUES (null, 1, 'Xe máy 5',1000000, 'item_motorbike_5', 0)");
            db.QueryData("INSERT INTO Items VALUES (null, 1, 'Xe máy 6',1000000, 'item_motorbike_6', 0)");
            db.QueryData("INSERT INTO Items VALUES (null, 1, 'Xe hơi 1',1000000, 'item_car_1', 0)");
            db.QueryData("INSERT INTO Items VALUES (null, 1, 'Xe hơi 2',1000000, 'item_car_2', 0)");
            db.QueryData("INSERT INTO Items VALUES (null, 1, 'Xe hơi 3',1000000, 'item_car_3', 0)");
            db.QueryData("INSERT INTO Items VALUES (null, 1, 'Xe hơi 4',1000000, 'item_car_4', 0)");
            db.QueryData("INSERT INTO Items VALUES (null, 1, 'Xe hơi 5',1000000, 'item_car_5', 0)");
            db.QueryData("INSERT INTO Items VALUES (null, 1, 'Xe hơi 6',1000000, 'item_car_6', 0)");
            db.QueryData("INSERT INTO Items VALUES (null, 1, 'Nhẫn 1',1000000, 'item_ring_1', 0)");
            db.QueryData("INSERT INTO Items VALUES (null, 1, 'Nhẫn 2',1000000, 'item_ring_2', 0)");
            db.QueryData("INSERT INTO Items VALUES (null, 1, 'Nhẫn 3',1000000, 'item_ring_3', 0)");
            db.QueryData("INSERT INTO Items VALUES (null, 1, 'Nhà 1',1000000, 'item_house_1', 0)");
            db.QueryData("INSERT INTO Items VALUES (null, 1, 'Nhà 2',1000000, 'item_house_2', 0)");
            db.QueryData("INSERT INTO Items VALUES (null, 1, 'Nhà 3',1000000, 'item_house_3', 0)");
        }
        //Tạo bảng Questions nếu nó chưa tồn tại
        Cursor QuestionsCursor = db.GetData("SELECT name FROM sqlite_master WHERE type='table' AND name='Questions'");
        if (QuestionsCursor == null || QuestionsCursor.getCount() <= 0) {
            //questionNumber: câu hỏi có thể xuất hiện ở câu số ...
            db.QueryData("CREATE TABLE IF NOT EXISTS Questions (id INTEGER PRIMARY KEY AUTOINCREMENT, questionNumber Integer, question NVARCHAR(200), answer1 NVARCHAR(50), answer2 NVARCHAR(50), answer3 NVARCHAR(50), answer4 NVARCHAR(50), correctAnswer Integer, audioFileName NVARCHAR(50))");
            db.QueryData("INSERT INTO Questions  VALUES (null, 1, 'Hình nào sau đây có ba cạnh?', 'Tam giác', 'Tứ giác', 'Ngũ giác', 'Lục giác', 1, 'q1_4')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 1, 'Theo luật giao thông đường bộ loại xe nào sau đây được ưu tiên khi đang làm nhiệm vụ?', 'Xe lu', 'Xe ba gác', 'Xe cứu thương', 'Xe thồ', 3, 'q1_5')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 1, 'Quả nào khi còn xanh thường để làm món nộm?', 'Hồng Xiêm', 'Cau', 'Bơ', 'Đu đủ', 4, 'q1_7')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 1, 'Nhân vật nào sau đây gắn với truyền thuyết về bánh chưng, bánh giày?', 'Trọng Thủy', 'Sơn Tinh', 'Lang Liêu', 'Trương Chi', 3, 'q1_8')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 1, 'Đâu là tên một loại đàn?', 'Bí', 'Mướp', 'Bầu', 'Dưa', 3, 'q2_2')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 1, 'Loài hoa nào thường nở vào buổi tối?', 'Hoa 10 giờ', 'Hoa quỳnh', 'Hoa Đồng Tiền', 'Hoa Hồng', 2, 'q2_3')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 1, 'Con còng là con gì?', 'Dã tràng', 'Muỗi', 'Bọ cạp', 'Bạch tuộc', 1, 'q2_8')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 1, 'Con vật nào sau đây không có sừng?', 'Dê', 'Tê giác', 'Trâu', 'Gà lôi', 4, 'q2_9')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 1, 'Theo một câu chuyện dân gian thì con vật gì đã chạy đua với rùa?', 'Trâu', 'Thỏ', 'Ngựa', 'Hươu', 2, 'q2_10')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 1, 'Đâu không phải tên một kiểu bơi?', 'Bơi ếch', 'Bơi bướm', 'Bơi ruồi', 'Bơi chó', 3, 'q3_2')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 1, 'Có câu \"Ăn chắc mặc ...\" gì?', 'Xịn', 'Bền', 'Chắc', 'Mỏng', 2, 'q3_3')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 1, 'Đâu là một câu khẩu ngữ có nghĩa thông dụng trong cuộc sống?', 'Run như cầy tơ', 'Run như cầy hương', 'Run như cầy cáo', 'Run như cầy sấy', 4, 'q3_4')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 2, 'Đâu không phải là tên của nhân vật trong truyện Doraemon?', 'Chaiko', 'Chaien', 'Điệp viên 007', 'Xuka', 3, 'q3_6')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 2, 'Đâu không phải là tên một loại rau dùng để làm thực phẩm?', 'Dền đỏ', 'Dền cơm', 'Dền gai', 'Dền dúi', 4, 'q4_3')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 2, 'Theo phương ngữ Nam Bộ thì khóm là quả gì?', 'Na', 'Bưởi', 'Dứa', 'Mít', 3, 'q4_4')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 2, 'Theo truyện cổ tích Lọ nước thần \"Dọc bằng đòn gánh củ bằng bình vôi\" là câu rao bán loại củ gì?', 'Củ cải', 'Củ hành', 'Củ từ', 'Củ ấu', 2, 'q4_8')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 2, 'Sakura là tên loài hoa nào ở Nhật Bản?', 'Anh Đào', 'Lan', 'Huệ', 'Hồng', 1, 'q5_3')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 2, 'Đâu không phải là một dụng cụ học tập?', 'Com-pa', 'Chiếu manh', 'Thước kẻ', 'Êke', 2, 'q5_4')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 2, 'Có câu \"Dài lưng thì ...\" làm sao?', 'Đau lưng', 'Dài tay', 'Tốn vải', 'Lắm việc', 3, 'q6_1')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 2, 'Từ nào sau đây chỉ đứa trẻ thông minh có năng khiếu đặc biệt?', 'Thần nhôm', 'Thần bạc', 'Thần đồng', 'Thần nhựa', 3, 'q6_4')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 2, 'Câu ca dao \"Lá xanh, bông trắng lại chen nhị vàng\" miêu tả loài hoa nào?', 'Sen', 'Chanh', 'Bưởi', 'Nâu', 1, 'q6_5')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 2, 'Đâu là tên một món bún nổi tiếng của Huế?', 'Nai', 'Lạc đà', 'Ngựa', 'Bò', 4, 'q7_1')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 2, 'Trong bài đồng dao \"Chú mèo mà trèo cây cau\" chú mèo đã hỏi thăm con vật nào?', 'Chó', 'Chim', 'Chuột', 'Trâu', 3, 'q7_2')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 2, 'Đâu là tên một loại rau thơm?', 'Vị', 'Mùi', 'Hương', 'Sắc', 2, 'q7_3')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 3, 'Đâu là tên một loại mướp?', 'Cay', 'Bùi', 'Nồng', 'Đắng', 4, 'q7_5')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 3, 'Theo một câu hát thì \"Kìa chú là chú ếch con, có hai là hai mắt ...\" làm sao?', 'Vuông', 'Tròn', 'Tam giác', 'Hình thang', 2, 'q8_1')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 3, 'Đâu là một động từ?', 'Khế', 'Mận', 'Đào', 'Mít', 3, 'q8_2')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 3, 'Con vật nào sau đây to hơn cả?', 'Chim ruồi', 'Chim sẻ', 'Gà tre', 'Gà tây', 4, 'q8_3')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 3, 'Từ \"tha thướt\" thường được dùng để miêu tả loại áo nào?', 'Áo chống đạn', 'Áo dài', 'Áo chống cháy', 'Áo phao', 2, 'q8_4')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 3, 'Bộ dạng khép nép, sợ sệt, né tránh không dám nhìn thẳng được gọi là gì?', 'Hừng hực', 'Hân hoan', 'Len lét', 'Hung hãn', 3, 'q9_2')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 3, 'Từ nào còn thiếu trong câu sau: \"Hay làm thì đói, hay ... thì no\"?', 'Đi chơi', 'Ăn chơi', 'Nói', 'Lười nhác', 3, 'q9_5')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 3, 'Đâu là một bộ phận của máy tính?', 'Ổ chim', 'Ổ gà', 'Ổ cứng', 'Ổ voi', 3, 'q9_10')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 3, 'Đâu là một thành ngữ chỉ sự nhập nhằng, không rõ ràng?', 'Bút sa gà chết', 'Hai năm rõ mười', 'Giấy trắng mực đen', 'Nửa nạc nửa mỡ', 4, 'q10_1')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 3, 'Từ nào thường được dùng để miêu tả vẻ đẹp của một chàng trai?', 'Yểu điệu', 'Thướt tha', 'Cường tráng', 'Nhu mì', 3, 'q10_4')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 3, 'Từ nào sau đây chỉ tên một loại gia vị?', 'Tốn', 'Tiêu', 'Mất', 'Đưa', 2, 'q10_10')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 3, 'Vật dụng nào dùng để múc nước?', 'Đũa cả', 'Gáo', 'Rế', 'Kiềng', 2, 'q11_2')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 4, '\"Nu na nu nống\" là gì?', 'Một kiểu ghế tựa', 'Tên một trò chơi dân gian', 'Một địa danh', 'Một loài hoa', 2, 'q11_3')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 4, 'Đâu là tên một truyện cổ tích nổi tiếng?', 'Con cóc là chị ông trời', 'Con cóc là bác ông trời', 'Con cóc là cậu ông trời', 'Con cóc là em ông trời', 3, 'q11_4')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 4, 'Theo một bài đồng dao ngược thì \"Một chục quả hồng nuốt ...\" ai?', 'Lão bảy mươi', 'Lão tám mươi', 'Lão chín mươi', 'Lão một trăm', 2, 'q11_5')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 4, 'Nhân vật nào không có trong truyện Tây Du Ký?', 'Tôn Ngộ Không', 'Trư Bát Giới', 'Sa Ngộ Tĩnh', 'Batman', 4, 'q12_1')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 4, 'Đâu là tên một bài hát nổi tiếng?', 'Hai anh em trên một chiếc xe tăng', 'Bốn anh em trên một chiếc xe tăng', 'Năm anh em trên một chiếc xe tăng', 'Bảy anh em trên một chiếc xe tăng', 3, 'q12_2')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 4, 'Từ nào còn thiếu trong câu thành ngữ sau: \"Lành như...\"?', 'Bão', 'Đất', 'Lũ', 'Lửa', 2, 'q12_5')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 4, 'Con vật nào không xuất hiện trong lời bài hát \"Chú ếch con\" của nhạc sĩ Phan Nhân?', 'Cá rô phi', 'Cá rô ron', 'Cá chuối', 'Cá trê', 3, 'q12_9')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 4, 'Từ nào còn thiếu trong câu khẩu ngữ thông dụng sau\"Bỏ ... chạy lấy người\"?', 'Xe hơi', 'Của', 'Nhà lầu', 'Vàng', 2, 'q12_10')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 4, 'Đâu là cách thường gọi một kiểu cắt tóc ngắn của nam giới?', 'Tôm', 'Cua', 'Trai', 'Ốc', 2, 'q13_5')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 4, 'Kẹo dừa là đặc sản của địa phương nào?', 'Bến Tre', 'Đồng Nai', 'Quảng Nam', 'Nghệ An', 1, 'q13_6')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 4, 'Đâu không phải là tên một loại bánh ăn được?', 'Bánh chưng', 'Bánh tét', 'Bánh xe', 'Bánh dày', 3, 'q13_7')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 5, 'Từ nào sau đây không phải là từ láy?', 'Lấp lánh', 'Lo lắng', 'Lay lắt', 'Lẫn lộn', 4, 'q13_9')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 5, 'Người ta thường cho quả gì vào nước luộc rau muống để thành món canh chua?', 'Dừa xiêm', 'Dưa lê', 'Sấu', 'Trứng gà', 3, 'q14_1')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 5, 'Có câu: \"Vắng chủ nhà gà vọc niêu ...\" gì?', 'Đuôi bò hầm thuốc bắc', 'Tôm', 'Vây cá mập', 'Gà hầm hạt sen', 2, 'q14_2')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 5, 'Đâu là tên gọi của một loài hoa trong tự nhiên?', 'Mua', 'Bán', 'Cho', 'Tặng', 1, 'q14_3')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 5, 'Có câu: \"Quýt làm ...\" thì cái gì \"chịu\"?', 'Ổi', 'Dứa', 'Roi', 'Cam', 4, 'q14_4')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 5, 'Câu thành ngữ \"Chớ thấy sóng cả mà ngã tay chèo\" nói hoặc khuyên về điều gì?', 'Kinh nghiệm vượt sóng', 'Giữ gìn nhan sắc', 'Lòng kiên trì', 'Lòng hiếu thảo', 3, 'q14_10')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 5, 'Thành ngữ Việt Nam có câu: \"Ăn cây táo, rào cây ...\" gì?', 'Vú sữa', 'Che-ri', 'Sa-pô-chê', 'Sung', 4, 'q15_1')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 5, 'Theo một câu chuyện dân gian thì con vật nào là cậu ông trời?', 'Con gà', 'Con cóc', 'Con mèo', 'Con thỏ', 2, 'q15_3')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 5, 'Tóc tơ là loại tóc có đặc điểm gì?', 'Xơ xác', 'Mềm', 'Cứng quèo', 'Đứt gãy', 2, 'q15_4')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 5, 'Có câu: \"Hóc xương gà, sa cành ...\" gì?', 'Xà lách', 'Ô-liu', 'Khế', 'Súp-lơ', 3, 'q15_5')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 5, 'Củ nào sau đây có vị cay?', 'Khoai tây', 'Cà rốt', 'Sen', 'Gừng', 4, 'q15_10')");

            db.QueryData("INSERT INTO Questions  VALUES (null, 6, 'Sông Nile chảy ra biển nào?', 'Biển Đỏ', 'Biển Địa Trung Hải', 'Biển Đen', 'Biển Ban Tích', 2, 'q1_1')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 6, 'Đâu là một thành phố của nước Nga?', 'Vancouver', 'Amsterdam', 'Berlin', 'Sant Peterburg', 4, 'q1_6')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 6, 'Từ nào sau đây là động từ?', 'Nhất', 'Lục', 'Cửu', 'Tứ', 2, 'q2_4')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 6, 'Trong ẩm thực Nhật Bản, matcha là loại nguyên liệu nào?', 'Bột gạo', 'Bột trà xanh', 'Bột ngô', 'Bột đậu', 2, 'q2_6')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 6, 'Tranh đông hồ truyền thống được vẽ trên chất liệu gì?', 'Cát', 'Giấy dó', 'Tường', 'Kính', 2, 'q3_5')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 6, 'Trong bài thơ \"Đêm nay Bác không ngủ\" đoàn dân công ngủ trong rừng đã dùng cái gì làm chăn?', 'Áo', 'Quần', 'Váy', 'Khăn', 1, 'q3_7')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 6, 'Hòn đảo Tôm Hùm là tên gọi khác của đảo nào sau đây ở nước ta?', 'Đảo Bình Ba', 'Đảo Hòn Mun', 'Đảo Ngọc', 'Đảo Lý Sơn', 1, 'q3_9')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 6, 'Rượu rum được làm từ nguyên liệu nào sau đây?', 'Ngũ cốc', 'Mía đường', 'Cây xương rồng', 'Hoa quả', 3, 'q3_10')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 6, 'Đâu là tên một hồ nổi tiếng ở Hà Nội?', 'Biển Hồ', 'Hồ Than Thở', 'Hồ Ngọc Hà', 'Hồ Lắk', 3, 'q4_1')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 6, 'Trong bài hát \"Mẹ yêu không nào\" con cò đã đậu trên cành gì?', 'Cành trúc', 'Cành tre', 'Cành mai', 'Cành bưởi', 2, 'q4_2')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 6, 'Bộ phim hoạt hình nào có nhân vật chính là loài cá?', 'Phi đội gà bay', 'Cậu bé rừng xanh', 'Kẻ cắp mặt trăng', 'Đi tìm Nemo', 4, 'q4_5')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 6, 'Theo một bộ luật được ban hành năm 1992 nhai kẹo cao su là hành động bị cấm ở nước nào?', 'Singapore', 'Brunei', 'Đông Timor', 'Malaysia', 1, 'q4_7')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 7, 'Gari là món ăn của nước nào?', 'Thái Lan', 'Nhật Bản', 'Hàn Quốc', 'Trung Quốc', 2, 'q4_9')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 7, 'Đâu là tên một bộ phim của Việt Nam?', 'Để tính luôn', 'Để ngày kia tính', 'Để mai tính', 'Để lúc khác tính', 3, 'q5_1')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 7, 'Phạm Tiến Duật là ai?', 'Bác sĩ', 'Diễn viên múa', 'Tay đua F1', 'Nhà thơ', 4, 'q5_2')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 7, 'Nguyễn Thị Ánh Viên là vận động viên nổi tiếng trong bộ môn thể thao nào?', 'Bơi lội', 'Golf', 'Bóng đá', 'Wushu', 1, 'q5_5')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 7, 'Đâu là một tính từ?', 'Nới', 'Với', 'Mới', 'Bới', 3, 'q5_6')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 7, 'Đâu là một tên gọi khác của cây gạo?', 'Mộc thương', 'Mộc miên', 'Mộc qua', 'Mộc tẩm', 2, 'q5_10')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 7, 'Thủ đô của Argentina là thành phố nào?', 'Amsterdam', 'Buenos Aires', 'Paris', 'London', 2, 'q6_2')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 7, 'Người miền Bắc thường cho loại rau gia vị nào vào cháo để giải cảm?', 'Hành tây', 'Tía tô', 'Diếp cá', 'Húng quế', 2, 'q6_3')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 7, 'Hang Trống và hang Trinh Nữ là những địa danh thuộc tỉnh nào?', 'Quảng Ninh', 'Ninh Bình', 'Khánh Hòa', 'Cà Mau', 1, 'q6_8')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 7, 'Những câu thơ \"Con dù lớn vẫn là con của mẹ Đi hết đời lòng mẹ vẫn theo con\" nằm trong bài thơ nào?', 'Con đi sơ tán xa', 'Con cò', 'Con tập nói', 'Con thức dậy', 2, 'q6_10')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 7, 'Cà cuống là gì?', 'Một loại cà', 'Một kiểu tóc', 'Một loài côn trùng', 'Một kiểu đi', 3, 'q7_4')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 8, 'Nước nào sau đây có rừng Tai-ga?', 'Na Uy', 'Đức', 'Áo', 'Hà Lan', 1, 'q7_9')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 8, 'Theo quan niệm của người Mường trong sử thi: \"Đẻ đất đẻ nước\" thì ông Pồng Pêu là thần gì?', 'Thần nắng', 'Thần mưa', 'Thần gió', 'Thần sấm', 2, 'q7_10')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 8, 'Không có loại vitamin nào sau đây?', 'Vitamin B', 'Vitamin C', 'Vitamin E', 'Vitamin Ô', 4, 'q8_5')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 8, 'Ga tàu nào không nằm trên trục đường sắt Bắc-Nam?', 'Hải Phòng', 'Phủ Lý', 'Diêu Trì', 'Biên Hòa', 1, 'q8_6')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 8, 'Đâu là tên một bài hát của nhạc sĩ Thế Hiển?', 'Tóc em đuôi voi', 'Tóc em đuôi ngựa', 'Tóc em đuôi sam', 'Tóc em đuôi gà', 4, 'q9_1')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 8, '27-2 là ngày kỉ niệm nào sau đây?', 'Ngày Thầy thuốc Việt Nam', 'Ngày Quốc tế Thiếu nhi', 'Ngày Quốc tế Phụ nữ', 'Ngày Giải phóng Thủ đô', 1, 'q9_3')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 8, 'Đâu là một giải thưởng thuộc lĩnh vực điện ảnh?', 'Quả chuối vàng', 'Mâm xôi vàng', 'Múi mít vàng', 'Bắp ngô vàng', 2, 'q9_4')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 8, 'Địa danh Thanh Trì - Hà Nội nổi tiếng với món ăn gì?', 'Chả rươi', 'Bánh cuốn', 'Bún bò', 'Bánh canh', 2, 'q10_2')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 8, 'Đâu là tên một bộ phim của đạo diễn Đặng Nhật Minh?', 'Mùa dưa hấu', 'Mùa ổi', 'Mùa mãng cầu', 'Mùa hồng xiêm', 2, 'q10_5')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 8, 'Thôn Cao - Hưng Yên nổi tiếng với nghề truyền thống nào?', 'Làm hương', 'Đóng tàu', 'Làm mộc', 'Rèn', 1, 'q10_6')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 8, '\"Khúc hát mặt trời\" là dự án phim hợp tác giữa Việt Nam và nước nào?', 'Hàn Quốc', 'Thái Lan', 'Nhật Bản', 'Ấn Độ', 3, 'q10_8')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 9, 'Bài hát \"Chàng buông vạt áo em ra\" thuộc loại hình nghệ thuật dân gian nào?', 'Tuồng', 'Chèo', 'Cải lương', 'Dân ca Quan họ', 4, 'q10_9')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 9, 'Đâu là tên gọi dân gian của một hiện tượng thiên nhiên?', 'Gấu ăn trăng', 'Lợn ăn trăng', 'Kiến ăn trăng', 'Sói ăn trăng', 1, 'q11_1')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 9, 'Đâu là câu mở đầu bài hát \"Từ rừng xanh cháu về thăm lăng Bác\"?', 'Núi muốn hỏi', 'Đứng trên quảng trường', 'Nhớ thảo nguyên xanh', 'Đi từ bản làng xa xôi', 4, 'q11_6')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 9, 'Núi Chóp Chài, ghềnh Đá Đĩa, thác Vực Hòm là những địa danh thuộc tỉnh nào?', 'Khánh Hòa', 'Phú Yên', 'Ninh Thuận', 'Bình Thuận', 2, 'q11_8')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 9, 'Bác Hồ viết \"Lời kêu gọi toàn quốc kháng chiến\" tại đâu?', 'Làng Vạn Phúc', 'Làng Bát Tràng', 'Làng Hương Trà', 'Làng Mỹ Khánh', 1, 'q11_9')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 9, '\"Roti gluay\" - món ăn nổi tiếng của Thái Lan có nguyên liệu chính là gì?', 'Chuối và trứng', 'Thịt thỏ và tiêu đen', 'Khoai tây và cốt dừa', 'Nộm đu đủ', 1, 'q11_10')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 9, 'Ngày thầy thuốc Việt Nam là ngày nào?', '27/2', '30/4', '1/6', '2/9', 1, 'q12_3')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 9, 'Thành phố Pleiku thuộc tỉnh nào nước ta?', 'Phú Thọ', 'Hòa Bình', 'Bắc Ninh', 'Gia Lai', 4, 'q12_4')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 9, 'Thủ đô của Canada có tên là gì?', 'Montreal', 'Ottawa', 'Quebec', 'Ontario', 2, 'q12_6')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 9, 'Đất nước nào sau đây không giáp biển?', 'Áo', 'Bỉ', 'Đức', 'Pháp', 1, 'q12_7')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 9, 'Tập thơ \"Bức tranh quê\" được sáng tác năm 1939 là của nữ thi sĩ nào?', 'Anh Thơ', 'Hằng Phương', 'Ngân Giang', 'Xuân Quỳnh', 1, 'q13_1')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 10, 'Thành phố Tallinn là thủ đô của quốc gia nào?', 'Litva', 'Latvia', 'Estonia', 'Malta', 3, 'q13_2')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 10, 'Đâu là tên gọi một loại bưởi ngon có tiếng ở miền Tây Nam Bộ?', 'Bưởi da trắng', 'Bưởi da xanh', 'Bưởi da vàng', 'Bưởi da cam', 2, 'q13_4')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 10, 'Khu đền Angkor nổi tiếng thuộc nước nào?', 'Campuchia', 'Lào', 'Thái Lan', 'Indonesia', 1, 'q13_8')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 10, 'Thị trấn Rạch Gốc tỉnh Cà Mau nổi tiếng với đặc sản gì?', 'Tôm khô', 'Bánh khọt', 'Tỏi', 'Bánh canh', 1, 'q13_10')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 10, 'Mehico thuộc châu lục nào?', 'Châu Á', 'Châu Phi', 'Châu Âu', 'Châu Mỹ', 4, 'q14_5')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 10, 'Đơn vị tiền tệ của Thụy Sĩ là gì?', 'Euro', 'Franc', 'Koruna', 'Leu', 2, 'q14_6')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 10, 'Địa danh \"phà Ghép\" nổi tiếng trong thời kháng chiến chống Mỹ nằm ở tỉnh nào?', 'Quảng Bình', 'Nghệ An', 'Hà Tĩnh', 'Thanh Hóa', 4, 'q14_7')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 10, 'Tháng đầu sau khi sinh con của người phụ nữ gọi là gì?', 'Ở cữ', 'Ở đợ', 'Ở vậy', 'Ở chung', 1, 'q15_2')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 10, 'Trong các cụm từ Hán - Việt sau, từ \"thủ\" ở trong cụm từ nào mang ý nghĩa là \"tay\"?', 'Thủ túc', 'Thủ cựu', 'Thủ lĩnh', 'Thủ đô', 1, 'q15_6')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 10, '\"Chè lam Phủ Quảng\" là món đặc sản của tỉnh thành nào?', 'Phú Thọ', 'Thanh Hóa', 'Quảng Bình', 'Quảng Trị', 2, 'q15_8')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 10, 'Lễ hội nào sau đây được tổ chức mỗi năm hai lần tại vùng Tháp Mười - Đồng Tháp?', 'Lễ hội Kỳ Yên', 'Lễ hội Lăng Ông Thượng', 'Lễ hội đền Bà Đen', 'Lễ hội Gò Tháp', 4, 'q15_9')");

            db.QueryData("INSERT INTO Questions  VALUES (null, 11, 'Chú bé rắc rối là tác phẩm của nhà văn nào sau đây?', 'Nguyễn Việt Hà', 'Nguyễn Ngọc Tư', 'Nguyễn Thị Thu Huệ', 'Nguyễn Nhật Ánh', 4, 'q1_2')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 11, 'Cây cầu đẹp và lãng mạn nào nằm trên dòng sông Vltava ở thủ đô CH Séc?', 'Cầu Charles', 'Cầu Sick', 'Cầu Alexander III', 'Cầu Rianto', 1, 'q1_3')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 11, 'Đèo Ô Quy Hồ nối liền tỉnh Lào Cai với tỉnh nào?', 'Lai Châu', 'Hà Giang', 'Yên Bái', 'Tuyên Quang', 1, 'q1_9')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 11, 'Nhà Thơ nào đã tự ví \"Lòng ta là những hàng thành quách cũ Từ ngàn năm bỗng vẳng tiếng loa xưa\"?', 'Vũ Đình Liên', 'Huy Cận', 'Tản Đà', 'Chế Lan Viên', 1, 'q1_10')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 11, 'Alexandre Dumas là ai?', 'Cầu thủ bóng chuyền', 'VĐV leo núi', 'Tay đua F1', 'Nhà văn', 4, 'q2_1')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 11, 'Đâu là tên một ca khúc của nhạc sĩ Phó Đức Phương?', 'Chảy đi tóc ơi', 'Chảy đi nước ơi', 'Chảy đi sông ơi', 'Chảy đi gió ơi', 3, 'q2_5')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 11, '\"Nước non nặng một nhời thề, Nước đi đi mãi không về cùng non.\" là những câu thơ của tác giả nào?', 'Tản Đà', 'Chính Hữu', 'Hàn Mặc Tử', 'Thế Lữ', 1, 'q2_7')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 11, 'Bài hát nào sau đây của nhạc sĩ Trần Long Ẩn nằm trong phong trào \"Hát cho đồng bào tôi nghe\"?', 'Tình đất đỏ miền Đông', 'Đêm thành phố đầy sao', 'Một đời người một rừng cây', 'Người mẹ bàn cờ', 4, 'q3_1')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 12, 'Trên ba mặt của cột mốc cực Tây Việt Nam tại A Pa Chải - Điện Biên không có thứ tiếng nào sau đây?', 'Tiếng Việt', 'Tiếng Trung', 'Tiếng Lào', 'Tiếng Campuchia', 4, 'q3_8')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 12, 'Hình ảnh gì được tôn vinh trong bài hát \"Một thoáng quê hương\" của nhạc sĩ Từ Huy?', 'Đôi guốc mộc', 'Nón quai thao', 'Tà áo dài', 'Áo tứ thân', 3, 'q4_6')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 12, 'Tên của quảng trường Trafalgar ở thủ đô London - Anh có xuất xứ từ đâu?', 'Một di tích khảo cổ', 'Tên một danh tướng', 'Một chiến thắng', 'Tên một hoàng đế', 3, 'q4_10')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 12, 'Bánh khô mè là đặc sản của địa phương nào?', 'Đà Nẵng', 'Ninh Bình', 'Nghệ An', 'Tây Ninh', 1, 'q5_7')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 12, '\"Tàu tốc hành\" là biệt danh của tay vợt nào?', 'David Ferrer', 'Rafael Nadal', 'Novak Djokovic', 'Roger Federer', 4, 'q5_8')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 12, 'Lễ hội San Fermin ở Châu Âu là lễ hội gì?', 'Ném cà chua', 'Đua bò tót', 'Lăn pho-mát', 'Đón mùa đông', 2, 'q5_9')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 12, 'Đâu là tên một sáng tác của nhạc sĩ Quốc Bảo?', 'Em về tóc bạc', 'Em về tóc tai', 'Em về tóc tém', 'Em về tóc xanh', 4, 'q6_6')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 13, 'Câu hát \"Thế giới muốn hòa bình và chán ghét chiến tranh\" nằm trong bài hát nào của nhạc sĩ Phạm Tuyên?', 'Cánh én tuổi thơ', 'Tiếng chuông và ngọn cờ', 'Tiến lên đoàn viên', 'Chiến đấu vì độc lập tự do', 2, 'q6_7')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 13, 'Chùa nào còn có tên gọi là chùa Ve Chai?', 'Chùa Linh Phước', 'Chùa Vĩnh Nghiêm', 'Chùa Thiên Mụ', 'Chùa Keo', 1, 'q6_9')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 13, 'Theo phân loại trong thực vật học loài cây nào sau đây không thuộc họ cau?', 'Dẻ thơm', 'Cọ', 'Chà là', 'Mây', 1, 'q7_6')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 13, 'Nhạc cụ nào được nhắc đến trong câu sau \"Phi ... bất thành chèo\"?', 'Sáo', 'Bầu', 'Kèn', 'Trống', 4, 'q7_7')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 13, 'Ca khúc Tiếng đàn Ta Lư của nhạc sĩ Huy Thục đã thể hiện tinh thần lạc quan cách mạng của những người dân ở đâu?', 'Nam Định', 'Tiền Giang', 'Quảng Trị', 'Cà Mau', 3, 'q7_8')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 13, 'Những câu thơ \"Bàn tay ta làm nên tất cả. Có sức người sỏi đá cũng thành cơm\" là của nhà thơ nào?', 'Chế Lan Viên', 'Trần Hữu Thung', 'Hoàng Trung Thông', 'Chính Hữu', 3, 'q8_7')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 13, 'Ngọn núi nào ở châu Phi mang tên một vị thần Hy Lạp?', 'Siemen', 'Elgon', 'Atlas', 'Meru', 3, 'q8_8')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 14, 'Thủ lĩnh cuộc khởi nghĩa nông dân Nguyễn Hữu Cầu được sử sách ghi danh với biệt hiệu là gì?', 'Quận Hẻo', 'Quận He', 'Quận Huy', 'Quận Tường', 2, 'q8_9')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 14, 'Làng nào được nhắc đến trong bài thơ \"Bên kia sông Đuống\" của nhà thơ Hoàng Cầm?', 'Làng hoa Ninh Phúc', 'Làng lụa Vạn Phúc', 'Làng gốm Thổ Hà', 'Làng tranh Đông Hồ', 4, 'q8_10')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 14, '\"Đội quân màu thiên thanh\" là biệt danh của đội bóng nước nào?', 'Hà Lan', 'Ý', 'Đức', 'Na Uy', 2, 'q9_6')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 14, 'Linh vật của Thế vận hội Olympic 2016 mang hình ảnh (dáng dấp) của con vật gì?', 'Dê', 'Ngựa', 'Chó', 'Mèo', 4, 'q9_7')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 14, 'Tục căng môi bằng đĩa là của thổ dân đất nước nào?', 'Ethiopia', 'Sudan', 'Palestine', 'Ai Cập', 1, 'q9_8')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 14, 'Hạng mục nào sau đây không nằm trong các giải thưởng của Giải Cống hiến?', 'Chương trình của năm', 'Ca sĩ của năm', 'Bài hát của năm', 'Bản phối của năm', 4, 'q9_9')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 14, 'Đâu là tên một loài hoa nổi tiếng ở các tỉnh miền núi phía Bắc?', 'Tam giác mạch', 'Tứ giác mạch', 'Ngũ giác mạch', 'Lục giác mạch', 1, 'q10_3')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 15, 'Tên của huyền thoại bóng đá nào đã được đặt cho sân vận động?', 'Mario Kempes', 'Giuseppe Meazza', 'Michel Platini', 'Zinedine Zidane', 2, 'q10_7')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 15, 'Đảo Ti Tốp ở Vịnh Hạ Long đã được đặt theo tên một phi công người nước nào?', 'Nga', 'Pháp', 'Anh', 'Mỹ', 1, 'q11_7')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 15, 'Theo quan niệm từ xa xưa, thổ dân vùng Pays Basque,Tây Ban Nha chỉ tổ chức lễ cưới khi có sự việc nào xảy ra?', 'Bắt được tê giác', 'Cầu vồng', 'Một con cá voi qua đời', 'Mưa đá', 3, 'q12_8')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 15, 'Địa danh nào ở tỉnh Quảng Trị xuất hiện trong lời bài hát \"Tiếng hát trên đường quê hương\" của nhạc sĩ Huy Thục?', 'Tà Cơn', 'Động Tri', 'Cam lộ', 'Gio An', 3, 'q13_3')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 15, 'Hình ảnh vị anh hùng dân tộc nào đã được tái hiện trong vở tuồng cổ \"Câu thơ yên ngựa\"?', 'Lý Thường Kiệt', 'Lê Hoàn', 'Trần Quang Khải', 'Nguyễn Trãi', 1, 'q14_8')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 15, 'Huyền thoại bóng đá Matthias Sammer chơi ở vị trí nào?', 'Tiền đạo', 'Tiền vệ', 'Hậu vệ', 'Thủ môn', 3, 'q14_9')");
            db.QueryData("INSERT INTO Questions  VALUES (null, 15, '\"Con chim hồng tước nhỏ\" là biệt danh của cầu thủ nào?', 'Garrincha', 'Vava', 'Pele', 'Ferenc Puskas', 1, 'q15_7')");
        }
    }
    //Hàm format định dạng tiền trong game
    public static String formatNumber(long num) {
        String[] suffixes = {"", " Nghìn", " Triệu", " Tỉ", " Nghìn tỉ"}; // Các đơn vị tiền tố
        int suffixIndex = 0;
        double formattedNumber = num;

        while (formattedNumber >= 1000 && suffixIndex < suffixes.length - 1) {
            formattedNumber /= 1000.0;
            suffixIndex++;
        }
        if(num % 10 == 0) {
            return String.format("%.0f%s", formattedNumber, suffixes[suffixIndex]);
        }
        else {
            return String.format("%.2f%s", formattedNumber, suffixes[suffixIndex]);
        }
    }
    public long getMoney() {
        //Lấy bản ghi đầu tiên của cột Money trong bảng PlayerInfo
        Cursor data = db.GetData("SELECT Money FROM PlayerInfo LIMIT 1");
        long money = 0;
        if (data != null && data.moveToFirst()) {
            money = data.getLong(0);
        }
        return money;
    }
    public String getStringMoney() {
        return formatNumber(getMoney());
    }
    public void setMoney(long money) {
        db.QueryData("UPDATE PlayerInfo SET Money = " + money);
    }
    //Hàm cộng tiền cho người chơi khi hoàn thành lượt chơi
    public void addMoney(int money) {
        setMoney(getMoney() + money);
    }
    //Hàm trừ tiền của người chơi khi mua vật phẩm
    public boolean minusMoney(long money) {
        //Kiểm tra xem số tiền của người chơi có lớn hơn hoặc bằng số tiền bị trừ không
        //Nếu lớn hơn hoặc bằng thì thực hiện trừ tiền
        if (getMoney() >= money) {
            setMoney(getMoney() - money);
            return true;
        }
        //Nếu không thì hủy bỏ việc trừ tiền
        else {
            return false;
        }
    }
    public long getDiamond() {
        //Lấy bản ghi đầu tiên của cột Diamond trong bảng PlayerInfo
        Cursor data = db.GetData("SELECT Diamond FROM PlayerInfo LIMIT 1");
        long diamond = 0;
        if (data != null && data.moveToFirst()) {
            diamond = data.getInt(0);
        }
        return diamond;
    }
    public void setDiamond(long diamond) {
        db.QueryData("UPDATE PlayerInfo SET Diamond = " + diamond);
    }
    //Hàm cộng kim cương cho người chơi
    public void addDiamond(int diamond) {
        setDiamond(getDiamond() + diamond);
    }
    //Hàm trừ kim cương của người chơi khi mua trợ giúp
    public boolean minusDiamond(int diamond) {
        //Kiểm tra xem số kim cương của người chơi có lớn hơn hoặc bằng số kim cương bị trừ không
        //Nếu lớn hơn hoặc bằng thì thực hiện trừ kim cương
        if (getDiamond() >= diamond) {
            setDiamond(getDiamond() - diamond);
            return true;
        }
        //Nếu không thì hủy bỏ việc trừ kim cương
        else {
            return false;
        }
    }
    public int getHighestQuestionNumber() {
        //Lấy bản ghi đầu tiên của cột HighestQuestionNumber trong bảng PlayerInfo
        Cursor data = db.GetData("SELECT HighestQuestionNumber FROM PlayerInfo LIMIT 1");
        int highestQuestionNumber = 0;
        if (data != null && data.moveToFirst()) {
            highestQuestionNumber = data.getInt(0);
        }
        return highestQuestionNumber;
    }
    private void setHighestQuestionNumber(int questionNumber) {
        db.QueryData("UPDATE PlayerInfo SET HighestQuestionNumber = " + questionNumber);
    }
    //Hàm cập nhật kỷ lục câu hỏi cao nhất
    public void updateHighestQuestionNumber(int questionNumber) {
        //Nếu câu hỏi cao nhất mới lớn hơn câu hỏi cao nhất cũ thì mới cập nhật
        if(questionNumber > getHighestQuestionNumber()) {
            setHighestQuestionNumber(questionNumber);
        }
    }
    public int getAnsweredQuestion() {
        //Lấy bản ghi đầu tiên của cột AnsweredQuestion trong bảng PlayerInfo
        Cursor data = db.GetData("SELECT AnsweredQuestion FROM PlayerInfo LIMIT 1");
        int answeredQuestion = 0;
        if (data != null && data.moveToFirst()) {
            answeredQuestion = data.getInt(0);
        }
        return answeredQuestion;
    }
    public void setAnsweredQuestion(int answeredQuestion) {
        db.QueryData("UPDATE PlayerInfo SET AnsweredQuestion = " + answeredQuestion);
    }
    //Hàm cập nhật số câu hỏi đã trả lời
    public void updateAnsweredQuestion(int answeredQuestion) {
        setAnsweredQuestion(getAnsweredQuestion() + answeredQuestion);
    }
    public int getCorrectAnsweredQuestion() {
        //Lấy bản ghi đầu tiên của cột AnsweredQuestion trong bảng PlayerInfo
        Cursor data = db.GetData("SELECT CorrectAnsweredQuestion FROM PlayerInfo LIMIT 1");
        int correctAnsweredQuestion = 0;
        if (data != null && data.moveToFirst()) {
            correctAnsweredQuestion = data.getInt(0);
        }
        return correctAnsweredQuestion;
    }
    public void setCorrectAnsweredQuestion(int correctAnsweredQuestion) {
        db.QueryData("UPDATE PlayerInfo SET CorrectAnsweredQuestion = " + correctAnsweredQuestion);
    }
    //Hàm cập nhật số câu hỏi đã trả lời đúng
    public void updateCorrectAnsweredQuestion(int correctAnsweredQuestion) {
        setCorrectAnsweredQuestion(getCorrectAnsweredQuestion() + correctAnsweredQuestion);
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
    public void changeItemBoughtState(int itemId, int newState) {
        // Update the 'isBought' field of the item with the given itemId
        String query = "UPDATE Items SET isBought = " + newState + " WHERE id = " + itemId;
        db.QueryData(query);
    }
    public ArrayList<ShopItem> getItemsByCategory(int categoryId) {
        ArrayList<ShopItem> items = new ArrayList<>();

        // Lấy tất cả bản ghi trong bảng Items với categoryId
        Cursor data = db.GetData("SELECT * FROM Items WHERE categoryId = " + categoryId);

        if (data != null && data.moveToFirst()) {
            do {
                // Tạo một đối tượng ShopItem từ dữ liệu Cursor
                ShopItem item = new ShopItem();
                item.setId(data.getInt(0));
                item.setCategoryId(data.getInt(1));
                item.setName(data.getString(2));
                item.setPrice(data.getInt(3));
                item.setImage(data.getString(4));
                item.setBought((data.getInt(5) == 1));

                // Thêm đối tượng ShopItem vào ArrayList
                items.add(item);
            } while (data.moveToNext());

            // Đóng Cursor sau khi sử dụng
            data.close();
        }

        return items;
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