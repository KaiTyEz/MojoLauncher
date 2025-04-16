package net.kdt.pojavlaunch.prefs.screens;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.prefs.LauncherPreferences;

public class LauncherColorPreferenceFragment extends PreferenceFragmentCompat implements ColorPickerDialog.ColorPickerListener {
    private SharedPreferences mPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_color, rootKey);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        // Set up color preference click listeners
        setupColorPreference("background_app_color", "background_app");
        setupColorPreference("status_bar_color", "background_status_bar");
        setupColorPreference("bottom_bar_color", "background_bottom_bar");
        setupColorPreference("mine_button_color", "minebutton_color");
        setupColorPreference("icon_color", "icon_outline_color");
        setupColorPreference("primary_text_color", "primary_text");
        setupColorPreference("secondary_text_color", "secondary_text");
    }

    private void setupColorPreference(String prefKey, String colorKey) {
        Preference preference = findPreference(prefKey);
        if (preference != null) {
            preference.setOnPreferenceClickListener(pref -> {
                showColorPicker(prefKey, colorKey);
                return true;
            });
        }
    }

    private void showColorPicker(String prefKey, String colorKey) {
        int currentColor = Color.parseColor(mPreferences.getString(colorKey, "#000000"));
        ColorPickerDialog dialog = ColorPickerDialog.newInstance(colorKey, currentColor);
        dialog.show(getChildFragmentManager(), "color_picker");
    }

    @Override
    public void onColorSelected(String colorKey, int color) {
        // Save the color to preferences
        String colorHex = String.format("#%06X", (0xFFFFFF & color));
        mPreferences.edit().putString(colorKey, colorHex).apply();
        
        // Update the preference summary
        Preference preference = findPreference(colorKey + "_color");
        if (preference != null) {
            preference.setSummary(colorHex);
        }

        // Notify the activity to refresh the theme
        if (getActivity() != null) {
            getActivity().recreate();
        }
    }
} 