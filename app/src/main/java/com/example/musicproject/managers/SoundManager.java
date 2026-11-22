package com.example.musicproject.managers;

import android.util.Log;

import org.billthefarmer.mididriver.MidiDriver;

public class SoundManager implements MidiDriver.OnMidiStartListener {

    // General MIDI program numbers used by the instrument settings
    public static final int PROGRAM_GRAND_PIANO = 0;
    public static final int PROGRAM_XYLOPHONE = 13;
    public static final int PROGRAM_CHURCH_ORGAN = 19;

    private static final String TAG = "MIDI";

    // MIDI status bytes (channel 0)
    private static final int STATUS_NOTE_ON = 0x90;
    private static final int STATUS_NOTE_OFF = 0x80;
    private static final int STATUS_PROGRAM_CHANGE = 0xC0;

    private static final int MAX_MIDI_VALUE = 127;
    private static final int MAX_VOLUME_PERCENT = 100;

    private static SoundManager instance;

    private final MidiDriver midi;
    private int volumePercent = MAX_VOLUME_PERCENT;

    public SoundManager() {
        midi = MidiDriver.getInstance(this);
        midi.setOnMidiStartListener(this);
        midi.start();
    }

    public static synchronized SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void playNote(int note) {
        byte[] msg = new byte[3];
        msg[0] = (byte) STATUS_NOTE_ON;
        msg[1] = (byte) note;
        msg[2] = (byte) (MAX_MIDI_VALUE * volumePercent / MAX_VOLUME_PERCENT); // velocity
        midi.write(msg);
        Log.d(TAG, "Note ON: " + note);
    }

    public void stopNote(int note) {
        byte[] msg = new byte[3];
        msg[0] = (byte) STATUS_NOTE_OFF;
        msg[1] = (byte) note;
        msg[2] = (byte) 0;
        midi.write(msg);
        Log.d(TAG, "Note OFF " + note);
    }

    public void changeInstrument(int programNumber) {
        if (programNumber < 0 || programNumber > MAX_MIDI_VALUE) {
            Log.w(TAG, "Invalid instrument number: " + programNumber);
            return;
        }

        byte[] msg = new byte[2];
        msg[0] = (byte) STATUS_PROGRAM_CHANGE;
        msg[1] = (byte) programNumber;
        midi.write(msg);
        Log.d(TAG, "Instrument changed to: " + programNumber);
    }

    @Override
    public void onMidiStart() {
        byte[] msg = new byte[2];
        msg[0] = (byte) STATUS_PROGRAM_CHANGE;
        msg[1] = (byte) PROGRAM_GRAND_PIANO;
        midi.write(msg);
        Log.d(TAG, "Instrument set");
    }

    public void setVolume(int volumePercent) {
        this.volumePercent = volumePercent;
    }

    public void playNoteByName(String noteName, int delayMillis) {
        int midiNote = convertNoteToMidiValue(noteName);
        if (midiNote != -1) {
            playNote(midiNote);

            new android.os.Handler().postDelayed(() -> stopNote(midiNote), delayMillis);
        } else {
            Log.w("SoundManager", "Unknown note: " + noteName);
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
}
