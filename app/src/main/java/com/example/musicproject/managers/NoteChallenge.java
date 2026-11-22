package com.example.musicproject.managers;

import java.util.Random;

/** Picks a random note name for the staff game and checks the player's answer. */
public class NoteChallenge {

    private static final String[] NOTE_NAMES = {"C", "D", "E", "F", "G", "A", "B"};

    public String generateRandomNote() {
        int index = new Random().nextInt(NOTE_NAMES.length);
        return NOTE_NAMES[index];
    }

    public boolean checkNoteAnswer(String correctNote, String userAnswer) {
        return correctNote != null && correctNote.equalsIgnoreCase(userAnswer);
    }
}
