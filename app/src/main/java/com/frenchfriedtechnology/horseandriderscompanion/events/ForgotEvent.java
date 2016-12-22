package com.frenchfriedtechnology.horseandriderscompanion.events;

import android.support.annotation.Nullable;


public class ForgotEvent {
    @Nullable
    private final boolean success;
    @Nullable
    private final String message;

    public ForgotEvent(boolean success, @Nullable String message) {
        this.success = success;
        this.message = message;
    }

    @Nullable
    public boolean isSuccess() {
        return success;
    }

    @Nullable
    public String getMessage() {
        return message;
    }
}
