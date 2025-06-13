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
import com.example.musicproject.database.Wynik;

import java.util.List;
import java.util.concurrent.Executors;

public class scoreFragment extends Fragment {

    private TextView pianoText, staveText, quizText, accuracyText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);

        pianoText = view.findViewById(R.id.best_piano);
        staveText = view.findViewById(R.id.best_stave);
        quizText = view.findViewById(R.id.best_quiz);
        accuracyText = view.findViewById(R.id.quiz_result);

        // Obsługa przycisku reset
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
                List<Wynik> pianoList = db.wynikDao().getWynikiDlaGrySync("pianino");
                List<Wynik> staveList = db.wynikDao().getWynikiDlaGrySync("pieciolinia");
                List<Wynik> quizList = db.wynikDao().getWynikiDlaGrySync("quiz");

                int pianoBest = calculateBest(pianoList);
                int staveBest = calculateBest(staveList);
                int quizBest = calculateBest(quizList);

                int accuracyBest = calculateBestAccuracy(pianoList, staveList, quizList);

                requireActivity().runOnUiThread(() -> {
                    pianoText.setText(pianoBest + " pkt");
                    staveText.setText(staveBest + " pkt");
                    quizText.setText(quizBest + " pkt");
                    accuracyText.setText(accuracyBest + " %");
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Najlepszy wynik "punkty" z listy (różnica poprawne - błędne)
    private int calculateBest(List<Wynik> wyniki) {
        int best = Integer.MIN_VALUE;
        for (Wynik w : wyniki) {
            if (w.getPunkty() > best) best = w.getPunkty();
        }
        return best == Integer.MIN_VALUE ? 0 : best;
    }

    // Najlepszy accuracy (procent poprawnych) ze wszystkich konkurencji
    private int calculateBestAccuracy(List<Wynik>... lists) {
        int bestAccuracy = 0;
        for (List<Wynik> wyniki : lists) {
            for (Wynik w : wyniki) {
                int total = w.getPoprawne() + w.getBledne();
                if (total > 0) {
                    int accuracy = (int) (100.0 * w.getPoprawne() / total);
                    if (accuracy > bestAccuracy) bestAccuracy = accuracy;
                }
            }
        }
        return bestAccuracy;
    }

    // RESET wszystkiego w tabeli Wynik
    private void resetScores() {
        AppDatabase db = AppDatabase.Companion.getInstance(requireContext());
        Executors.newSingleThreadExecutor().execute(() -> {
            db.wynikDao().deleteAll();
            // Po resecie odśwież wyniki na ekranie
            loadScores();
        });
    }
}
