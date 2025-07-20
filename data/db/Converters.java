package xyz.losi.leestick.data.db;

import androidx.room.TypeConverter;

import xyz.losi.leestick.model.NoteIconType;

public class Converters {
    @TypeConverter
    public static String fromIconColor(NoteIconType.IconColor value) {
        return value == null ? null : value.name();
    }

    @TypeConverter
    public static NoteIconType.IconColor toIconColor(String value) {
        return value == null ? null : NoteIconType.IconColor.valueOf(value);
    }

}

