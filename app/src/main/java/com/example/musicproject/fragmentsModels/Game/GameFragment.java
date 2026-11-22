package com.example.musicproject.fragmentsModels.Game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicproject.R;
import com.example.musicproject.fragmentsModels.Game.piano.PianoFragment;
import com.example.musicproject.fragmentsModels.Game.quiz.QuizFragment;
import com.example.musicproject.fragmentsModels.Game.staff.StaffFragment;
import com.example.musicproject.managers.ScreenNavigator;

public class GameFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        Button pianoButton = view.findViewById(R.id.button_game_piano);
        Button staffButton = view.findViewById(R.id.button_game_staff);
        Button quizButton = view.findViewById(R.id.button_game_quiz);

        pianoButton.setOnClickListener(v -> openGame(new PianoFragment()));
        staffButton.setOnClickListener(v -> openGame(new StaffFragment()));
        quizButton.setOnClickListener(v -> openGame(new QuizFragment()));

        return view;
    }

    private void openGame(Fragment game) {
        ScreenNavigator.open(requireActivity().getSupportFragmentManager(), game);
    }
}
