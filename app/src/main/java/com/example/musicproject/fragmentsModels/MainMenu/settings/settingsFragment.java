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

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Button soundButton = view.findViewById(R.id.button_sound_settings);
        Button instrumentButton = view.findViewById(R.id.button_instruments_settings);
        Button languageButton = view.findViewById(R.id.button_language_settings);

        soundButton.setOnClickListener(v -> openSubSetting("settingsSound"));
        instrumentButton.setOnClickListener(v -> openSubSetting("settingsInstruments"));
        languageButton.setOnClickListener(v -> openSubSetting("settingsLanguage"));

        return view;
    }

    private void openSubSetting(String type) {
        Fragment fragment;

        switch (type) {
            case "settingsSound":
                fragment = new SoundFragment();
                break;
            case "settingsInstruments":
                fragment = new InstrumentFragment();
                break;
            case "settingsLanguage":
                fragment = new LanguageFragment();
                break;
            default:
                throw new IllegalArgumentException("Unknown setting type: " + type);
        }

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
