package xyz.losi.leestick.ui.main;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import xyz.losi.leestick.data.db.Note;
import xyz.losi.leestick.data.db.NoteDatabase;
import xyz.losi.leestick.model.NoteIconType;
import xyz.losi.leestick.notification.NotificationHelper;
import xyz.losi.leestick.utils.SettingsManager;

public class MainViewModel extends AndroidViewModel {

    private NoteDatabase noteDatabase;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainViewModel(@NonNull Application application) {
        super(application);
        noteDatabase = NoteDatabase.getInstance(application);
        insertInitialNotesIfFirstRun(application);
    }

    public LiveData<List<Note>> getNotes() {
        return noteDatabase.notesDao().getNotes();
    }

    public void remove(Note note) {
        Disposable disposable = noteDatabase.notesDao().remove(note.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer rowsDeleted) {
                        Log.d("MainViewModel", "Removed rows: " + rowsDeleted);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d("MainViewModel", "Error refreshList");
                    }
                });

        compositeDisposable.add(disposable);
     }

     public void removeAll() {
        noteDatabase.notesDao().removeAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("MainViewModel", "Все заметки удалены"),
                        throwable -> Log.e("MainViewModel", "Ошибка при удалении заметок", throwable)
                );
     }

    public void updateNotificationEnabled(Context context, boolean enabled) {
        if (enabled) {
            getNotes().observeForever(new Observer<List<Note>>() {
                @Override
                public void onChanged(List<Note> notes) {
                    NotificationHelper.buildNotification(context.getApplicationContext(), notes, Notification.VISIBILITY_PUBLIC);
                    getNotes().removeObserver(this);
                }
            });
        } else {
            NotificationHelper.cancelNotification(context.getApplicationContext());
        }
    }

    public void updateNotificationAppearance(Context context) {
        getNotes().observeForever(new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                boolean showOnLockScreen = SettingsManager.getShowOnLockscreen(context);
                if (!showOnLockScreen) {
                    NotificationHelper.cancelNotification(context.getApplicationContext());
                } else {
                    // Всегда показываем с VISIBILITY_PUBLIC, если нужно
                    NotificationHelper.buildNotification(context.getApplicationContext(), notes, Notification.VISIBILITY_PUBLIC);
                }
                getNotes().removeObserver(this);
            }
        });
    }

    @SuppressLint("CommitPrefEdits")
    public void insertInitialNotesIfFirstRun(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        //prefs.edit().putBoolean("is_first_run", true).apply();
        boolean isFirstRun = prefs.getBoolean("is_first_run", true);

        if (isFirstRun) {
            List<Note> defaultNotes = Arrays.asList(
                    new Note("Выбрать шторы", NoteIconType.IconColor.BLUE, 100f),
                    new Note(3, "Помыть посуду", NoteIconType.IconColor.GREEN, 200f),
                    new Note(4, "Учить JavaScript", NoteIconType.IconColor.RED, 300f),
                    new Note(5, "Базарить с лосём", NoteIconType.IconColor.YELLOW, 400f)
            );
            Observable.fromIterable(defaultNotes)
                    .flatMapCompletable(note -> noteDatabase.notesDao().add(note))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> {
                                prefs.edit().putBoolean("is_first_run", false).apply();
                            },
                            throwable -> {
                                Log.e("MainViewModel", "Ошибка при добавлении стартовых заметок", throwable);
                            });
        }
    }

    public void onNoteMoved(List<Note> notes, int from, int to) {
        if(from == to) return;

        Note movedNote = notes.get(to);
        float newWeight;
        if (to == 0) {
            newWeight = notes.get(1).getWeight() - 100;
        } else if (to == notes.size() - 1) {
            newWeight = notes.get(notes.size() - 2).getWeight() + 100;
        } else {
            float prev = notes.get(to - 1).getWeight();
            float next = notes.get(to + 1).getWeight();
            newWeight = (prev + next) / 2f;
        }

        movedNote.setWeight(newWeight);

        Disposable disposable = noteDatabase.notesDao().add(movedNote)
                .subscribeOn(Schedulers.io())
                .subscribe();

        compositeDisposable.add(disposable);

    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }


}
