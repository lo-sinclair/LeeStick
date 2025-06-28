package xyz.losi.leestick.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.TooltipCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import xyz.losi.leestick.R;
import xyz.losi.leestick.data.db.Note;
import xyz.losi.leestick.model.NoteColorType;
import xyz.losi.leestick.notification.NotificationHelper;
import xyz.losi.leestick.notification.NotificationService;
import xyz.losi.leestick.ui.addnote.AddNoteActivity;
import xyz.losi.leestick.utils.NoteFormatter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotes;
    private FloatingActionButton buttonAddNote;
    private NotesAdapter notesAdapter;

    private MainViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (NoteColorType color : NoteColorType.values()) {
            color.initEmoji(getApplicationContext());
        }

        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        initViews();

        notesAdapter = new NotesAdapter();

        recyclerViewNotes.setAdapter(notesAdapter);
        //recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));

        viewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                notesAdapter.setNotes(notes);

                Intent serviceIntent = new Intent(MainActivity.this, NotificationService.class);
                serviceIntent.putExtra("title", "Мои заметки");
                serviceIntent.putExtra("text", NoteFormatter.formatList(notes));

                NotificationHelper.createNotificationChannel(MainActivity.this);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // API 26+ (Android 8+)
                    startForegroundService(serviceIntent);
                } else {
                    // API 25- (Android 7 и ниже)
                    startService(serviceIntent);
                }
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                 int direction) {
                int position = viewHolder.getAdapterPosition();
                Note note = notesAdapter.getNotes().get(position);

                viewModel.remove(note);
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
        MenuItem switchItem = menu.findItem(R.id.menu_lock_switch);

        View actionView = switchItem.getActionView();
        if (actionView == null) {
            Log.e("MENU", "actionView is null!");
            return true;
        }

        SwitchCompat switchCompat = actionView.findViewById(R.id.switchLockScreen);
        if (switchCompat == null) {
            Log.e("MENU", "switchCompat is null!");
            return true;
        }

        // Всплывающая подсказка (только для Android 8.0+ и выше)
        TooltipCompat.setTooltipText(switchCompat, getString(R.string.hint_lock_screen));

        // Установка значения по умолчанию
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean showOnLockScreen = prefs.getBoolean("show_on_lock_screen", true);
        switchCompat.setChecked(showOnLockScreen);

        // Обработка нажатий
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("show_on_lock_screen", isChecked);
            editor.apply();
            Toast.makeText(this, "Показывать на экране блокировки: " + isChecked, Toast.LENGTH_SHORT).show();
        });

        return true;
    }

}