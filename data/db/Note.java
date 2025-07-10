package xyz.losi.leestick.data.db;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import xyz.losi.leestick.model.NoteIconType;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String text;
    private NoteIconType.IconColor iconColor;
    private float weight;

    public Note(int id, String text, NoteIconType.IconColor iconColor, float weight) {
        this.id = id;
        this.text = text;
        this.iconColor = iconColor;
        this.weight = weight;
    }

    @Ignore
    public Note(String text, NoteIconType.IconColor color, float weight) {
        this.text = text;
        this.iconColor = color;
        this.weight = weight;
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

    public NoteIconType.IconColor getIconColor() {
        return iconColor;
    }

    public void setIconColor(NoteIconType.IconColor iconColor) {
        this.iconColor = iconColor;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", iconColor=" + iconColor +
                '}';
    }
}
