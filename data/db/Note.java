package xyz.losi.leestick.data.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import xyz.losi.leestick.model.NoteIconType;

@Entity(tableName = "notes")
public class Note implements Cloneable, Parcelable{
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

    public Note(Parcel in) {
        id = in.readInt();
        text = in.readString();
        String colorName = in.readString();
        iconColor = colorName != null ? NoteIconType.IconColor.valueOf(colorName) : null;
        weight = in.readFloat();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public Note(Note note) {
        this(note.getId(), note.getText(), note.getIconColor(), note.getWeight());
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(text);
        dest.writeString(iconColor != null ? iconColor.name() : null);
        dest.writeFloat(weight);
    }
}
