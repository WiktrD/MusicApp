package com.example.musicproject.managers;

import com.example.musicproject.models.QuizQuestion;

import java.util.List;

/** Source of quiz questions (the whole pool, in a fixed order). */
public interface QuestionRepository {
    List<QuizQuestion> loadQuestions();
}
