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
import xyz.losi.leestick.data.db.NotesDao;
import xyz.losi.leestick.data.db.NotesRepository;
import xyz.losi.leestick.model.NoteIconType;
import xyz.losi.leestick.notification.NotificationHelper;
import xyz.losi.leestick.utils.SettingsManager;

public class MainViewModel extends AndroidViewModel {

    private final NotesRepository notesRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainViewModel(@NonNull Application application, NotesRepository repository) {
        super(application);
        NotesDao dao =NoteDatabase.getInstance(application).notesDao();
        notesRepository = new NotesRepository(dao);
        insertInitialNotesIfFirstRun(application);
    }

    public LiveData<List<Note>> getNotes() {
        return notesRepository.getAllNotesLiveData();
    }

    public void remove(Note note) {

        Disposable disposable = notesRepository.removeNote(note)
        .subscribe(() -> {}, throwable -> Log.e("MainViewModel", "Ошибка удаления", throwable));

        compositeDisposable.add(disposable);
     }

     public void removeAll() {
         Disposable disposable = notesRepository.removeAllNotes()
                 .subscribe(() -> {}, throwable -> Log.e("MainViewModel", "Ошибка удаления", throwable));
         compositeDisposable.add(disposable);
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
                    new Note("Помыть посуду", NoteIconType.IconColor.GREEN, 200f),
                    new Note("Учить JavaScript", NoteIconType.IconColor.RED, 300f),
                    new Note("Базарить с лосём", NoteIconType.IconColor.YELLOW, 400f)
            );
            Disposable disposable = Observable.fromIterable(defaultNotes)
                    .flatMapCompletable(notesRepository::addNote)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> {
                                prefs.edit().putBoolean("is_first_run", false).apply();
                            },
                            throwable -> {
                                Log.e("MainViewModel", "Ошибка при добавлении стартовых заметок", throwable);
                            });
            compositeDisposable.add(disposable);
        }
    }

    public void onNoteMoved(List<Note> notes, int from, int to) {
        Disposable disposable = notesRepository.moveNote(notes, from, to)
                .subscribe(
                        () -> {},
                        throwable -> Log.e("MainViewModel", "Ошибка при перемещении", throwable)
                );

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }


}
