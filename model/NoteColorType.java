package xyz.losi.leestick.model;

import android.content.Context;

import androidx.core.content.ContextCompat;

import xyz.losi.leestick.R;

public enum NoteColorType {
    YELLOW(R.color.yellow, R.string.yellow_icon),
    RED(R.color.red, R.string.red_icon),
    BLUE(R.color.blue, R.string.blue_icon),
    GREEN(R.color.green, R.string.green_icon, true);


    private final int colorResId;
    private final int emojiResId;
    private final boolean defaultCheck;


    NoteColorType(int colorResId, int emojiResId, boolean defaultCheck) {
        this.colorResId = colorResId;
        this.emojiResId = emojiResId;
        this.defaultCheck = defaultCheck;
    }

    NoteColorType(int colorResId, int emojiResId) {
        this.colorResId = colorResId;
        this.emojiResId = emojiResId;
        this.defaultCheck = false;
    }

    public int getColor(Context context) {
        return ContextCompat.getColor(context, colorResId);
    }

    public String getEmoji(Context context) {
        return context.getString(emojiResId);
    }

    public int getColorResId() {
        return colorResId;
    }

    public int getEmojiResId() {
        return emojiResId;
    }

    public boolean isDefaultCheck() {
        return defaultCheck;
    }
}
