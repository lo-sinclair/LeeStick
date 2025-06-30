package xyz.losi.leestick.data.db;

import androidx.room.TypeConverter;

import xyz.losi.leestick.model.NoteIconType;

public class Converters {
    @TypeConverter
    public static String fromIconColor(NoteIconType.IconColor color) {
        return color == null ? null : color.name();
    }

    @TypeConverter
    public static NoteIconType.IconColor toIconColor(String name) {
        return name == null ? null : NoteIconType.IconColor.valueOf(name);
    }

}

