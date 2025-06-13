package com.example.musicproject.fragmentsModels.Game.staff;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicproject.managers.GameManager;
import com.example.musicproject.managers.SoundManager;

public class StaffViewModel extends ViewModel {
    private final MutableLiveData<String> currentNote = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isAnswerCorrect = new MutableLiveData<>();

    private final GameManager gameManager;
    private final SoundManager soundManager;

    public StaffViewModel() {
        gameManager = new GameManager();
        soundManager = SoundManager.getInstance();
        loadNewNote();
    }

    public LiveData<String> getCurrentNote() {
        return currentNote;
    }

    public LiveData<Boolean> getIsAnswerCorrect() {
        return isAnswerCorrect;
    }

    public void loadNewNote() {
        String note = gameManager.generateRandomNote();
        currentNote.setValue(note);
    }

    public void submitAnswer(String userAnswer) {
        boolean correct = gameManager.checkNoteAnswer(currentNote.getValue(), userAnswer);
        isAnswerCorrect.setValue(correct);

        if (correct) {
            loadNewNote();
        }
    }
    public void playCurrentNote() {
        String note = currentNote.getValue();
        if (note != null) {

            soundManager.playNoteByName(note,1000);
        }
    }
}