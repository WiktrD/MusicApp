package com.example.musicproject.fragmentsModels.Game.staff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.example.atmusic.StaveView;
import com.example.musicproject.AppViewModelFactory;
import com.example.musicproject.R;
import com.example.musicproject.fragmentsModels.Game.GameResultFragment;
import com.example.musicproject.managers.AnimationManager;
import com.example.musicproject.managers.ScreenNavigator;

public class StaffFragment extends Fragment {

    private static final long PLAY_BUTTON_COOLDOWN_MS = 1000;

    private StaffViewModel viewModel;
    private String selectedNote = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_staff, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, new AppViewModelFactory(requireContext()))
                .get(StaffViewModel.class);

        LottieAnimationView introAnim = view.findViewById(R.id.lottie_intro);
        LottieAnimationView successAnim = view.findViewById(R.id.lottie_success);
        LottieAnimationView lossAnim = view.findViewById(R.id.lottie_loss);
        AnimationManager.showLottieAnimation(introAnim);

        View playButton = view.findViewById(R.id.play_button);
        TextView correctNoteText = view.findViewById(R.id.correct_note);
        TextView wrongNoteText = view.findViewById(R.id.wrong_note);
        TextView targetNoteText = view.findViewById(R.id.random_note);
        TextView timerText = view.findViewById(R.id.time_text);
        StaveView staveView = view.findViewById(R.id.stave_view);

        // Observe the note the player has to guess
        viewModel.getCurrentNote().observe(getViewLifecycleOwner(), note -> {
            targetNoteText.setText(note != null ? note : "-");

            // Automatically play the new note
            playCurrentNote(playButton);
        });

        viewModel.getSecondsLeft().observe(getViewLifecycleOwner(),
                seconds -> timerText.setText(seconds + " s"));
        viewModel.getCorrectCount().observe(getViewLifecycleOwner(),
                count -> correctNoteText.setText(String.valueOf(count)));
        viewModel.getIncorrectCount().observe(getViewLifecycleOwner(),
                count -> wrongNoteText.setText(String.valueOf(count)));

        viewModel.getAnswerResult().observe(getViewLifecycleOwner(), event -> {
            Boolean correct = event.getContentIfNotHandled();
            if (correct != null) {
                AnimationManager.showLottieAnimation(correct ? successAnim : lossAnim);
            }
        });

        viewModel.getGameFinished().observe(getViewLifecycleOwner(), event -> {
            Integer score = event.getContentIfNotHandled();
            if (score != null) {
                Toast.makeText(requireContext(), getString(R.string.end_message, score), Toast.LENGTH_LONG).show();
                ScreenNavigator.open(requireActivity().getSupportFragmentManager(),
                        GameResultFragment.newInstance(score));
            }
        });

        playButton.setOnClickListener(v -> playCurrentNote(playButton));

        // Remember the note the player placed on the stave
        staveView.setOnTouchListener((v, event) -> {
            selectedNote = staveView.getSelectedNoteName();
            return false;
        });

        // Submit the answer
        view.findViewById(R.id.accept_button).setOnClickListener(v -> {
            if (selectedNote != null) {
                viewModel.submitAnswer(selectedNote);
                selectedNote = null; // reset the selection for the next question
            }
        });
    }

    /** Plays the current note and keeps the play button disabled for a moment. */
    private void playCurrentNote(View playButton) {
        playButton.setEnabled(false);
        viewModel.playCurrentNote();
        playButton.postDelayed(() -> playButton.setEnabled(true), PLAY_BUTTON_COOLDOWN_MS);
    }
}
