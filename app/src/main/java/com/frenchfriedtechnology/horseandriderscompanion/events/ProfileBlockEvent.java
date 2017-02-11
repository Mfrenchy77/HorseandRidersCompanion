package com.frenchfriedtechnology.horseandriderscompanion.events;

public class ProfileBlockEvent {
    String email;

    public ProfileBlockEvent(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
