package com.example.musicproject.managers;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.musicproject.R;

/** The single place that knows how screens are switched. */
public final class ScreenNavigator {

    private ScreenNavigator() {
    }

    /** Replaces the current screen and adds the transaction to the back stack. */
    public static void open(FragmentManager fragmentManager, Fragment screen) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, screen)
                .addToBackStack(null)
                .commit();
    }
}
