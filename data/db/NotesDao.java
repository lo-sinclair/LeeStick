package xyz.losi.leestick.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import xyz.losi.leestick.data.db.Note;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM notes ORDER BY weight ASC")
    LiveData<List<Note>> getNotes();

    @Query("SELECT * FROM notes ORDER BY weight ASC")
    List<Note> getNotesList();

    @Query("SELECT MAX(weight) FROM notes")
    float getMaxWeight();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable add(Note note);

    @Update
    Completable update(Note note);

    @Query("DELETE FROM notes WHERE id = :id")
    Single<Integer> remove(int id);

    @Query("DELETE FROM notes")
    Completable removeAll();
}
