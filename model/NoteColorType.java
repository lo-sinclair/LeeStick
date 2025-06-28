package xyz.losi.leestick.model;

import android.content.Context;
import android.os.Build;

import androidx.core.content.ContextCompat;

import xyz.losi.leestick.R;

public enum NoteColorType {
    RED(R.color.red, R.string.red_icon, R.string.red_alt_icon),
//    ORANGE(R.color.orange, R.string.orange_icon, R.string.orange_alt_icon),
    YELLOW(R.color.yellow, R.string.yellow_icon, R.string.yellow_alt_icon),
    GREEN(R.color.green, R.string.green_icon, R.string.green_alt_icon, true),
    BLUE(R.color.blue, R.string.blue_icon, R.string.blue_alt_icon),
    PURPLE(R.color.purple, R.string.purple_icon, R.string.purple_alt_icon),
    BLACK(R.color.black, R.string.black_icon, R.string.black_alt_icon);
//    WHITE(R.color.white, R.string.white_icon, R.string.white_alt_icon);


    private final int colorResId;
    private final int emojiResId;
    private final int emojiLegacyResId;
    private final boolean defaultCheck;
    private String emoji = null;


    NoteColorType(int colorResId, int emojiResId, int emojiLegacyResId, boolean defaultCheck) {
        this.colorResId = colorResId;
        this.emojiResId = emojiResId;
        this.emojiLegacyResId = emojiLegacyResId;
        this.defaultCheck = defaultCheck;
    }

    NoteColorType(int colorResId, int emojiResId, int emojiLegacyResId) {
        this.colorResId = colorResId;
        this.emojiResId = emojiResId;
        this.emojiLegacyResId = emojiLegacyResId;
        this.defaultCheck = false;
    }

    public void initEmoji(Context context) {
        if (emoji == null) {
            emoji = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                    ? context.getString(emojiResId)
                    : context.getString(emojiLegacyResId);
        }
    }

    public int getColor(Context context) {
        return ContextCompat.getColor(context, colorResId);
    }

    public String getEmoji() {
        return emoji != null ? emoji : "⬜";
    }

    public int getColorResId() {
        return colorResId;
    }

    public int getEmojiResId() {
        return emojiResId;
    }

    public int getEmojiLegacyResId() {
        return emojiLegacyResId;
    }

    public boolean isDefaultCheck() {
        return defaultCheck;
    }
}
