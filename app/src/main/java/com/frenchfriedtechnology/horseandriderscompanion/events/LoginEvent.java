package com.frenchfriedtechnology.horseandriderscompanion.events;

import android.support.annotation.Nullable;

public class LoginEvent {
    private final boolean success;
    @Nullable
    private final String message;
    private String email;

    public LoginEvent(boolean success, @Nullable String message, String email) {
        this.success = success;
        this.email = email;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public boolean isSuccess() {
        return success;
    }

    @Nullable
    public String getMessage() {
        return message;
    }
}
