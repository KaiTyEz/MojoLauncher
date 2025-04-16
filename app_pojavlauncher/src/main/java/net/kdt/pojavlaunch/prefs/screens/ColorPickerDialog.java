package net.kdt.pojavlaunch.prefs.screens;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import net.kdt.pojavlaunch.R;

public class ColorPickerDialog extends DialogFragment {
    private static final String ARG_COLOR = "color";
    private static final String ARG_COLOR_KEY = "color_key";
    private ColorPickerListener mListener;
    private String mColorKey;
    private int mCurrentColor;

    public interface ColorPickerListener {
        void onColorSelected(String colorKey, int color);
    }

    public static ColorPickerDialog newInstance(String colorKey, int color) {
        ColorPickerDialog dialog = new ColorPickerDialog();
        Bundle args = new Bundle();
        args.putString(ARG_COLOR_KEY, colorKey);
        args.putInt(ARG_COLOR, color);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ColorPickerListener) {
            mListener = (ColorPickerListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColorKey = getArguments().getString(ARG_COLOR_KEY);
            mCurrentColor = getArguments().getInt(ARG_COLOR);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_color_picker, null);

        SeekBar redSeekBar = view.findViewById(R.id.seekbar_red);
        SeekBar greenSeekBar = view.findViewById(R.id.seekbar_green);
        SeekBar blueSeekBar = view.findViewById(R.id.seekbar_blue);
        View colorPreview = view.findViewById(R.id.color_preview);

        // Set initial values
        redSeekBar.setProgress(Color.red(mCurrentColor));
        greenSeekBar.setProgress(Color.green(mCurrentColor));
        blueSeekBar.setProgress(Color.blue(mCurrentColor));
        colorPreview.setBackgroundColor(mCurrentColor);

        // Update color preview when seekbars change
        SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCurrentColor = Color.rgb(
                    redSeekBar.getProgress(),
                    greenSeekBar.getProgress(),
                    blueSeekBar.getProgress()
                );
                colorPreview.setBackgroundColor(mCurrentColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };

        redSeekBar.setOnSeekBarChangeListener(seekBarListener);
        greenSeekBar.setOnSeekBarChangeListener(seekBarListener);
        blueSeekBar.setOnSeekBarChangeListener(seekBarListener);

        builder.setView(view)
               .setTitle(R.string.select_color)
               .setPositiveButton(R.string.apply, (dialog, id) -> {
                   if (mListener != null) {
                       mListener.onColorSelected(mColorKey, mCurrentColor);
                   }
               })
               .setNegativeButton(R.string.cancel, (dialog, id) -> dismiss());

        return builder.create();
    }
} 