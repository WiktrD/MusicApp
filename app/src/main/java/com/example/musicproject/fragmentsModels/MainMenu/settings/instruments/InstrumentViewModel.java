package com.example.musicproject.fragmentsModels.MainMenu.settings.instruments;

import androidx.lifecycle.ViewModel;
import com.example.musicproject.managers.SoundManager;


public class InstrumentViewModel extends ViewModel {
    private final SoundManager soundManager = SoundManager.getInstance();


    public void setInstrument(int programNumber) {
        soundManager.changeInstrument(programNumber);
    }

}