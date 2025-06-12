package com.example.musicproject.fragmentsModels.Game.piano;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicproject.managers.SoundManager;

import java.util.Random;

public class PianoViewModel extends ViewModel {
    private final SoundManager soundManager;
    private final MutableLiveData<String> playedNote;
    private int targetNote = -1;
    private int correctCount = 0;
    private int incorrectCount = 0;
    private final MutableLiveData<Integer> targetNoteLive = new MutableLiveData<>();
    private final MutableLiveData<Integer> correctCountLive = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> incorrectCountLive = new MutableLiveData<>(0);

    public PianoViewModel() {

        soundManager =  SoundManager.getInstance();
        playedNote = new MutableLiveData<>();
        generateRandomNote();

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
    }
    public SoundManager getSoundManager() {
        return soundManager;
    }
    public void generateRandomNote() {
        targetNote = 60 + new Random().nextInt(12);
        targetNoteLive.setValue(targetNote);
    }

    public void checkNote(int playedNote) {
        if (playedNote == targetNote) {
            correctCount++;
            correctCountLive.setValue(correctCount);
        } else {
            incorrectCount++;
            incorrectCountLive.setValue(incorrectCount);
        }
        generateRandomNote(); // losuj nową
    }
    public int getTargetNote() {
        return targetNote;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public int getIncorrectCount() {
        return incorrectCount;
    }
    public LiveData<Integer> getTargetNoteLive() {
        return targetNoteLive;
    }
    public LiveData<Integer> getCorrectCountLive() {
        return correctCountLive;
    }
    public LiveData<Integer> getIncorrectCountLive() {
        return incorrectCountLive;
    }


}