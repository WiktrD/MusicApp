package com.example.musicproject.fragmentsModels.MainMenu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainMenuViewModel extends ViewModel {
    private final MutableLiveData<Integer> Score = new MutableLiveData<>(0);

    public LiveData<Integer> getUserName() {
        return Score;
    }

}