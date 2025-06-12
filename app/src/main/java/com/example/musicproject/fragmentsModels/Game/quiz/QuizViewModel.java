package com.example.musicproject.fragmentsModels.Game.quiz;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicproject.managers.GameManager;

public class QuizViewModel extends ViewModel {

        private final MutableLiveData<String> question = new MutableLiveData<>();
        private final MutableLiveData<Boolean> isAnswerCorrect = new MutableLiveData<>();
        private final GameManager gameManager;

        public QuizViewModel() {
            gameManager = new GameManager();
            loadNewQuestion();
        }

        public LiveData<String> getQuestion() { return question; }
        public LiveData<Boolean> getIsAnswerCorrect() { return isAnswerCorrect; }

        public void loadNewQuestion() {
            question.setValue(gameManager.generateQuizQuestion());
        }

        public void submitAnswer(String answer) {
            boolean correct = gameManager.checkQuizAnswer(answer);
            isAnswerCorrect.setValue(correct);
            if (correct) loadNewQuestion();
        }


}