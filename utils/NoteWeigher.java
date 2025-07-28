package xyz.losi.leestick.utils;

import androidx.preference.ListPreference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xyz.losi.leestick.data.db.Note;

public class NoteWeigher {

    private static final float DEFAULT_STEP = 100f;
    private static final float MIN_GAP = 1f; // минимально допустимая разница между соседними весами

    public static float calculateWeight(List<Note> notes, int toPosition) {
        if (toPosition < 0 || toPosition >= notes.size()) return Float.NaN;

        // Примерное поведение вставки
        float newWeight;
        if (toPosition == 0) {
            newWeight = notes.get(0).getWeight() - DEFAULT_STEP;
        } else if (toPosition == notes.size() - 1) {
            newWeight = notes.get(notes.size() - 1).getWeight() + DEFAULT_STEP;
        } else {
            float before = notes.get(toPosition - 1).getWeight();
            float after = notes.get(toPosition).getWeight();

            // Если веса слишком близки — возвращаем NaN, чтобы вызвать пересчёт
            if (Math.abs(after - before) < MIN_GAP) {
                return Float.NaN;
            }

            newWeight = (before + after) / 2f;
        }

        return newWeight;
    }

    public static void reweightAll(List<Note> notes) {
        for (int i = 0; i < notes.size(); i++) {
            notes.get(i).setWeight(i * DEFAULT_STEP);
        }
    }
}
