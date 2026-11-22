package com.example.musicproject.fragmentsModels.Game.piano;

import android.app.Application;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.musicproject.database.GameTypes;
import com.example.musicproject.managers.ResultManager;
import com.example.musicproject.managers.SoundManager;

import java.util.Random;

public class PianoViewModel extends AndroidViewModel {

    private static final int MIDDLE_C_MIDI_NOTE = 60;
    private static final int NOTES_PER_OCTAVE = 12;
    private static final long TIMER_TICK_MS = 1000;

    private final SoundManager soundManager;
    private final ResultManager resultManager;

    private final MutableLiveData<Long> timeLeft = new MutableLiveData<>();
    private final MutableLiveData<Boolean> gameEnded = new MutableLiveData<>(false);
    private CountDownTimer countDownTimer;

    private int targetNote = -1;
    private int correctCount = 0;
    private int incorrectCount = 0;
    private final MutableLiveData<Integer> targetNoteLive = new MutableLiveData<>();
    private final MutableLiveData<Integer> correctCountLive = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> incorrectCountLive = new MutableLiveData<>(0);

    public PianoViewModel(@NonNull Application application) {
        super(application);
        resultManager = ResultManager.create(application, GameTypes.PIANO);
        soundManager = SoundManager.getInstance();
        generateRandomNote();
    }


    public void playNote(int midiNote) {
        soundManager.playNote(midiNote);
    }

    public void stopNote(int midiNote) {
        soundManager.stopNote(midiNote);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (countDownTimer != null) countDownTimer.cancel();
    }

    public void generateRandomNote() {
        targetNote = MIDDLE_C_MIDI_NOTE + new Random().nextInt(NOTES_PER_OCTAVE);
        targetNoteLive.setValue(targetNote);
    }

    public void checkNote(int playedNote) {
        boolean isCorrect = playedNote == targetNote;
        resultManager.recordAnswer(isCorrect);
        if (isCorrect) {
            correctCountLive.setValue(resultManager.getCorrect());
        } else {
            incorrectCountLive.setValue(resultManager.getIncorrect());
        }
        generateRandomNote(); // draw a new target note
    }

    public void startGameTimer(long durationMillis) {
        if (countDownTimer != null) countDownTimer.cancel();
        gameEnded.setValue(false);
        countDownTimer = new CountDownTimer(durationMillis, TIMER_TICK_MS) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft.setValue(millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                timeLeft.setValue(0L);
                gameEnded.setValue(true);
            }
        };
        countDownTimer.start();
    }

    public int getCorrectCount() { return resultManager.getCorrect(); }
    public int getIncorrectCount() { return resultManager.getIncorrect(); }
    public int finishGame() { return resultManager.finish(); }

    public LiveData<Integer> getTargetNoteLive() {
        return targetNoteLive;
    }

    public LiveData<Integer> getCorrectCountLive() {
        return correctCountLive;
    }

    public LiveData<Integer> getIncorrectCountLive() {
        return incorrectCountLive;
    }

    public LiveData<Long> getTimeLeft() {
        return timeLeft;
    }

    public LiveData<Boolean> isGameEnded() {
        return gameEnded;
    }
}
