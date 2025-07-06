package xyz.losi.leestick.ui.settings;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;

import xyz.losi.leestick.R;
import xyz.losi.leestick.model.NoteIconType;
import xyz.losi.leestick.notification.NotificationHelper;
import xyz.losi.leestick.ui.main.MainViewModel;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.empty_preferences, rootKey);

        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(requireContext());
        setPreferenceScreen(screen);

        // 1. Показывать на экране блокировки
        SwitchPreferenceCompat showOnLockScreenPref = new SwitchPreferenceCompat(requireContext());
        showOnLockScreenPref.setKey("show_notification");
        showOnLockScreenPref.setTitle("Показывать на экране блокировки");
        showOnLockScreenPref.setSummary("Показывать листик на экране блокировки");
        showOnLockScreenPref.setDefaultValue(true);

        screen.addPreference(showOnLockScreenPref);

        showOnLockScreenPref.setOnPreferenceChangeListener(((preference, newValue) -> {
            boolean enabled = (Boolean) newValue;

            MainViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
            viewModel.updateNotificationEnabled(requireContext(), enabled);
            return true;
        }));

        // 2. Тип маркеров
        ListPreference iconTypePref = new ListPreference(requireContext());
        iconTypePref.setKey("note_icon_style");
        iconTypePref.setTitle("Тип маркеров");
        iconTypePref.setSummary("Выберите стиль иконок");

        NoteIconType.IconStyle[] values = NoteIconType.IconStyle.values();
        String[] entries = new String[values.length];
        String[] entryValues = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            entries[i] = values[i].getName();
            entryValues[i] = values[i].name();
        }

        iconTypePref.setEntries(entries);
        iconTypePref.setEntryValues(entryValues);
        iconTypePref.setDefaultValue(NoteIconType.IconStyle.SQUARE.name());

        screen.addPreference(iconTypePref);

        iconTypePref.setOnPreferenceChangeListener(((preference, newValue) -> {
            String newIconStyleName = (String) newValue;

            MainViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
            viewModel.updateNotificationAppearance(requireContext());
            return true;
        }));

        // 3. Цвет стикера
        ColorPreference colorPref = new ColorPreference(
                requireContext(),
                null
        );
        colorPref.setKey("note_color");
        colorPref.setTitle("Цвет фона");
        colorPref.setSummary("Выберите цвет стикера");
        screen.addPreference(colorPref);
    }
}





















