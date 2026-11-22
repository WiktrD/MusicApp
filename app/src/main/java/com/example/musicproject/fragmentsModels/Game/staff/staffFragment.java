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

import com.airbnb.lottie.LottieAnimationView;
import com.example.atmusic.StaveView;
import com.example.musicproject.R;
import com.example.musicproject.database.GameTypes;
import com.example.musicproject.fragmentsModels.Game.GameResultFragment;
import com.example.musicproject.managers.AnimationManager;
import com.example.musicproject.managers.ResultManager;

public class StaffFragment extends Fragment {

    private static final long GAME_DURATION_MS = 60_000;
    private static final long TIMER_TICK_MS = 1000;
    private static final long PLAY_BUTTON_COOLDOWN_MS = 1000;

    private StaffViewModel viewModel;

    private ResultManager resultManager;

    private TextView correctNoteText, wrongNoteText, targetNoteText, timerText;
    private CountDownTimer timer;

    private String selectedNote = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_staff, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resultManager = ResultManager.create(requireContext(), GameTypes.STAFF);

        LottieAnimationView successAnim = view.findViewById(R.id.lottie_success);
        LottieAnimationView lossAnim = view.findViewById(R.id.lottie_loss);

        viewModel = new ViewModelProvider(this).get(StaffViewModel.class);
        View playButton = view.findViewById(R.id.play_button);

        LottieAnimationView introAnim = view.findViewById(R.id.lottie_intro);
        AnimationManager.showLottieAnimation(introAnim);

        correctNoteText = view.findViewById(R.id.correct_note);
        wrongNoteText = view.findViewById(R.id.wrong_note);
        targetNoteText = view.findViewById(R.id.random_note);
        timerText = view.findViewById(R.id.time_text);

        StaveView staveView = view.findViewById(R.id.stave_view);

        // Observe the note the player has to guess
        viewModel.getCurrentNote().observe(getViewLifecycleOwner(), note -> {
            targetNoteText.setText(note != null ? note : "-");

            // Automatically play the new note
            playCurrentNote(playButton);
        });

        viewModel.getIsAnswerCorrect().observe(getViewLifecycleOwner(), correct -> {
            resultManager.recordAnswer(correct);
            AnimationManager.showLottieAnimation(correct ? successAnim : lossAnim);
            correctNoteText.setText(String.valueOf(resultManager.getCorrect()));
            wrongNoteText.setText(String.valueOf(resultManager.getIncorrect()));
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

        startGameTimer();
    }

    /** Plays the current note and keeps the play button disabled for a moment. */
    private void playCurrentNote(View playButton) {
        playButton.setEnabled(false);
        viewModel.playCurrentNote();
        playButton.postDelayed(() -> playButton.setEnabled(true), PLAY_BUTTON_COOLDOWN_MS);
    }

    private void startGameTimer() {
        timer = new CountDownTimer(GAME_DURATION_MS, TIMER_TICK_MS) {
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
        int score = resultManager.finish(); // calculates the score and saves it (once)

        Toast.makeText(requireContext(), getString(R.string.end_message, score), Toast.LENGTH_LONG).show();

        // Go to the result screen with the final score
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
