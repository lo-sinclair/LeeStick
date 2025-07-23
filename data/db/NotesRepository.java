package xyz.losi.leestick.data.db;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import xyz.losi.leestick.utils.NoteWeigher;

public class NotesRepository {
    private final NotesDao notesDao;
    
    public NotesRepository(NotesDao notesDao) {
        this.notesDao = notesDao;
    }

    /** Получение LiveData списка заметок (для отображения) */
    public LiveData<List<Note>> getAllNotesLiveData() {
        return notesDao.getNotes();
    }


    /** Получение списка заметок напрямую (не LiveData) */
    public List<Note> getAllNotesList() {
        return notesDao.getNotesList();
    }


    /** Добавление новой заметки с весом на основе max(weight) */
    public Completable addNote(Note note) {
        return Single.fromCallable(notesDao::getMaxWeight)
                .map(maxWeight -> maxWeight + 100f)
                .flatMapCompletable(newWeight -> {
                    note.setWeight(newWeight);
                    return notesDao.add(note);
                });
    }

    /** Добавление новой заметки без расчета веса для восстановления */
    public Completable restoreNote(Note note) {
        return notesDao.add(note);
    }


    /** Обновление заметки после редактирования */
    public Completable updateNote(Note note) {
        return notesDao.update(note);
    }


    /** Обработка перемещения и пересчёт веса */
    public Completable moveNote(List<Note> notes, int from, int to) {
        /*if (from < 0 || to < 0 || from >= notes.size() || to >= notes.size()) {
            return Completable.error(new IllegalArgumentException("Invalid move indices: from=" + from + ", to=" + to));
        }

        if (from == to) return Completable.complete();*/

        List<Note> reordered = NoteWeigher.reorderAndUpdateWeights(notes, from, to);

        return Completable.fromAction(() -> {
            for (Note note : reordered) {
                notesDao.add(note).blockingAwait();
            }
        }).subscribeOn(Schedulers.io());
    }

    /** Удаление заметки */
    public Completable removeNote(Note note) {
        return notesDao.remove(note.getId()).ignoreElement();
    }

    /** Удаление всех заметок */
    public Completable removeAllNotes() {
        return notesDao.removeAll();
    }

}
