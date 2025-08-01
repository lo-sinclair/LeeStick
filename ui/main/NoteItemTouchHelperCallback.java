package xyz.losi.leestick.ui.main;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

import xyz.losi.leestick.data.db.Note;
import xyz.losi.leestick.ui.main.ItemTouchHelperViewHolder;
import xyz.losi.leestick.ui.main.NotesAdapter;
import xyz.losi.leestick.ui.main.OnOrderChangedListener;

public class NoteItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final NotesAdapter adapter;
    private final Consumer<Note> onNoteSwiped;
    private final OnOrderChangedListener onOrderChangedListener;

    public NoteItemTouchHelperCallback(NotesAdapter adapter, Consumer<Note> onNoteSwiped, OnOrderChangedListener onOrderChangedListener) {
        this.adapter = adapter;
        this.onNoteSwiped = onNoteSwiped;
        this.onOrderChangedListener = onOrderChangedListener;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder source,
                          @NonNull RecyclerView.ViewHolder target) {
        return adapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        Note note = adapter.getNotes().get(position);
        onNoteSwiped.accept(note);
        //adapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                ((ItemTouchHelperViewHolder) viewHolder).onItemSelected();
            }
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);


        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            ((ItemTouchHelperViewHolder) viewHolder).onItemClear();
        }

            NotesAdapter notesAdapter = (NotesAdapter) adapter;

            Note movedNote = notesAdapter.getNoteAt(viewHolder.getAdapterPosition());
            int toPosition = viewHolder.getAdapterPosition();

            // Вызовать метод ViewModel
            if (onOrderChangedListener != null) {
                onOrderChangedListener.onOrderChanged(movedNote, toPosition);
            }



        /*if (viewHolder instanceof ItemTouchHelperViewHolder) {
            ((ItemTouchHelperViewHolder) viewHolder).onItemClear();
        }*/
    }
}
