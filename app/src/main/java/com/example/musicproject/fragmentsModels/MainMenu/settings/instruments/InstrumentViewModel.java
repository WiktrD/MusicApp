package com.example.musicproject.fragmentsModels.MainMenu.settings.instruments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicproject.managers.SoundManager;

public class InstrumentViewModel extends ViewModel {
    private final SoundManager soundManager = SoundManager.getInstance();
    private final MutableLiveData<Integer> selectedInstrument =
            new MutableLiveData<>(SoundManager.PROGRAM_GRAND_PIANO);

    public LiveData<Integer> getSelectedInstrument() {
        return selectedInstrument;
    }

    public void setInstrument(int programNumber) {
        soundManager.changeInstrument(programNumber);
        selectedInstrument.setValue(programNumber);
    }
}
