package com.example.musicproject.fragmentsModels.MainMenu.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isSoundEnabled = new MutableLiveData<>(true);

    public LiveData<Boolean> getIsSoundEnabled() { return isSoundEnabled; }

    public void toggleSound() {
        isSoundEnabled.setValue(!Boolean.TRUE.equals(isSoundEnabled.getValue()));
    }
}