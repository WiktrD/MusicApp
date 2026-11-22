package com.example.musicproject.fragmentsModels.Game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.musicproject.R;
import com.example.musicproject.fragmentsModels.Game.piano.PianoFragment;
import com.example.musicproject.fragmentsModels.Game.quiz.QuizFragment;
import com.example.musicproject.fragmentsModels.Game.staff.StaffFragment;

public class GameFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        Button pianoButton = view.findViewById(R.id.button_game_piano);
        Button staffButton = view.findViewById(R.id.button_game_staff);
        Button quizButton = view.findViewById(R.id.button_game_quiz);

        pianoButton.setOnClickListener(v -> openGame("piano"));
        staffButton.setOnClickListener(v -> openGame("staff"));
        quizButton.setOnClickListener(v -> openGame("quiz"));

        return view;
    }

    private void openGame(String gameType) {
        Fragment fragment;
        switch (gameType) {
            case "piano":
                fragment = new PianoFragment();
                break;
            case "staff":
                fragment = new StaffFragment();
                break;
            case "quiz":
                fragment = new QuizFragment();
                break;
            default:
                throw new IllegalArgumentException("Unknown game type: " + gameType);
        }

        FragmentTransaction transaction = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
