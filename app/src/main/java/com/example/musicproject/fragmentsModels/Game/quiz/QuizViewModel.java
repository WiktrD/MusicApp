package com.example.musicproject.fragmentsModels.Game.quiz;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicproject.managers.QuestionRepository;
import com.example.musicproject.managers.ResultManager;
import com.example.musicproject.models.Event;
import com.example.musicproject.models.QuizQuestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizViewModel extends ViewModel {

    private static final int QUESTIONS_PER_GAME = 5;

    private final ResultManager resultManager;
    private final List<QuizQuestion> questions;
    private int currentQuestionIndex = 0;

    private final MutableLiveData<QuizQuestion> currentQuestion = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> answerResult = new MutableLiveData<>();
    private final MutableLiveData<Event<Integer>> quizFinished = new MutableLiveData<>();

    public QuizViewModel(QuestionRepository repository, ResultManager resultManager, Random random) {
        this.resultManager = resultManager;

        List<QuizQuestion> pool = new ArrayList<>(repository.loadQuestions());
        Collections.shuffle(pool, random); // random question order
        int count = Math.min(QUESTIONS_PER_GAME, pool.size());
        this.questions = new ArrayList<>(pool.subList(0, count));

        if (questions.isEmpty()) {
            quizFinished.setValue(new Event<>(resultManager.finish()));
        } else {
            currentQuestion.setValue(questions.get(0));
        }
    }

    public void submitAnswer(int selectedIndex) {
        QuizQuestion question = currentQuestion.getValue();
        if (question == null || resultManager.isFinished()) return;

        boolean isCorrect = question.getCorrectIndex() == selectedIndex;
        resultManager.recordAnswer(isCorrect);
        answerResult.setValue(new Event<>(isCorrect));

        if (isCorrect) {
            showNextQuestion();
        }
    }

    private void showNextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            currentQuestion.setValue(questions.get(currentQuestionIndex));
        } else {
            quizFinished.setValue(new Event<>(resultManager.finish()));
        }
    }

    public LiveData<QuizQuestion> getCurrentQuestion() {
        return currentQuestion;
    }

    /** Emits true/false once per submitted answer. */
    public LiveData<Event<Boolean>> getAnswerResult() {
        return answerResult;
    }

    /** Emits the final score once, after the last question. */
    public LiveData<Event<Integer>> getQuizFinished() {
        return quizFinished;
    }
}
