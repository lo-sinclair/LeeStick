package xyz.losi.leestick.ui.addnote;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import xyz.losi.leestick.data.db.Note;
import xyz.losi.leestick.data.db.NoteDatabase;
import xyz.losi.leestick.data.db.NotesRepository;
import xyz.losi.leestick.model.NoteIconType;

public class AddEditNoteViewModel extends AndroidViewModel {
    //private NotesDao notesDao;
    private final NotesRepository notesRepository;
    private MutableLiveData<Boolean> shouldCloseScreen = new MutableLiveData<>();
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public AddEditNoteViewModel(@NonNull Application application, NotesRepository repository, NotesRepository notesRepository) {
        super(application);
        this.notesRepository = notesRepository;
    }

    public void saveNote(Note note) {
        Disposable disposable = notesRepository.addNote(note)
                .subscribeOn(Schedulers.io()) // в другом потоке
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        Log.d("AddNoteViewModel", "subscribe");
                        shouldCloseScreen.setValue(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d("AddNoteViewModel", "Error saveNote");
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void updateNote(Note note) {
        Disposable disposable = notesRepository.updateNote(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> shouldCloseScreen.setValue(true),
                        throwable -> Log.e("AddNoteViewModel", "Error updateNote", throwable));
        compositeDisposable.add(disposable);
    }

    public MutableLiveData<Boolean> getShouldCloseScreen() {
        return shouldCloseScreen;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
