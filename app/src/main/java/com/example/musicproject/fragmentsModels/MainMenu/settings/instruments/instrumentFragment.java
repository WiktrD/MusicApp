package com.example.musicproject.fragmentsModels.MainMenu.settings.instruments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.musicproject.R;

public class instrumentFragment extends Fragment {

    private InstrumentViewModel viewModel;

    public static instrumentFragment newInstance() {
        return new instrumentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instrument, container, false);

        viewModel = new ViewModelProvider(this).get(InstrumentViewModel.class);

        Button pianoButton = view.findViewById(R.id.instrument_piano_button);
        Button guitarButton = view.findViewById(R.id.instrument_xylofon_button);
        Button fluteButton = view.findViewById(R.id.instrument_organs_button);

        pianoButton.setOnClickListener(v -> viewModel.setInstrument(0));   // Grand Piano
        guitarButton.setOnClickListener(v -> viewModel.setInstrument(13)); // Xylophone
        fluteButton.setOnClickListener(v -> viewModel.setInstrument(19));  // Church Organ

        viewModel.getSelectedInstrument().observe(getViewLifecycleOwner(), selected -> {
            updateButtonStyles(pianoButton, selected == 0);
            updateButtonStyles(guitarButton, selected == 13);
            updateButtonStyles(fluteButton, selected == 19);
        });


        return view;
    }
    private void updateButtonStyles(Button button, boolean isSelected) {
        if (isSelected) {
            button.setAlpha(1.0f);
            button.setBackgroundColor(getResources().getColor(R.color.teal_200, null)); // lub inny kolor
        } else {
            button.setAlpha(0.5f);
            button.setBackgroundColor(getResources().getColor(R.color.gray, null));
        }
    }

}