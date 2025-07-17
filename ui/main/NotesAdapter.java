package xyz.losi.leestick.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xyz.losi.leestick.R;
import xyz.losi.leestick.data.db.Note;
import xyz.losi.leestick.model.NoteColorType;
import xyz.losi.leestick.utils.SettingsManager;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private List<Note> notes = new ArrayList<>();

    private onNoteClickListener onNoteClickListener;

    public void setOnNoteClickListener(NotesAdapter.onNoteClickListener onNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public List<Note> getNotes() {
        return new ArrayList<Note>(notes);
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.note_item,
                parent,
                false);


        return new NotesViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(NotesViewHolder viewHolder, int position) {
        Note note = notes.get(position);
        Context context = viewHolder.itemView.getContext();
        viewHolder.textViewNote.setText(note.getText());
        //viewHolder.textViewNote.setBackgroundColor(SettingsManager.getNoteColor(context).getColor(context));
        viewHolder.textViewNote.setBackgroundColor(NoteColorType.YELLOW.getColor(context));

        viewHolder.colorViewNote.setBackgroundColor(note.getIconColor().getColor(context));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onNoteClickListener != null) {
                    onNoteClickListener.onNoteClick(note);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void swapItems(int from, int to) {
        if(from < 0 || to < 0 || from >= notes.size() || to >= notes.size()) {
            return;
        }
        Collections.swap(notes, from, to);
        notifyItemChanged(from, to);
    }


    class NotesViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{
        private TextView textViewNote;
        private FrameLayout colorViewNote;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNote = itemView.findViewById(R.id.textViewNote);
            colorViewNote = itemView.findViewById(R.id.colorViewNote);
        }

        @Override
        public void onItemSelected() {
            itemView.setAlpha(0.7f);
            itemView.setScaleX(1.03f);
            itemView.setScaleY(1.03f);
        }

        @Override
        public void onItemClear() {
            itemView.setAlpha(1.0f);
            itemView.setScaleX(1.0f);
            itemView.setScaleY(1.0f);
        }
    }

    interface onNoteClickListener {
        void onNoteClick(Note note);
    }

    interface ItemTouchHelperViewHolder {
        void onItemSelected(); // когда перетаскиваем
        void onItemClear();    // когда отпущено
    }
}
