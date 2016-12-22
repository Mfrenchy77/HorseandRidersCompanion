package com.frenchfriedtechnology.horseandriderscompanion.events;

import android.support.annotation.Nullable;

public class RegisterEvent {
    @Nullable
    private final boolean success;
    @Nullable
    private final String message;
    @Nullable
    private String riderName;

    public RegisterEvent(boolean mSuccess, @Nullable String message, String riderName) {
        this.success = mSuccess;
        this.message = message;
        this.riderName = riderName;
    }

    public String getRiderName() {
        return riderName;
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

