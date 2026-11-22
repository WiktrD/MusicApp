package com.example.musicproject.fragmentsModels.Game.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.example.musicproject.AppViewModelFactory;
import com.example.musicproject.R;
import com.example.musicproject.fragmentsModels.Game.GameResultFragment;
import com.example.musicproject.managers.AnimationManager;
import com.example.musicproject.managers.ScreenNavigator;

public class QuizFragment extends Fragment {

    private QuizViewModel viewModel;
    private TextView questionText;
    private RadioGroup answerGroup;
    private RadioButton answer1, answer2, answer3, answer4;
    private Button nextButton;
    private int selectedAnswerIndex = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, new AppViewModelFactory(requireContext()))
                .get(QuizViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        LottieAnimationView introAnim = view.findViewById(R.id.lottie_intro);
        AnimationManager.showLottieAnimation(introAnim);

        questionText = view.findViewById(R.id.question_text);
        answerGroup = view.findViewById(R.id.answer_group);
        answer1 = view.findViewById(R.id.answer_1);
        answer2 = view.findViewById(R.id.answer_2);
        answer3 = view.findViewById(R.id.answer_3);
        answer4 = view.findViewById(R.id.answer_4);
        nextButton = view.findViewById(R.id.next_button);

        viewModel.getCurrentQuestion().observe(getViewLifecycleOwner(), question -> {
            if (question == null) return;

            questionText.setText(question.getQuestion());
            answer1.setText(question.getOptions().get(0));
            answer2.setText(question.getOptions().get(1));
            answer3.setText(question.getOptions().get(2));
            answer4.setText(question.getOptions().get(3));

            answerGroup.clearCheck();
            selectedAnswerIndex = -1;
        });

        viewModel.getAnswerResult().observe(getViewLifecycleOwner(), event -> {
            Boolean isCorrect = event.getContentIfNotHandled();
            if (isCorrect == null) return;
            int messageId = isCorrect ? R.string.correct_answer : R.string.incorrect_answer;
            Toast.makeText(getContext(), getString(messageId), Toast.LENGTH_SHORT).show();
        });

        viewModel.getQuizFinished().observe(getViewLifecycleOwner(), event -> {
            Integer score = event.getContentIfNotHandled();
            if (score != null) {
                ScreenNavigator.open(requireActivity().getSupportFragmentManager(),
                        GameResultFragment.newInstance(score));
            }
        });

        answerGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.answer_1) selectedAnswerIndex = 0;
            else if (checkedId == R.id.answer_2) selectedAnswerIndex = 1;
            else if (checkedId == R.id.answer_3) selectedAnswerIndex = 2;
            else if (checkedId == R.id.answer_4) selectedAnswerIndex = 3;
        });

        nextButton.setOnClickListener(v -> {
            if (selectedAnswerIndex != -1) {
                viewModel.submitAnswer(selectedAnswerIndex);
            } else {
                Toast.makeText(getContext(), getString(R.string.select_answer), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
