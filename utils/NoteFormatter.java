package xyz.losi.leestick.utils;

import java.util.List;

import xyz.losi.leestick.data.db.Note;

public class NoteFormatter {
    public static String formatList(List<Note> notes) {
        StringBuilder stringBuilder = new StringBuilder();

        for(Note note : notes) {
            stringBuilder.append(note.getColor().getEmoji())
                    .append(" ")
                    .append(note.getText())
                    .append("\n");
        }
        return stringBuilder.toString().trim();
    }
}

