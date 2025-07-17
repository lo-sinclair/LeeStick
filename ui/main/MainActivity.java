package xyz.losi.leestick.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import io.reactivex.rxjava3.annotations.Nullable;
import xyz.losi.leestick.R;
import xyz.losi.leestick.data.db.Note;
import xyz.losi.leestick.notification.NotificationHelper;
import xyz.losi.leestick.ui.NotesViewModelFactory;
import xyz.losi.leestick.ui.addnote.AddNoteActivity;
import xyz.losi.leestick.ui.settings.SettingsActivity;
import xyz.losi.leestick.utils.SettingsManager;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotes;
    private FloatingActionButton buttonAddNote;
    private NotesAdapter notesAdapter;

    private MainViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Создание канала (один раз, можно вынести в onCreate)
        //NotificationManagerCompat.from(this).cancel(1);
        NotificationHelper.createNotificationChannel(MainActivity.this);

        /*for (NoteColorType color : NoteColorType.values()) {
            color.initEmoji(getApplicationContext());
        }*/

        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(
                this,
                new NotesViewModelFactory(getApplication())
        ).get(MainViewModel.class);

        initViews();

        notesAdapter = new NotesAdapter();

        recyclerViewNotes.setAdapter(notesAdapter);
        //recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));

        viewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                notesAdapter.setNotes(notes);

                // Показ кастомного уведомления
                if (SettingsManager.getShowOnLockscreen(MainActivity.this)) {
                    NotificationHelper.buildNotification(MainActivity.this, notes, Notification.VISIBILITY_PUBLIC);
                } else {
                    NotificationHelper.buildNotification(MainActivity.this, notes, Notification.VISIBILITY_SECRET);
                }
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.RIGHT
         ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                notesAdapter.swapItems(from, to);
                viewModel.onNoteMoved(notesAdapter.getNotes(), from, to);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                 int direction) {
                int position = viewHolder.getAdapterPosition();
                Note note = notesAdapter.getNotes().get(position);

                viewModel.remove(note);
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder instanceof NotesAdapter.ItemTouchHelperViewHolder) {
                    ((NotesAdapter.ItemTouchHelperViewHolder) viewHolder).onItemSelected();
                }
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                if (viewHolder instanceof NotesAdapter.ItemTouchHelperViewHolder) {
                    ((NotesAdapter.ItemTouchHelperViewHolder) viewHolder).onItemClear();
                }
            }
        });

        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddNoteActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerViewNotes);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initViews() {
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        buttonAddNote = findViewById(R.id.buttonAddNote);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        if(item.getItemId() == R.id.action_delete_all) {
            showDeleteAllConfirmationDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteAllConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Удалить все заметки?")
                .setMessage("Это действие нельзя отменить. Вы уверены?")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    viewModel.removeAll();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

}