package com.example.musicproject.managers;

import android.content.Context;

import com.example.musicproject.database.AppDatabase;
import com.example.musicproject.database.GameScore;
import com.example.musicproject.database.ScoreDao;

import java.util.concurrent.Executor;

/**
 * Tracks the answers of one game round, calculates the score
 * and saves it to the database (once).
 */
public class ResultManager {

    private final String gameType; // one of GameTypes.*
    private final ScoreDao scoreDao;
    private final Executor ioExecutor;

    private int correct;
    private int incorrect;
    private boolean finished;

    public ResultManager(String gameType, ScoreDao scoreDao, Executor ioExecutor) {
        this.gameType = gameType;
        this.scoreDao = scoreDao;
        this.ioExecutor = ioExecutor;
    }

    /** Production factory: real database, saving on a background thread. */
    public static ResultManager create(Context context, String gameType) {
        ScoreDao dao = AppDatabase.Companion.getInstance(context).scoreDao();
        return new ResultManager(gameType, dao, runnable -> new Thread(runnable).start());
    }

    public void recordAnswer(boolean isCorrect) {
        if (finished) return;
        if (isCorrect) {
            correct++;
        } else {
            incorrect++;
        }
    }

    public int getCorrect() { return correct; }

    public int getIncorrect() { return incorrect; }

    public int getScore() { return correct - incorrect; }


    /** Saves the result on the first call; later calls only return the score. */
    public int finish() {
        if (!finished) {
            finished = true;
            GameScore entity = new GameScore(
                    0, gameType, getScore(), correct, incorrect, System.currentTimeMillis());
            ioExecutor.execute(() -> scoreDao.insertScoreSync(entity));
        }
        return getScore();
    }
}