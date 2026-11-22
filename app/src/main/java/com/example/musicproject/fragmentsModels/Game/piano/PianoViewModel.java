package com.example.musicproject.fragmentsModels.Game.piano;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicproject.managers.GameTimer;
import com.example.musicproject.managers.NotePlayer;
import com.example.musicproject.managers.ResultManager;
import com.example.musicproject.models.Event;

import java.util.Random;

public class PianoViewModel extends ViewModel {

    private static final int MIDDLE_C_MIDI_NOTE = 60;
    private static final int NOTES_PER_OCTAVE = 12;
    private static final long GAME_DURATION_MS = 60_000;

    private final NotePlayer notePlayer;
    private final ResultManager resultManager;
    private final GameTimer timer;
    private final Random random;

    private final MutableLiveData<Long> timeLeft = new MutableLiveData<>();
    private final MutableLiveData<Event<Integer>> gameFinished = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> answerResult = new MutableLiveData<>();
    private final MutableLiveData<Integer> targetNoteLive = new MutableLiveData<>();
    private final MutableLiveData<Integer> correctCountLive = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> incorrectCountLive = new MutableLiveData<>(0);
    private int targetNote = -1;

    public PianoViewModel(NotePlayer notePlayer, ResultManager resultManager,
                          GameTimer timer, Random random) {
        this.notePlayer = notePlayer;
        this.resultManager = resultManager;
        this.timer = timer;
        this.random = random;

        generateRandomNote();
        timer.start(GAME_DURATION_MS, new GameTimer.Listener() {
            @Override
            public void onTick(long millisLeft) {
                timeLeft.setValue(millisLeft / 1000);
            }

            @Override
            public void onFinish() {
                timeLeft.setValue(0L);
                gameFinished.setValue(new Event<>(resultManager.finish()));
            }
        });
    }

    public void playNote(int midiNote) {
        notePlayer.playNote(midiNote);
    }

    public void stopNote(int midiNote) {
        notePlayer.stopNote(midiNote);
    }

    public void checkNote(int playedNote) {
        if (resultManager.isFinished()) return;

        boolean isCorrect = playedNote == targetNote;
        resultManager.recordAnswer(isCorrect);
        if (isCorrect) {
            correctCountLive.setValue(resultManager.getCorrect());
        } else {
            incorrectCountLive.setValue(resultManager.getIncorrect());
        }
        answerResult.setValue(new Event<>(isCorrect));
        generateRandomNote(); // draw a new target note
    }

    private void generateRandomNote() {
        targetNote = MIDDLE_C_MIDI_NOTE + random.nextInt(NOTES_PER_OCTAVE);
        targetNoteLive.setValue(targetNote);
    }

    @Override
    protected void onCleared() {
        timer.cancel();
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

    public LiveData<Long> getTimeLeft() {
        return timeLeft;
    }

    /** Emits the final score once, when the time is up. */
    public LiveData<Event<Integer>> getGameFinished() {
        return gameFinished;
    }

    /** Emits true/false once per answer (used for the success/loss animations). */
    public LiveData<Event<Boolean>> getAnswerResult() {
        return answerResult;
    }
}
