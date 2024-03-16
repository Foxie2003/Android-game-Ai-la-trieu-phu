package studies.foxie.ailatrieuphu;

import android.content.Context;
import android.media.MediaPlayer;
import java.util.ArrayList;

public class SoundManager {
    private MediaPlayer mediaPlayer;
    private Context context;

    public SoundManager(Context context) {
        this.context = context;
        mediaPlayer = new MediaPlayer();
    }

    // Phát âm thanh từ một ID âm thanh
    public void playSound(int soundResId) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

        mediaPlayer = MediaPlayer.create(context, soundResId);
        mediaPlayer.start();
    }

    // Phát âm thanh lặp lại từ một ID âm thanh
    public void playSoundLoop(int soundResId) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

        mediaPlayer = MediaPlayer.create(context, soundResId);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    // Tạm dừng phát âm thanh
    public void pauseSound() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    // Tiếp tục phát âm thanh sau khi đã tạm dừng
    public void resumeSound() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    // Dừng phát âm thanh và đặt lại MediaPlayer
    public void stopSound() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    private MediaPlayer.OnCompletionListener onCompletionListener;

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        this.onCompletionListener = listener;
        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(listener);
        }
    }

    // Phương thức này dùng để xóa sự kiện nghe sau khi sự kiện đã xảy ra
    public void removeOnCompletionListener() {
        this.onCompletionListener = null;
        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(null);
        }
    }
}
