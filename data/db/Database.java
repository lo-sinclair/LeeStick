package xyz.losi.leestick.data.db;

import java.util.ArrayList;
import java.util.List;

import xyz.losi.leestick.model.NoteColorType;
import xyz.losi.leestick.model.NoteIconType;

public class Database {

    private final List<Note> notes = new ArrayList<>();

    private static Database instance;

    private Database() {

        notes.add(new Note(1, "Покормить кота", NoteIconType.IconColor.GREEN));
        notes.add(new Note(2, "Выбрать шторы", NoteIconType.IconColor.BLUE));
        notes.add(new Note(3, "Помыть посуду", NoteIconType.IconColor.GREEN));
        notes.add(new Note(4, "Учить JavaScript", NoteIconType.IconColor.RED));
        notes.add(new Note(5, "Базарить с лосём", NoteIconType.IconColor.YELLOW));

        /*for (int i = 0; i <= 6; i++) {
            Random random = new Random();
            Note note = notes.get(random.nextInt(4));
            note.setId(i+1);
            notes.add(note);
        }*/
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
