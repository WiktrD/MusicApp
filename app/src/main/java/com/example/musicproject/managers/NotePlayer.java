package com.example.musicproject.managers;

/** Plays notes. ViewModels depend on this interface, not on the MIDI implementation. */
public interface NotePlayer {
    void playNote(int midiNote);

    void stopNote(int midiNote);

    void playNoteByName(String noteName, int durationMillis);
}
