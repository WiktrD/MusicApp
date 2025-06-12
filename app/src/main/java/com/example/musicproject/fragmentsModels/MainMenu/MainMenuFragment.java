package com.example.musicproject.fragmentsModels.MainMenu;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.musicproject.GameActivity;
import com.example.musicproject.R;
import com.example.musicproject.fragmentsModels.MainMenu.score.scoreFragment;
import com.example.musicproject.fragmentsModels.MainMenu.settings.instruments.instrumentFragment;
import com.example.musicproject.fragmentsModels.MainMenu.settings.langauge.langaugeFragment;
import com.example.musicproject.fragmentsModels.MainMenu.settings.settingsFragment;
import com.example.musicproject.fragmentsModels.MainMenu.settings.sound.soundFragment;

public class MainMenuFragment extends Fragment {

    private com.example.musicproject.fragmentsModels.MainMenu.MainMenuViewModel mViewModel;

    public static MainMenuFragment newInstance() {
        return new MainMenuFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(com.example.musicproject.fragmentsModels.MainMenu.MainMenuViewModel.class);
        // TODO: Use the ViewModel
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu,container,false);
        Button buttonPlay= view.findViewById(R.id.button_play);
        Button buttonScore= view.findViewById(R.id.button_scores);
        Button buttonSettings= view.findViewById(R.id.button_settings);
        Button buttonQuit= view.findViewById(R.id.button_exit);
        buttonPlay.setOnClickListener(v->{
            Intent intent = new Intent(requireContext(), GameActivity.class);
            startActivity(intent);
        });
        buttonSettings.setOnClickListener(v -> openFragment("settings"));
        buttonScore.setOnClickListener(v -> openFragment("results"));
        buttonQuit.setOnClickListener(v -> requireActivity().finishAffinity());

        return view;

    }

    private void openFragment(String type) {
        Fragment fragment;

        switch (type) {
            case "settings":
                fragment = new settingsFragment();
                break;
            case "results":
                fragment = new scoreFragment();
                break;
            default:
                throw new IllegalArgumentException("Unknown fragment type: " + type);
        }  requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    }