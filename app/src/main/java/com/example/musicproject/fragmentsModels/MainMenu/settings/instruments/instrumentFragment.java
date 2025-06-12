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

        Button pianoButton = view.findViewById(R.id.button_piano);
        Button guitarButton = view.findViewById(R.id.button_guitar);
        Button fluteButton = view.findViewById(R.id.button_flute);

        pianoButton.setOnClickListener(v -> viewModel.setInstrument(0));   // Grand Piano
        guitarButton.setOnClickListener(v -> viewModel.setInstrument(13)); // Xylophone
        fluteButton.setOnClickListener(v -> viewModel.setInstrument(19));  // Church Organ

        return view;
    }


}