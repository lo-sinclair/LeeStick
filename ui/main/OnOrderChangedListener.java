package xyz.losi.leestick.ui.main;

import java.util.List;

import xyz.losi.leestick.data.db.Note;

public interface OnOrderChangedListener {
    void onOrderChanged(Note movedNote, int toPosition);
}
