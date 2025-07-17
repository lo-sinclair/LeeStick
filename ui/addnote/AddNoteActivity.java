package xyz.losi.leestick.ui.addnote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import xyz.losi.leestick.R;
import xyz.losi.leestick.data.db.Note;
import xyz.losi.leestick.model.NoteIconType;
import xyz.losi.leestick.ui.NotesViewModelFactory;
import xyz.losi.leestick.ui.main.MainViewModel;
import xyz.losi.leestick.utils.Logger;
import xyz.losi.leestick.utils.SettingsManager;
import xyz.losi.leestick.utils.ViewUtils;

public class AddNoteActivity extends AppCompatActivity {

    private AddNoteViewModel viewModel;

    private EditText editTextNote;
    private Button buttonSave;
    private LinearLayout colorContainer;
    private NoteIconType.IconColor selectedColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        viewModel = new ViewModelProvider(
                this,
                new NotesViewModelFactory(getApplication())
        ).get(AddNoteViewModel.class);

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
        colorContainer = findViewById(R.id.colorContainer);
        buttonSave = findViewById(R.id.buttonSave);

        for (NoteIconType.IconColor color : NoteIconType.IconColor.values()) {
            FrameLayout frame = new FrameLayout(this);
            LinearLayout.LayoutParams frameParams = new LinearLayout.LayoutParams(
                    ViewUtils.dpToPx(this, 30), ViewUtils.dpToPx(this, 30));
            frameParams.setMargins(ViewUtils.dpToPx(this, 4), 0, ViewUtils.dpToPx(this, 4), 0);
            frame.setLayoutParams(frameParams);


            View colorSquare = new View(this);
            FrameLayout.LayoutParams squareParams = new FrameLayout.LayoutParams(
                    //FrameLayout.LayoutParams.MATCH_PARENT,
                    ViewUtils.dpToPx(this, 22),
                    ViewUtils.dpToPx(this, 22),
                    Gravity.CENTER
            );

            colorSquare.setLayoutParams(squareParams);
            colorSquare.setBackgroundColor(color.getColor(this));
            colorSquare.setTag(color);

            frame.addView(colorSquare);
            frame.setTag(R.id.color_frame_tag, frame);
            frame.setTag(R.id.color_square_tag, colorSquare);

            NoteIconType.IconColor defaultNoteIconColor = SettingsManager.getDefaultNoteIconColor(this);
            if(color == defaultNoteIconColor) {
                selectedColor = color;
                frame.setBackgroundResource(R.drawable.selected_square_border);
            } else {
                frame.setBackground(null);
            }


            frame.setOnClickListener(v -> {
                resetSelection();
                v.setBackgroundResource(R.drawable.selected_square_border);
                selectedColor = (NoteIconType.IconColor) colorSquare.getTag();
                SettingsManager.setDefaultNoteIconColor(this, selectedColor);
            });

            colorContainer.addView(frame);
        }
    }

    private void resetSelection() {
        for (int i = 0; i < colorContainer.getChildCount(); i++) {
            View child = colorContainer.getChildAt(i); // FrameLayout
            child.setBackground(null); // убираем рамку
        }
    }

    private void saveNote(){

        String text = editTextNote.getText().toString().trim();
        viewModel.saveNote(new Note(text, selectedColor, 0f));

    }

    public static Intent newIntent(Context context){
        return new Intent(context, AddNoteActivity.class);
    }
}