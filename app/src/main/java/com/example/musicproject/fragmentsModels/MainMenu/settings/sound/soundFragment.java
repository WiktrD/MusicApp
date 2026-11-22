package com.example.musicproject.fragmentsModels.MainMenu.settings.sound;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicproject.R;

public class SoundFragment extends Fragment {
    private SeekBar seekBar;
    private TextView volumeText;
    private AudioManager audioManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sound, container, false);

        seekBar = view.findViewById(R.id.seekBar);
        volumeText = view.findViewById(R.id.volume_level);
        View muteButton = view.findViewById(R.id.mute);

        audioManager = (AudioManager) requireContext().getSystemService(Context.AUDIO_SERVICE);

        // Maximum and current music stream volume
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        seekBar.setMax(maxVolume);
        seekBar.setProgress(currentVolume);
        updateVolumeText(currentVolume, maxVolume);

        // Change the volume with the SeekBar
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

        // Mute button
        muteButton.setOnClickListener(v -> {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            seekBar.setProgress(0);
            updateVolumeText(0, maxVolume);
        });

        return view;
    }

    private void updateVolumeText(int current, int max) {
        int percent = (int) (100.0 * current / max);
        volumeText.setText(getString(R.string.instrument_string_volume) + " " + percent + "%");
    }
}
