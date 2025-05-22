package com.example.musicproject.managers;

import android.content.Context;
import android.util.Log;

import org.billthefarmer.mididriver.MidiDriver;
public class SoundManager implements MidiDriver.OnMidiStartListener {
    private MidiDriver midi;

    public SoundManager() {
        midi = MidiDriver.getInstance(this);
        midi.setOnMidiStartListener(this);
        midi.start();
    }

    public void playNote(int note) {
        byte[] msg = new byte[3];
        msg[0] = (byte) 0x90; // NOTE_ON na kanale 0
        msg[1] = (byte) note; // numer nuty
        msg[2] = (byte) 127;  // głośność maksymalna
        midi.write(msg);
        Log.d("MIDI", "Note ON: " + note);
    }

    public void stopNote(int note) {
        byte[] msg = new byte[3];
        msg[0] = (byte) 0x80; // NOTE_OFF, channel 0
        msg[1] = (byte) note;
        msg[2] = (byte) 0;
        midi.write(msg);
        Log.d("MIDI", "Note OFF " + note);
    }
    public void stop() {
        if (midi != null) {
            midi.stop();
            Log.d("MIDI", "MIDI stopped.");
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
    public void onMidiStart() {
        byte[] msg = new byte[2];
        msg[0] = (byte) 0xC0; // PROGRAM_CHANGE
        msg[1] = (byte) 0;    // instrument 0 = Grand Piano
        midi.write(msg);
        Log.d("MIDI", "Instrument set");
    }

}
