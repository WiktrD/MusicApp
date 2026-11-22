package com.example.musicproject.managers;

/** Countdown used by the timed games. Replaceable in tests. */
public interface GameTimer {

    interface Listener {
        void onTick(long millisLeft);

        void onFinish();
    }

    void start(long durationMillis, Listener listener);

    void cancel();
}
