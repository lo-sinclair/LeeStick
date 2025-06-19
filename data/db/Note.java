package xyz.losi.leestick.data.db;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import xyz.losi.leestick.model.NoteColorType;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String text;
    private NoteColorType color;

    public Note(int id, String text, NoteColorType color) {
        this.id = id;
        this.text = text;
        this.color = color;
    }

    @Ignore
    public Note(String text, NoteColorType color) {
        this.text = text;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public NoteColorType getColor() {
        return color;
    }

    public void setColor(NoteColorType color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", color=" + color +
                '}';
    }
}
