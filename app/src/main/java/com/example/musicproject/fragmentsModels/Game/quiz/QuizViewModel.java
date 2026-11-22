package com.example.musicproject.fragmentsModels.Game.quiz;

import android.content.Context;
import android.content.res.Resources;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicproject.database.GameTypes;
import com.example.musicproject.managers.ResultManager;
import com.example.musicproject.models.QuizQuestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizViewModel extends ViewModel {

    private static final int QUESTION_POOL_SIZE = 15;  // quiz_q1 .. quiz_q15 in strings.xml
    private static final int QUESTIONS_PER_GAME = 5;

    private final MutableLiveData<QuizQuestion> currentQuestion = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isAnswerCorrect = new MutableLiveData<>();
    private int currentQuestionIndex = 0;

    private final List<QuizQuestion> questionList = new ArrayList<>();

    private int correctCount = 0;
    private int incorrectCount = 0;
    private ResultManager resultManager;

    public void loadQuestionsFromResources(Context context) {
        questionList.clear();
        Resources res = context.getResources();
        String packageName = context.getPackageName();
        int numberOfQuestions = QUESTION_POOL_SIZE;

        for (int i = 1; i <= numberOfQuestions; i++) {
            String q = res.getString(res.getIdentifier("quiz_q" + i, "string", packageName));
            String a = res.getString(res.getIdentifier("quiz_q" + i + "_a", "string", packageName));
            String b = res.getString(res.getIdentifier("quiz_q" + i + "_b", "string", packageName));
            String c = res.getString(res.getIdentifier("quiz_q" + i + "_c", "string", packageName));
            String d = res.getString(res.getIdentifier("quiz_q" + i + "_d", "string", packageName));
            String correctStr = res.getString(res.getIdentifier("quiz_q" + i + "_correct", "string", packageName));
            int correctIndex = Integer.parseInt(correctStr) - 1; // A=1 -> 0 index
            questionList.add(new QuizQuestion(q, Arrays.asList(a, b, c, d), correctIndex));
        }
        Collections.shuffle(questionList); // random question order
        if (questionList.size() > QUESTIONS_PER_GAME) {
            questionList.subList(QUESTIONS_PER_GAME, questionList.size()).clear();
        }

        resultManager = ResultManager.create(context, GameTypes.QUIZ);
        currentQuestionIndex = 0;
        loadQuestion();
    }

    public LiveData<QuizQuestion> getCurrentQuestion() {
        return currentQuestion;
    }

    public LiveData<Boolean> getIsAnswerCorrect() {
        return isAnswerCorrect;
    }

    public int getCorrectCount() { return resultManager.getCorrect(); }
    public int getIncorrectCount() { return resultManager.getIncorrect(); }
    public int finishQuiz() { return resultManager.finish(); }

    public void submitAnswer(int selectedIndex) {
        QuizQuestion question = currentQuestion.getValue();
        if (question != null) {
            boolean isCorrect = question.getCorrectIndex() == selectedIndex;
            isAnswerCorrect.setValue(isCorrect);
            if (isCorrect) {
                correctCount++;
            } else {
                incorrectCount++;
            }
        }
    }

    public void loadNextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questionList.size()) {
            loadQuestion();
        } else {
            // quiz finished
            currentQuestion.setValue(null);
        }
    }

    private void loadQuestion() {
        currentQuestion.setValue(questionList.get(currentQuestionIndex));
        isAnswerCorrect.setValue(null); // reset
    }
}
