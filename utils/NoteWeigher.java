package xyz.losi.leestick.utils;

import androidx.preference.ListPreference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xyz.losi.leestick.data.db.Note;

public class NoteWeigher {

    public static float calculateWeightBetween(float prev, float next) {
        return (prev + next) / 2f;
    }

    public static float calculateWeight(Note previousNote, Note nextNote) {
        if (previousNote == null && nextNote == null) {
            return 100;
        } else if (previousNote == null) {
            return nextNote.getWeight() - 100;
        } else if (nextNote == null) {
            return previousNote.getWeight() + 100;
        } else {
            float prev = previousNote.getWeight();
            float next = nextNote.getWeight();
            if (next - prev <= 1) {
                // Нужно пересчитать все веса
                return -1;
            }
            return (prev + next) / 2f;
        }
    }

    public static List<Note> reorderAndUpdateWeights(List<Note> origNotes, int from, int to){
        if (from < 0 || to < 0 || from >= origNotes.size() || to >= origNotes.size() || from == to) {
            return origNotes;
        }

        List<Note> updated = new ArrayList<>(origNotes);
        Note moved = updated.remove(from);
        updated.add(to, moved);

        Note movedCopy = new Note(moved);

        float newWeight;
        if (to == 0) {
            newWeight = updated.get(1).getWeight() - 100f;
        } else if (to == updated.size() - 1) {
            newWeight = updated.get(updated.size() - 2).getWeight() + 100f;
        } else {
            float prev = updated.get(to - 1).getWeight();
            float next = updated.get(to + 1).getWeight();
            newWeight = calculateWeightBetween(prev, next);
        }
        movedCopy.setWeight(newWeight);
        updated.set(to, movedCopy);

        if (needRebalance(updated, 1f)) {
            return rebalance(updated);
        }

        return updated;
    }

    public static boolean needRebalance(List<Note> notes, float delta) {
        for(int i = 1; i < notes.size(); i++) {
            float diff = notes.get(i).getWeight() - notes.get(i-1).getWeight();
            //Если разница между соседними весами слишком мала
            if(diff < delta) {
                return true;
            }
        }
        return false;
    }



    public static List<Note> rebalance(List<Note> notes) {
        List<Note> rebalanced = new ArrayList<>();
        for (int i = 0; i < notes.size(); i++) {
            Note newNote = new Note(notes.get(i));
            newNote.setWeight((i + 1) * 100f);
            rebalanced.add(newNote);
        }
        return rebalanced;
    }
}
