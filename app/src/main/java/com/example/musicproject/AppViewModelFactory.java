package com.example.musicproject;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicproject.database.AppDatabase;
import com.example.musicproject.database.GameTypes;
import com.example.musicproject.fragmentsModels.Game.piano.PianoViewModel;
import com.example.musicproject.fragmentsModels.Game.quiz.QuizViewModel;
import com.example.musicproject.fragmentsModels.Game.staff.StaffViewModel;
import com.example.musicproject.fragmentsModels.MainMenu.score.ScoreViewModel;
import com.example.musicproject.fragmentsModels.MainMenu.settings.instruments.InstrumentViewModel;
import com.example.musicproject.managers.AndroidGameTimer;
import com.example.musicproject.managers.NoteChallenge;
import com.example.musicproject.managers.ResourceQuestionRepository;
import com.example.musicproject.managers.ResultManager;
import com.example.musicproject.managers.SoundManager;

import java.util.Random;

/**
 * Composition root: the only place that decides which concrete classes
 * (SoundManager, Room, Android timer) the ViewModels get.
 * Usage: new ViewModelProvider(this, new AppViewModelFactory(requireContext())).get(X.class)
 */
public class AppViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public AppViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        ViewModel viewModel;

        if (modelClass == PianoViewModel.class) {
            viewModel = new PianoViewModel(
                    SoundManager.getInstance(),
                    ResultManager.create(context, GameTypes.PIANO),
                    new AndroidGameTimer(),
                    new Random());
        } else if (modelClass == StaffViewModel.class) {
            viewModel = new StaffViewModel(
                    SoundManager.getInstance(),
                    ResultManager.create(context, GameTypes.STAFF),
                    new NoteChallenge(),
                    new AndroidGameTimer());
        } else if (modelClass == QuizViewModel.class) {
            viewModel = new QuizViewModel(
                    new ResourceQuestionRepository(context),
                    ResultManager.create(context, GameTypes.QUIZ),
                    new Random());
        } else if (modelClass == InstrumentViewModel.class) {
            viewModel = new InstrumentViewModel(SoundManager.getInstance());
        } else if (modelClass == ScoreViewModel.class) {
            viewModel = new ScoreViewModel(
                    AppDatabase.Companion.getInstance(context).scoreDao(),
                    runnable -> new Thread(runnable).start());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
        }

        return modelClass.cast(viewModel);
    }
}
