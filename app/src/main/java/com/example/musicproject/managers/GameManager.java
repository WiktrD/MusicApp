package com.example.musicproject.managers;

import java.util.Random;

public class GameManager {

    private final String[] notes = {"C", "D", "E", "F", "G", "A", "B"};

    public String generateRandomNote() {
        int index = new Random().nextInt(notes.length);
        return notes[index];
    }

    public boolean checkNoteAnswer(String correctNote, String userAnswer) {
        return correctNote != null && correctNote.equalsIgnoreCase(userAnswer);
    }
    public String generateQuizQuestion() {
        return "What is the note of this symbol?"; // przykład
    }
    public boolean checkQuizAnswer(String answer) {
        return answer.equalsIgnoreCase("C"); // przykładowa odpowiedź
    }
}
