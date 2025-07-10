package xyz.losi.leestick.data.db;

import java.util.ArrayList;
import java.util.List;

import xyz.losi.leestick.model.NoteColorType;
import xyz.losi.leestick.model.NoteIconType;

public class Database {

    private final List<Note> notes = new ArrayList<>();

    private static Database instance;

    private Database() {
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void add(Note note) {
        notes.add(note);
    }

    public void remove(Note note) {
        notes.remove(note);
    }

    public List<Note> getNotes() {
        return notes;
    }
}
