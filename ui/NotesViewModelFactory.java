package xyz.losi.leestick.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import xyz.losi.leestick.data.db.NoteDatabase;
import xyz.losi.leestick.data.db.NotesDao;
import xyz.losi.leestick.data.db.NotesRepository;
import xyz.losi.leestick.ui.addnote.AddEditNoteViewModel;
import xyz.losi.leestick.ui.main.MainViewModel;

public class NotesViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public NotesViewModelFactory(Application application) {
        this.application = application;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        NotesDao dao = NoteDatabase.getInstance(application).notesDao();
        NotesRepository repository = new NotesRepository(dao);

        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(application, repository);
        } else if (modelClass.isAssignableFrom(AddEditNoteViewModel.class)) {
            return (T) new AddEditNoteViewModel(application, repository, repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
