package com.example.musicproject.models;

/** Best results shown on the scores screen. */
public class ScoreSummary {
    private final int bestPiano;
    private final int bestStaff;
    private final int bestQuiz;
    private final int bestAccuracy;

    public ScoreSummary(int bestPiano, int bestStaff, int bestQuiz, int bestAccuracy) {
        this.bestPiano = bestPiano;
        this.bestStaff = bestStaff;
        this.bestQuiz = bestQuiz;
        this.bestAccuracy = bestAccuracy;
    }

    public int getBestPiano() { return bestPiano; }

    public int getBestStaff() { return bestStaff; }

    public int getBestQuiz() { return bestQuiz; }

    public int getBestAccuracy() { return bestAccuracy; }
}
