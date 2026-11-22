package com.example.musicproject.managers;

import java.util.Random;

/** Picks a random note name for the staff game and checks the player's answer. */
public class NoteChallenge {

    private static final String[] NOTE_NAMES = {"C", "D", "E", "F", "G", "A", "B"};

    private final Random random;

    public NoteChallenge() {
        this(new Random());
    }

    public NoteChallenge(Random random) {
        this.random = random;
    }

    public String generateRandomNote() {
        return NOTE_NAMES[random.nextInt(NOTE_NAMES.length)];
    }

    public boolean checkNoteAnswer(String correctNote, String userAnswer) {
        return correctNote != null && correctNote.equalsIgnoreCase(userAnswer);
    }
}
