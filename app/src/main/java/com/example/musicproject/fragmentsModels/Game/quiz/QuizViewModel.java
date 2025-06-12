package com.example.musicproject.fragmentsModels.Game.quiz;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicproject.managers.GameManager;
import com.example.musicproject.models.QuizQuestion;

import java.util.Arrays;
import java.util.List;

public class QuizViewModel extends ViewModel {

        private final MutableLiveData<QuizQuestion> currentQuestion = new MutableLiveData<>();
        private final MutableLiveData<Boolean> isAnswerCorrect = new MutableLiveData<>();
        private int currentQuestionIndex = 0;


    private final List<QuizQuestion> questionList = Arrays.asList(
            new QuizQuestion("Ile półtonów ma oktawa?", Arrays.asList("5", "7", "8", "12"), 3),
            new QuizQuestion("Która nuta ma najwyższą częstotliwość?", Arrays.asList("C", "E", "G", "B"), 3),
            new QuizQuestion("Ile klawiszy ma pełne pianino?", Arrays.asList("61", "76", "88", "100"), 2)
    );
        public QuizViewModel() {
            loadQuestion();
        }

    public LiveData<QuizQuestion> getCurrentQuestion() {
        return currentQuestion;
    }
    public LiveData<Boolean> getIsAnswerCorrect() {
        return isAnswerCorrect;
    }


    public void submitAnswer(int selectedIndex) {
        QuizQuestion question = currentQuestion.getValue();
        if (question != null) {
            boolean isCorrect = question.getCorrectIndex() == selectedIndex;
            isAnswerCorrect.setValue(isCorrect);
        }
    }
    public void loadNextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questionList.size()) {
            loadQuestion();
        } else {
            // Optional: zakończ quiz
            currentQuestion.setValue(null);
        }
    }

    private void loadQuestion() {
        currentQuestion.setValue(questionList.get(currentQuestionIndex));
        isAnswerCorrect.setValue(null); // reset
    }

}


