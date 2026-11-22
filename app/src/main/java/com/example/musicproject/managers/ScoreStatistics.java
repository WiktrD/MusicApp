package com.example.musicproject.managers;

import com.example.musicproject.database.GameScore;

import java.util.List;

/** Pure calculations on saved scores (no Android dependencies). */
public final class ScoreStatistics {

    private ScoreStatistics() {
    }

    /** Best "points" value in the list (points = correct - incorrect), 0 for an empty list. */
    public static int bestPoints(List<GameScore> scores) {
        if (scores == null || scores.isEmpty()) return 0;
        int best = Integer.MIN_VALUE;
        for (GameScore score : scores) {
            if (score.getPoints() > best) best = score.getPoints();
        }
        return best;
    }

    /** Best accuracy (percentage of correct answers) in the list, 0 for an empty list. */
    public static int bestAccuracy(List<GameScore> scores) {
        if (scores == null) return 0;
        int bestAccuracy = 0;
        for (GameScore score : scores) {
            int total = score.getCorrectCount() + score.getIncorrectCount();
            if (total > 0) {
                int accuracy = (int) (100.0 * score.getCorrectCount() / total);
                if (accuracy > bestAccuracy) bestAccuracy = accuracy;
            }
        }
        return bestAccuracy;
    }
}
