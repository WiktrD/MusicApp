package com.example.musicproject.managers;

/** Changes the instrument used to play notes (General MIDI program number). */
public interface InstrumentSelector {
    void changeInstrument(int programNumber);
}
