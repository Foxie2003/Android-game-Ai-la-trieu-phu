package studies.foxie.ailatrieuphu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;

public class GradientTextView extends androidx.appcompat.widget.AppCompatTextView {

    public GradientTextView(Context context) {
        super(context);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = getPaint();
        // Tạo một gradient theo chiều ngang
        @SuppressLint("DrawAllocation") Shader shader = new LinearGradient(0, 0, 0, getHeight(),
                new int[]{0xFF2dfdf9, 0xFFffffff, 0xFFfd18d6},
                null, Shader.TileMode.CLAMP);
        paint.setShader(shader);

        super.onDraw(canvas);
    }
}
