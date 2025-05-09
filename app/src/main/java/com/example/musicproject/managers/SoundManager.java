package com.example.musicproject.managers;

import org.billthefarmer.mididriver.MidiDriver;
public class SoundManager {
    private final MidiDriver midiDriver;

    public SoundManager() {
        midiDriver = MidiDriver.getInstance();
        midiDriver.start();
    }

    public void playNote(String note) {
        int noteValue = convertNoteToMidiValue(note);
        if (noteValue != -1) {
            byte[] event = new byte[3];
            event[0] = (byte) 0x90; // Note On
            event[1] = (byte) noteValue;
            event[2] = (byte) 100; // Velocity
            midiDriver.write(event);
        }
    }

    private int convertNoteToMidiValue(String note) {
        switch (note.toUpperCase()) {
            case "C": return 60;
            case "C#": return 61;
            case "D": return 62;
            case "D#": return 63;
            case "E": return 64;
            case "F": return 65;
            case "F#": return 66;
            case "G": return 67;
            case "G#": return 68;
            case "A": return 69;
            case "A#": return 70;
            case "B": return 71;
            default: return -1;
        }
    }

    public void stop() {
        midiDriver.stop();
    }
}
