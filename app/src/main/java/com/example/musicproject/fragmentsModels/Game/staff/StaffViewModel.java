package com.example.musicproject.fragmentsModels.Game.staff;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicproject.managers.NoteChallenge;
import com.example.musicproject.managers.SoundManager;

public class StaffViewModel extends ViewModel {

    private static final int NOTE_DURATION_MS = 1000;

    private final MutableLiveData<String> currentNote = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isAnswerCorrect = new MutableLiveData<>();

    private final NoteChallenge noteChallenge;
    private final SoundManager soundManager;

    public StaffViewModel() {
        noteChallenge = new NoteChallenge();
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
        String note = noteChallenge.generateRandomNote();
        currentNote.setValue(note);
    }

    public void submitAnswer(String userAnswer) {
        boolean correct = noteChallenge.checkNoteAnswer(currentNote.getValue(), userAnswer);
        isAnswerCorrect.setValue(correct);

        if (correct) {
            loadNewNote();
        }
    }

    public void playCurrentNote() {
        String note = currentNote.getValue();
        if (note != null) {
            soundManager.playNoteByName(note, NOTE_DURATION_MS);
        }
    }
}
