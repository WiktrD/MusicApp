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
        View view = inflater.inflate(R.layout.fragment_sound, container, false);

        seekBar = view.findViewById(R.id.seekBar);
        volumeText = view.findViewById(R.id.volume_level);
        View muteButton = view.findViewById(R.id.mute);

        // AudioManager z kontekstu
        audioManager = (AudioManager) requireContext().getSystemService(getContext().AUDIO_SERVICE);

        // Maksymalna i aktualna głośność
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        seekBar.setMax(maxVolume);
        seekBar.setProgress(currentVolume);
        updateVolumeText(currentVolume, maxVolume);

        // Zmiana głośności przez SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                updateVolumeText(progress, maxVolume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Obsługa mute
        muteButton.setOnClickListener(v -> {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            seekBar.setProgress(0);
            updateVolumeText(0, maxVolume);
        });

        return view;
    }

    private void updateVolumeText(int current, int max) {
        // Możesz wyświetlać procent lub "poziom X/Y"
        int percent = (int) (100.0 * current / max);
        volumeText.setText(getString(R.string.instrument_string_volume) + " " + percent + "%");
    }
}