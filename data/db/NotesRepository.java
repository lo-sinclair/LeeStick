package xyz.losi.leestick.data.db;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

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

    /** Обновление заметки после редактирования */
    public Completable updateNote(Note note) {
        return notesDao.update(note);
    }

    /** Обработка перемещения и пересчёт веса */
    public Completable moveNote(List<Note> notes, int from, int to) {
        if(from == to) return Completable.complete();

        Note movedNote = notes.get(from);
        notes.remove(from);
        notes.add(to, movedNote);

        float newWeight;
        if (to==0) {
            newWeight = notes.get(1).getWeight() - 100f;
        } else if (to == notes.size()-1) {
            newWeight = notes.get(notes.size() - 2).getWeight() + 100f;
        } else {
            float prev = notes.get(to-1).getWeight();
            float next = notes.get(to+1).getWeight();
            newWeight = (prev + next)/2f;
        }
        movedNote.setWeight(newWeight);

        // Если разница между соседними весами слишком мала — пересчитай все
        boolean needRebalance = false;
        for(int i = 1; i < notes.size(); i++) {
            float diff = notes.get(i).getWeight() - notes.get(i-1).getWeight();
            if(diff < 1) {
                needRebalance = true;
                break;
            }
        }

        if(needRebalance) {
            return Completable.fromAction(() -> {
                for(int i = 1; i < notes.size(); i++) {
                    notes.get(i).setWeight((i + 1) * 100f);
                    notesDao.add(notes.get(i)).blockingAwait();
                }
            }).subscribeOn(Schedulers.io());
        } else {
            return notesDao.add(movedNote)
                    .subscribeOn(Schedulers.io());
        }
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
