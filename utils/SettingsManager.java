package xyz.losi.leestick.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import xyz.losi.leestick.R;
import xyz.losi.leestick.model.NoteColorType;
import xyz.losi.leestick.model.NoteIconType;

public class SettingsManager {
    private static SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setIconStyle(Context context, NoteIconType.IconStyle style) {
        getPrefs(context).edit().putString("note_icon_style", style.name()).apply();
    }

    public static NoteIconType.IconStyle getIconStyle(Context context) {
        String name = getPrefs(context).getString("note_icon_style", NoteIconType.IconStyle.CIRCLE.name());
        return NoteIconType.IconStyle.valueOf(name);
    }

    public static void setBackgroundColor(Context context, int colorResId) {
        getPrefs(context).edit().putInt("note_background_color", colorResId).apply();
    }

    public static int getBackgroundColor(Context context) {
        return getPrefs(context).getInt("note_background_color", R.color.note_yellow);
    }

    public static void setNoteColor(Context context, NoteColorType color) {
        getPrefs(context).edit().putString("note_color", color.name()).apply();
    }

    public static NoteColorType getNoteColor(Context context) {
        String name = getPrefs(context).getString("note_color", NoteIconType.IconColor.YELLOW.name());
        return NoteColorType.valueOf(name);
    }

    public static void setDefaultNoteIconColor(Context context, NoteIconType.IconColor color) {
        getPrefs(context).edit().putString("default_note_icon_color", color.name()).apply();
    }

    public static NoteIconType.IconColor getDefaultNoteIconColor(Context context) {
        String name = getPrefs(context).getString("default_note_icon_color", NoteIconType.IconColor.BLUE.name());
        return NoteIconType.IconColor.valueOf(name);
    }

    public void setShowOnLockscreen(Context context, boolean showOnLockscreen) {
        getPrefs(context).edit().putBoolean("show_on_lockscreen", showOnLockscreen).apply();
    }

    public static boolean getShowOnLockscreen(Context context) {
        return getPrefs(context).getBoolean("show_on_lockscreen", true);
    }

    public static void setFontSize(Context context, float size) {
        getPrefs(context).edit().putString("note_font_size", String.valueOf(size)).apply();
    }

    public static float getFontSize(Context context) {
        try {
            return Float.parseFloat(getPrefs(context).getString("note_font_size", "16.0"));
        } catch (NumberFormatException e) {
            return 16.0f; // fallback
        }
    }

    public static void setNotesQuantity(Context context, int quantity) {
        getPrefs(context).edit().putInt("notes_quantity", quantity).apply();
    }

    public static int getNotesQuantity(Context context) {
        return getPrefs(context).getInt("notes_quantity", 10);
    }


}

