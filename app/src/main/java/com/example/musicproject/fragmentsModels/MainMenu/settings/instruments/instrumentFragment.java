package com.example.musicproject.fragmentsModels.MainMenu.settings.instruments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicproject.AppViewModelFactory;
import com.example.musicproject.R;
import com.example.musicproject.managers.SoundManager;

public class InstrumentFragment extends Fragment {

    private static final float ALPHA_SELECTED = 1.0f;
    private static final float ALPHA_NOT_SELECTED = 0.5f;

    private InstrumentViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instrument, container, false);

        viewModel = new ViewModelProvider(this, new AppViewModelFactory(requireContext()))
                .get(InstrumentViewModel.class);

        Button pianoButton = view.findViewById(R.id.instrument_piano_button);
        Button xylophoneButton = view.findViewById(R.id.instrument_xylofon_button);
        Button organButton = view.findViewById(R.id.instrument_organs_button);

        pianoButton.setOnClickListener(v -> viewModel.setInstrument(SoundManager.PROGRAM_GRAND_PIANO));
        xylophoneButton.setOnClickListener(v -> viewModel.setInstrument(SoundManager.PROGRAM_XYLOPHONE));
        organButton.setOnClickListener(v -> viewModel.setInstrument(SoundManager.PROGRAM_CHURCH_ORGAN));

        viewModel.getSelectedInstrument().observe(getViewLifecycleOwner(), selected -> {
            updateButtonStyles(pianoButton, selected == SoundManager.PROGRAM_GRAND_PIANO);
            updateButtonStyles(xylophoneButton, selected == SoundManager.PROGRAM_XYLOPHONE);
            updateButtonStyles(organButton, selected == SoundManager.PROGRAM_CHURCH_ORGAN);
        });

        return view;
    }

    private void updateButtonStyles(Button button, boolean isSelected) {
        button.setAlpha(isSelected ? ALPHA_SELECTED : ALPHA_NOT_SELECTED);
    }
}
