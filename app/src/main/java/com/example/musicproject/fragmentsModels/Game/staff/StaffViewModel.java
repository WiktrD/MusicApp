package com.example.musicproject.fragmentsModels.Game.staff;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicproject.managers.GameTimer;
import com.example.musicproject.managers.NoteChallenge;
import com.example.musicproject.managers.NotePlayer;
import com.example.musicproject.managers.ResultManager;
import com.example.musicproject.models.Event;

public class StaffViewModel extends ViewModel {

    private static final long GAME_DURATION_MS = 60_000;
    private static final int NOTE_DURATION_MS = 1000;

    private final NotePlayer notePlayer;
    private final ResultManager resultManager;
    private final NoteChallenge noteChallenge;
    private final GameTimer timer;

    private final MutableLiveData<String> currentNote = new MutableLiveData<>();
    private final MutableLiveData<Long> secondsLeft = new MutableLiveData<>();
    private final MutableLiveData<Integer> correctCount = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> incorrectCount = new MutableLiveData<>(0);
    private final MutableLiveData<Event<Boolean>> answerResult = new MutableLiveData<>();
    private final MutableLiveData<Event<Integer>> gameFinished = new MutableLiveData<>();

    public StaffViewModel(NotePlayer notePlayer, ResultManager resultManager,
                          NoteChallenge noteChallenge, GameTimer timer) {
        this.notePlayer = notePlayer;
        this.resultManager = resultManager;
        this.noteChallenge = noteChallenge;
        this.timer = timer;

        loadNewNote();
        timer.start(GAME_DURATION_MS, new GameTimer.Listener() {
            @Override
            public void onTick(long millisLeft) {
                secondsLeft.setValue(millisLeft / 1000);
            }

            @Override
            public void onFinish() {
                secondsLeft.setValue(0L);
                gameFinished.setValue(new Event<>(resultManager.finish()));
            }
        });
    }

    public void submitAnswer(String userAnswer) {
        if (resultManager.isFinished()) return;

        boolean correct = noteChallenge.checkNoteAnswer(currentNote.getValue(), userAnswer);
        resultManager.recordAnswer(correct);
        correctCount.setValue(resultManager.getCorrect());
        incorrectCount.setValue(resultManager.getIncorrect());
        answerResult.setValue(new Event<>(correct));

        if (correct) {
            loadNewNote();
        }
    }

    public void playCurrentNote() {
        String note = currentNote.getValue();
        if (note != null) {
            notePlayer.playNoteByName(note, NOTE_DURATION_MS);
        }
    }

    private void loadNewNote() {
        currentNote.setValue(noteChallenge.generateRandomNote());
    }

    @Override
    protected void onCleared() {
        timer.cancel();
    }

    public LiveData<String> getCurrentNote() {
        return currentNote;
    }

    public LiveData<Long> getSecondsLeft() {
        return secondsLeft;
    }

    public LiveData<Integer> getCorrectCount() {
        return correctCount;
    }

    public LiveData<Integer> getIncorrectCount() {
        return incorrectCount;
    }

    /** Emits true/false once per answer (used for the success/loss animations). */
    public LiveData<Event<Boolean>> getAnswerResult() {
        return answerResult;
    }

    /** Emits the final score once, when the time is up. */
    public LiveData<Event<Integer>> getGameFinished() {
        return gameFinished;
    }
}
