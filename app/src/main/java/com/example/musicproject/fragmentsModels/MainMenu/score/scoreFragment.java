package com.example.musicproject.fragmentsModels.MainMenu.score;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicproject.R;
import com.example.musicproject.database.AppDatabase;
import com.example.musicproject.database.GameScore;
import com.example.musicproject.database.GameTypes;

import java.util.List;
import java.util.concurrent.Executors;

public class ScoreFragment extends Fragment {

    private TextView pianoText, staveText, quizText, accuracyText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);

        pianoText = view.findViewById(R.id.best_piano);
        staveText = view.findViewById(R.id.best_stave);
        quizText = view.findViewById(R.id.best_quiz);
        accuracyText = view.findViewById(R.id.best_accuracy);

        View resetButton = view.findViewById(R.id.button_reset);
        if (resetButton != null) {
            resetButton.setOnClickListener(v -> resetScores());
        }

        loadScores();

        return view;
    }

    private void loadScores() {
        AppDatabase db = AppDatabase.Companion.getInstance(requireContext());

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<GameScore> pianoScores = db.scoreDao().getScoresForGameSync(GameTypes.PIANO);
                List<GameScore> staffScores = db.scoreDao().getScoresForGameSync(GameTypes.STAFF);
                List<GameScore> quizScores = db.scoreDao().getScoresForGameSync(GameTypes.QUIZ);

                int pianoBest = calculateBest(pianoScores);
                int staffBest = calculateBest(staffScores);
                int quizBest = calculateBest(quizScores);

                int accuracyBest = calculateBestAccuracy(pianoScores, staffScores, quizScores);

                requireActivity().runOnUiThread(() -> {
                    pianoText.setText(pianoBest + getString(R.string.pkt));
                    staveText.setText(staffBest + getString(R.string.pkt));
                    quizText.setText(quizBest + getString(R.string.pkt));
                    accuracyText.setText(accuracyBest + " %");
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Best "points" value in the list (points = correct - incorrect)
    private int calculateBest(List<GameScore> scores) {
        int best = Integer.MIN_VALUE;
        for (GameScore score : scores) {
            if (score.getPoints() > best) best = score.getPoints();
        }
        return best == Integer.MIN_VALUE ? 0 : best;
    }

    // Best accuracy (percentage of correct answers) across all games
    @SafeVarargs
    private final int calculateBestAccuracy(List<GameScore>... scoreLists) {
        int bestAccuracy = 0;
        for (List<GameScore> scores : scoreLists) {
            for (GameScore score : scores) {
                int total = score.getCorrectCount() + score.getIncorrectCount();
                if (total > 0) {
                    int accuracy = (int) (100.0 * score.getCorrectCount() / total);
                    if (accuracy > bestAccuracy) bestAccuracy = accuracy;
                }
            }
        }
        return bestAccuracy;
    }

    // Delete all scores
    private void resetScores() {
        AppDatabase db = AppDatabase.Companion.getInstance(requireContext());
        Executors.newSingleThreadExecutor().execute(() -> {
            db.scoreDao().deleteAll();
            // Refresh the displayed scores after the reset
            loadScores();
        });
    }
}
