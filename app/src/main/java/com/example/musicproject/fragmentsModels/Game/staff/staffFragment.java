package com.example.musicproject.fragmentsModels.Game.staff;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.atmusic.StaveView;
import com.example.musicproject.R;
import com.example.musicproject.database.AppDatabase;
import com.example.musicproject.database.Wynik;
import com.example.musicproject.fragmentsModels.Game.GameResultFragment;

public class staffFragment extends Fragment {

    private StaffViewModel viewModel;

    private int correctCount = 0;
    private int incorrectCount = 0;

    private TextView correctNoteText, wrongNoteText, targetNoteText, timerText;
    private CountDownTimer timer;
    private final long GAME_DURATION_MS = 60_000;

    private String selectedNote = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_staff, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(StaffViewModel.class);
        View playButton = view.findViewById(R.id.play_button);

        correctNoteText = view.findViewById(R.id.correct_note);
        wrongNoteText = view.findViewById(R.id.wrong_note);
        targetNoteText = view.findViewById(R.id.random_note);

        // Dodaj TextView do timera w XML, np. o id time_text
        timerText = view.findViewById(R.id.time_text);

        StaveView staveView = view.findViewById(R.id.stave_view);

        // Obserwuj wyświetlaną nutę do zgadywania
        viewModel.getCurrentNote().observe(getViewLifecycleOwner(), note -> {
            targetNoteText.setText(note != null ? note : "-");

            // Auto odtwarzanie nowej nuty:
            playButton.setEnabled(false);
            viewModel.playCurrentNote();
            playButton.postDelayed(() -> playButton.setEnabled(true), 1000);
        });

        viewModel.getIsAnswerCorrect().observe(getViewLifecycleOwner(), correct -> {
            if (correct) {
                correctCount++;
            } else {
                incorrectCount++;
            }
            correctNoteText.setText(String.valueOf(correctCount));
            wrongNoteText.setText(String.valueOf(incorrectCount));
        });



        view.findViewById(R.id.play_button).setOnClickListener(v -> {
            playButton.setEnabled(false);
            viewModel.playCurrentNote();
            playButton.postDelayed(() -> playButton.setEnabled(true), 1000);

        });


        // Interakcja ze StaveView
        staveView.setOnTouchListener((v, event) -> {
            selectedNote = staveView.getTouchedNoteName();
            return false;
        });

        // Akceptuj odpowiedź
        view.findViewById(R.id.accept_button).setOnClickListener(v -> {
            if (selectedNote != null) {
                viewModel.submitAnswer(selectedNote);
                selectedNote = null; // resetuj zaznaczenie na następne pytanie
            }
        });

        // Wystartuj timer!
        startGameTimer();
    }

    private void startGameTimer() {
        timer = new CountDownTimer(GAME_DURATION_MS, 1000) {
            public void onTick(long millisUntilFinished) {
                if (timerText != null) {
                    timerText.setText((millisUntilFinished / 1000) + " s");
                }
            }

            public void onFinish() {
                showFinalScore();
            }
        }.start();
    }

    private void showFinalScore() {
        int total = correctCount + incorrectCount;
        int score = (int) (100.0 * correctCount / (total > 0 ? total : 1));

        Toast.makeText(requireContext(), getString(R.string.end_message, score), Toast.LENGTH_LONG).show();

        // ZAPIS DO BAZY DANYCH – Thread, bo insertWynik to suspend
        new Thread(() -> {
            AppDatabase db = AppDatabase.Companion.getInstance(requireContext());
            Wynik wynik = new Wynik(
                    0,
                    "pieciolinia",
                    score,
                    correctCount,
                    incorrectCount,
                    System.currentTimeMillis()
            );
            db.wynikDao().insertWynikSync(wynik); // <- MUSISZ DODAĆ TEN insertWynikSync do WynikDao!
        }).start();

        // Przejście do GameResultFragment z wynikiem
        Bundle bundle = new Bundle();
        bundle.putInt("score", score);
        GameResultFragment resultFragment = new GameResultFragment();
        resultFragment.setArguments(bundle);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, resultFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) timer.cancel();
    }
}
