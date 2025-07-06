package xyz.losi.leestick.model;

import android.content.Context;

import androidx.core.content.ContextCompat;

import xyz.losi.leestick.R;

public enum NoteColorType {
    RED(R.color.note_red),
    PINK(R.color.note_pink),
    ORANGE(R.color.note_orange),
    YELLOW(R.color.note_yellow, true),
    GREEN(R.color.note_green),
    BLUE(R.color.note_blue),
    PURPLE(R.color.note_purple),
    VIOLET(R.color.note_violet),
    GRAY(R.color.note_gray),
    WHITE(R.color.white);


    private final int colorResId;
    private final boolean defaultCheck;

    NoteColorType(int colorResId, boolean defaultCheck) {
        this.colorResId = colorResId;
        this.defaultCheck = defaultCheck;
    }

    NoteColorType(int colorResId) {
        this.colorResId = colorResId;
        this.defaultCheck = false;
    }


    public int getColor(Context context) {
        return ContextCompat.getColor(context, colorResId);
    }

    public int getColorResId() {
        return colorResId;
    }

    public boolean isDefaultCheck() {
        return defaultCheck;
    }
}


   /* public void initEmoji(Context context) {
        if (emoji == null) {
            emoji = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                    ? context.getString(emojiResId)
                    : context.getString(emojiLegacyResId);
        }
    }*/