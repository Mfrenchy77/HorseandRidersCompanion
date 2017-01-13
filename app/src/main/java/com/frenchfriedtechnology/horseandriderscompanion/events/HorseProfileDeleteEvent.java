package com.frenchfriedtechnology.horseandriderscompanion.events;

public class HorseProfileDeleteEvent {
    private long id;

    public HorseProfileDeleteEvent(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
