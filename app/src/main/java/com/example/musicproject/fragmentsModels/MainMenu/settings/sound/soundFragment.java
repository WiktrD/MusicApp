package com.example.musicproject.fragmentsModels.MainMenu.settings.sound;

import androidx.lifecycle.ViewModelProvider;

import android.media.AudioManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.musicproject.R;

public class soundFragment extends Fragment {
    private SeekBar seekBar;
    private TextView volumeText;
    private AudioManager audioManager;

    private SoundViewModel mViewModel;

    public static soundFragment newInstance() {
        return new soundFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sound, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SoundViewModel.class);
        // TODO: Use the ViewModel
    }

}