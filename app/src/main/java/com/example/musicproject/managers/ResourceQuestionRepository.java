package com.example.musicproject.managers;

import android.content.Context;
import android.content.res.Resources;

import com.example.musicproject.models.QuizQuestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Reads the quiz questions from strings.xml (quiz_q1, quiz_q1_a, ... quiz_q1_correct). */
public class ResourceQuestionRepository implements QuestionRepository {

    private static final int QUESTION_POOL_SIZE = 15; // quiz_q1 .. quiz_q15 in strings.xml

    private final Context context;

    public ResourceQuestionRepository(Context context) {
        this.context = context;
    }

    @Override
    public List<QuizQuestion> loadQuestions() {
        Resources res = context.getResources();
        String packageName = context.getPackageName();
        List<QuizQuestion> questions = new ArrayList<>();

        for (int i = 1; i <= QUESTION_POOL_SIZE; i++) {
            String q = res.getString(res.getIdentifier("quiz_q" + i, "string", packageName));
            String a = res.getString(res.getIdentifier("quiz_q" + i + "_a", "string", packageName));
            String b = res.getString(res.getIdentifier("quiz_q" + i + "_b", "string", packageName));
            String c = res.getString(res.getIdentifier("quiz_q" + i + "_c", "string", packageName));
            String d = res.getString(res.getIdentifier("quiz_q" + i + "_d", "string", packageName));
            String correctStr = res.getString(res.getIdentifier("quiz_q" + i + "_correct", "string", packageName));
            int correctIndex = Integer.parseInt(correctStr) - 1; // A=1 -> 0 index
            questions.add(new QuizQuestion(q, Arrays.asList(a, b, c, d), correctIndex));
        }
        return questions;
    }
}
