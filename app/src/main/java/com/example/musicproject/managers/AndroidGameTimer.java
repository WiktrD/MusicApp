package com.example.musicproject.managers;

import android.os.CountDownTimer;

/** {@link GameTimer} backed by Android's CountDownTimer (must be started on the main thread). */
public class AndroidGameTimer implements GameTimer {

    private static final long TICK_MS = 1000;

    private CountDownTimer timer;

    @Override
    public void start(long durationMillis, Listener listener) {
        cancel();
        timer = new CountDownTimer(durationMillis, TICK_MS) {
            @Override
            public void onTick(long millisUntilFinished) {
                listener.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                listener.onFinish();
            }
        };
        timer.start();
    }

    @Override
    public void cancel() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
