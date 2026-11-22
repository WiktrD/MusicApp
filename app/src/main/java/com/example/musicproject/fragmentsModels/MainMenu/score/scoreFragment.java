package com.example.musicproject.fragmentsModels.MainMenu.score;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicproject.AppViewModelFactory;
import com.example.musicproject.R;

public class ScoreFragment extends Fragment {

    private ScoreViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);

        viewModel = new ViewModelProvider(this, new AppViewModelFactory(requireContext()))
                .get(ScoreViewModel.class);

        TextView pianoText = view.findViewById(R.id.best_piano);
        TextView staveText = view.findViewById(R.id.best_stave);
        TextView quizText = view.findViewById(R.id.best_quiz);
        TextView accuracyText = view.findViewById(R.id.best_accuracy);

        View resetButton = view.findViewById(R.id.button_reset);
        if (resetButton != null) {
            resetButton.setOnClickListener(v -> viewModel.resetScores());
        }

        viewModel.getSummary().observe(getViewLifecycleOwner(), summary -> {
            pianoText.setText(summary.getBestPiano() + getString(R.string.pkt));
            staveText.setText(summary.getBestStaff() + getString(R.string.pkt));
            quizText.setText(summary.getBestQuiz() + getString(R.string.pkt));
            accuracyText.setText(summary.getBestAccuracy() + " %");
        });

        return view;
    }
}
