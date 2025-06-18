package xyz.losi.leestick;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AddNoteActivity extends AppCompatActivity {

    private AddNoteViewModel viewModel;

    private EditText editTextNote;
    private RadioGroup radioGroupColorNote;
    private Button buttonSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        viewModel = new ViewModelProvider(this).get(AddNoteViewModel.class);

        viewModel.getShouldCloseScreen().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean shouldClose) {
                if (shouldClose) {
                    finish();
                }

            }
        });

        initViews();
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
    }

    private void initViews() {
        editTextNote = findViewById(R.id.editTextNote);

        radioGroupColorNote = findViewById(R.id.radioGroupColorNote);
        buttonSave = findViewById(R.id.buttonSave);

        for (NoteColorType color : NoteColorType.values()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(color.getEmoji(this));
            radioButton.setTag(color.name());
            radioGroupColorNote.addView(radioButton);
            if(color.isDefaultCheck()) {
                radioGroupColorNote.check(radioButton.getId());
            }
        }
    }

    private void saveNote(){

        String text = editTextNote.getText().toString().trim();

        RadioButton selectedRadio = findViewById(radioGroupColorNote.getCheckedRadioButtonId());
        NoteColorType color = NoteColorType.valueOf(selectedRadio.getTag().toString());

        Note note = new Note(0, text, color);

        viewModel.saveNote(note);

    }

    public static Intent newIntent(Context context){
        return new Intent(context, AddNoteActivity.class);
    }
}