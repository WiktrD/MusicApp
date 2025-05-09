package com.example.musicproject.fragmentsModels.piano;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicproject.managers.SoundManager;

import java.util.ArrayList;
import java.util.List;

public class PianoViewModel extends ViewModel {
    private final MutableLiveData<List<String>> pressedKeys = new MutableLiveData<>(new ArrayList<>());
    private final SoundManager soundManager;

    public PianoViewModel() {
        soundManager = new SoundManager();
    }

    public LiveData<List<String>> getPressedKeys() {
        return pressedKeys;
    }

    public void pressKey(String note) {
        List<String> currentKeys = pressedKeys.getValue();
        if (currentKeys == null) return;

        currentKeys.add(note);
        pressedKeys.setValue(currentKeys);

        //soundManager.playNote(note); // Gra dźwięk
    }

    public void clearKeys() {
        pressedKeys.setValue(new ArrayList<>());
    }
}