package com.frenchfriedtechnology.horseandriderscompanion.events;

public class HorseProfileDeleteEvent {
    private String id;

    public HorseProfileDeleteEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
