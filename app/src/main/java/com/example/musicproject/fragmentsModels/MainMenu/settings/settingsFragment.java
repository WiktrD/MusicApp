package com.example.musicproject.fragmentsModels.MainMenu.settings;

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
import com.example.musicproject.fragmentsModels.MainMenu.settings.instruments.instrumentFragment;
import com.example.musicproject.fragmentsModels.MainMenu.settings.langauge.langaugeFragment;
import com.example.musicproject.fragmentsModels.MainMenu.settings.sound.soundFragment;

public class settingsFragment extends Fragment {

    private SettingsViewModel mViewModel;

    public static settingsFragment newInstance() {
        return new settingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_settings, container, false);
        Button soundButton = view.findViewById(R.id.button_sound_settings);
        Button instrumentButton = view.findViewById(R.id.button_instruments_settings);
        Button languageButton = view.findViewById(R.id.button_language_settings);

        soundButton.setOnClickListener(v -> openSubSetting("settingsSound"));
        instrumentButton.setOnClickListener(v -> openSubSetting("settingsInstruments"));
        languageButton.setOnClickListener(v -> openSubSetting("settingsLangauge"));

        return view;
    }

    private void openSubSetting(String type) {
        Fragment fragment;

        switch (type) {
            case "settingsSound":
                fragment = new soundFragment();
                break;
            case "settingsInstruments":
                fragment = new instrumentFragment();
                break;
            case "settingsLangauge":
                fragment = new langaugeFragment();
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