package xyz.losi.leestick.ui.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import xyz.losi.leestick.R;
import xyz.losi.leestick.model.NoteColorType;
import xyz.losi.leestick.ui.main.MainViewModel;
import xyz.losi.leestick.utils.Logger;
import xyz.losi.leestick.utils.ViewUtils;

public class ColorPreference extends Preference {

    public ColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.preference_color);
    }


    public void showColorPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Выберите цвет");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(20, 20, 20, 20);

        builder.setView(layout);
        builder.setNegativeButton("Отмена", null);

        AlertDialog dialog = builder.create();

        for(NoteColorType color : NoteColorType.values()) {
            View colorView = new View(getContext());
            colorView.setBackgroundColor(color.getColor(getContext()));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewUtils.dpToPx(getContext(), 30),
                    ViewUtils.dpToPx(getContext(), 30)
            );
            params.setMargins(ViewUtils.dpToPx(
                    getContext(), 4), 0,
                    ViewUtils.dpToPx(getContext(), 4), 0
            );
            colorView.setLayoutParams(params);
            colorView.setOnClickListener(v -> {
                persistString(color.name());

                Context context = getContext();
                if (context instanceof ViewModelStoreOwner) {
                    ViewModelProvider provider = new ViewModelProvider((ViewModelStoreOwner) context);
                    MainViewModel viewModel = provider.get(MainViewModel.class);
                    viewModel.updateNotificationAppearance(context);
                }

                notifyChanged();
                dialog.dismiss();
            });
            layout.addView(colorView);
        }

        dialog.show();
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        View colorPreview = holder.findViewById(R.id.colorPreview);
        if (colorPreview != null) {
            String colorName = getPersistedString(NoteColorType.YELLOW.name());
            NoteColorType color = NoteColorType.valueOf(colorName);
            colorPreview.setBackgroundColor(color.getColor(getContext()));
        }
    }

    @Override
    protected void onClick() {
        showColorPickerDialog();
    }
}
