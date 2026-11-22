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
import com.example.musicproject.R;
import com.example.musicproject.database.AppDatabase;
import com.example.musicproject.database.GameScore;
import com.example.musicproject.database.GameTypes;
import com.example.musicproject.fragmentsModels.Game.GameResultFragment;
import com.example.musicproject.managers.AnimationManager;

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
        viewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        viewModel.loadQuestionsFromResources(requireContext());
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
            if (question == null) {
                saveScoreAndShowResult();
                return;
            }

            questionText.setText(question.getQuestion());
            answer1.setText(question.getOptions().get(0));
            answer2.setText(question.getOptions().get(1));
            answer3.setText(question.getOptions().get(2));
            answer4.setText(question.getOptions().get(3));

            answerGroup.clearCheck();
            selectedAnswerIndex = -1;
        });

        viewModel.getIsAnswerCorrect().observe(getViewLifecycleOwner(), isCorrect -> {
            if (isCorrect == null) return;
            if (isCorrect) {
                Toast.makeText(getContext(), getString(R.string.correct_answer), Toast.LENGTH_SHORT).show();
                viewModel.loadNextQuestion();
            } else {
                Toast.makeText(getContext(), getString(R.string.incorrect_answer), Toast.LENGTH_SHORT).show();
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

    private void saveScoreAndShowResult() {
        int score = viewModel.finishQuiz();
        int correct = viewModel.getCorrectCount();
        int incorrect = viewModel.getIncorrectCount();

        GameScore gameScore = new GameScore(
                0,
                GameTypes.QUIZ,
                score,
                correct,
                incorrect,
                System.currentTimeMillis()
        );
        AppDatabase db = AppDatabase.Companion.getInstance(requireContext());
        new Thread(() -> db.scoreDao().insertScoreSync(gameScore)).start();

        // Go to the result screen
        Bundle bundle = new Bundle();
        bundle.putInt("score", score);
        bundle.putInt("correctCount", correct);
        bundle.putInt("incorrectCount", incorrect);

        GameResultFragment resultFragment = new GameResultFragment();
        resultFragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, resultFragment)
                .addToBackStack(null)
                .commit();
    }
}
