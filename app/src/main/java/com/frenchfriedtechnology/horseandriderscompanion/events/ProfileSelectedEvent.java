package com.frenchfriedtechnology.horseandriderscompanion.events;

public class ProfileSelectedEvent {
    private String email;

    public ProfileSelectedEvent(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
