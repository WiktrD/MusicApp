package com.example.musicproject.fragmentsModels.Game.quiz;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicproject.R;

public class quizFragment extends Fragment {

    private QuizViewModel mViewModel;
    private TextView questionText;
    private RadioGroup answerGroup;
    private RadioButton answer1, answer2, answer3, answer4;
    private Button nextButton;
    private int selectedAnswerIndex = -1;

    public static quizFragment newInstance() {
        return new quizFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        // TODO: Use the ViewModel
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        questionText = view.findViewById(R.id.question_text);
        answerGroup = view.findViewById(R.id.answer_group);
        answer1 = view.findViewById(R.id.answer_1);
        answer2 = view.findViewById(R.id.answer_2);
        answer3 = view.findViewById(R.id.answer_3);
        answer4 = view.findViewById(R.id.answer_4);
        nextButton = view.findViewById(R.id.next_button);

        mViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        mViewModel.getCurrentQuestion().observe(getViewLifecycleOwner(), question -> {
            if (question == null) {
                questionText.setText("Quiz zakończony!");
                answerGroup.setVisibility(View.GONE);
                nextButton.setEnabled(false);
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

        mViewModel.getIsAnswerCorrect().observe(getViewLifecycleOwner(), isCorrect -> {
            if (isCorrect == null) return;
            if (isCorrect) {
                Toast.makeText(getContext(), "✅ Dobrze!", Toast.LENGTH_SHORT).show();
                mViewModel.loadNextQuestion();
            } else {
                Toast.makeText(getContext(), "❌ Błąd, spróbuj ponownie", Toast.LENGTH_SHORT).show();
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
                mViewModel.submitAnswer(selectedAnswerIndex);
            } else {
                Toast.makeText(getContext(), "Wybierz odpowiedź", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    }


