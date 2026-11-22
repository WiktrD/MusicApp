package com.example.musicproject.models;

import androidx.annotation.Nullable;

/**
 * Wraps a value that must be handled only once (e.g. "game finished"),
 * so it is not delivered again when the screen is recreated.
 */
public class Event<T> {

    private final T content;
    private boolean handled = false;

    public Event(T content) {
        this.content = content;
    }

    /** Returns the content the first time it is called, null afterwards. */
    @Nullable
    public T getContentIfNotHandled() {
        if (handled) return null;
        handled = true;
        return content;
    }
}
