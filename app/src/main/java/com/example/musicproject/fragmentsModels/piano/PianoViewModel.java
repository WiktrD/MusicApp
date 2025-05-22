package com.example.musicproject.fragmentsModels.piano;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicproject.managers.SoundManager;

public class PianoViewModel extends ViewModel {
    private final SoundManager soundManager;
    private final MutableLiveData<String> playedNote;

    public PianoViewModel() {
        soundManager = new SoundManager();
        playedNote = new MutableLiveData<>();
    }

    public void playNote(int midiNote) {
        soundManager.playNote( midiNote);
        playedNote.setValue(String.valueOf(midiNote));
    }
    public void stopNote(int midiNote) {
        soundManager.stopNote(midiNote);
    }

    public LiveData<String> getPlayedNote() {
        return playedNote;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        soundManager.stop();
    }
    public SoundManager getSoundManager() {
        return soundManager;
    }
}