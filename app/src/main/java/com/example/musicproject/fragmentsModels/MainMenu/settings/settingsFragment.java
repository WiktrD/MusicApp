package com.example.musicproject.fragmentsModels.MainMenu.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicproject.R;
import com.example.musicproject.fragmentsModels.MainMenu.settings.instruments.InstrumentFragment;
import com.example.musicproject.fragmentsModels.MainMenu.settings.language.LanguageFragment;
import com.example.musicproject.fragmentsModels.MainMenu.settings.sound.SoundFragment;
import com.example.musicproject.managers.ScreenNavigator;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Button soundButton = view.findViewById(R.id.button_sound_settings);
        Button instrumentButton = view.findViewById(R.id.button_instruments_settings);
        Button languageButton = view.findViewById(R.id.button_language_settings);

        soundButton.setOnClickListener(v -> openScreen(new SoundFragment()));
        instrumentButton.setOnClickListener(v -> openScreen(new InstrumentFragment()));
        languageButton.setOnClickListener(v -> openScreen(new LanguageFragment()));

        return view;
    }

    private void openScreen(Fragment screen) {
        ScreenNavigator.open(requireActivity().getSupportFragmentManager(), screen);
    }
}
