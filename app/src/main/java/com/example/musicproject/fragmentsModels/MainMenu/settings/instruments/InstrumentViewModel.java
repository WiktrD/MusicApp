package com.example.musicproject.fragmentsModels.MainMenu.settings.instruments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicproject.managers.InstrumentSelector;
import com.example.musicproject.managers.SoundManager;

public class InstrumentViewModel extends ViewModel {

    private final InstrumentSelector instrumentSelector;
    private final MutableLiveData<Integer> selectedInstrument =
            new MutableLiveData<>(SoundManager.PROGRAM_GRAND_PIANO);

    public InstrumentViewModel(InstrumentSelector instrumentSelector) {
        this.instrumentSelector = instrumentSelector;
    }

    public LiveData<Integer> getSelectedInstrument() {
        return selectedInstrument;
    }

    public void setInstrument(int programNumber) {
        instrumentSelector.changeInstrument(programNumber);
        selectedInstrument.setValue(programNumber);
    }
}
