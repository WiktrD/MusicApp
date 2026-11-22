package com.example.musicproject.fragmentsModels.MainMenu.score;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicproject.database.GameScore;
import com.example.musicproject.database.GameTypes;
import com.example.musicproject.database.ScoreDao;
import com.example.musicproject.managers.ScoreStatistics;
import com.example.musicproject.models.ScoreSummary;

import java.util.List;
import java.util.concurrent.Executor;

public class ScoreViewModel extends ViewModel {

    private final ScoreDao scoreDao;
    private final Executor ioExecutor;

    private final LiveData<List<GameScore>> pianoScores;
    private final LiveData<List<GameScore>> staffScores;
    private final LiveData<List<GameScore>> quizScores;
    private final MediatorLiveData<ScoreSummary> summary = new MediatorLiveData<>();

    public ScoreViewModel(ScoreDao scoreDao, Executor ioExecutor) {
        this.scoreDao = scoreDao;
        this.ioExecutor = ioExecutor;

        pianoScores = scoreDao.observeScores(GameTypes.PIANO);
        staffScores = scoreDao.observeScores(GameTypes.STAFF);
        quizScores = scoreDao.observeScores(GameTypes.QUIZ);

        summary.addSource(pianoScores, scores -> updateSummary());
        summary.addSource(staffScores, scores -> updateSummary());
        summary.addSource(quizScores, scores -> updateSummary());
    }

    private void updateSummary() {
        List<GameScore> piano = pianoScores.getValue();
        List<GameScore> staff = staffScores.getValue();
        List<GameScore> quiz = quizScores.getValue();
        if (piano == null || staff == null || quiz == null) return; // wait for all three

        int bestAccuracy = Math.max(ScoreStatistics.bestAccuracy(piano),
                Math.max(ScoreStatistics.bestAccuracy(staff), ScoreStatistics.bestAccuracy(quiz)));

        summary.setValue(new ScoreSummary(
                ScoreStatistics.bestPoints(piano),
                ScoreStatistics.bestPoints(staff),
                ScoreStatistics.bestPoints(quiz),
                bestAccuracy));
    }

    public LiveData<ScoreSummary> getSummary() {
        return summary;
    }

    /** Deletes all scores; the summary refreshes by itself when the table changes. */
    public void resetScores() {
        ioExecutor.execute(scoreDao::deleteAll);
    }
}
