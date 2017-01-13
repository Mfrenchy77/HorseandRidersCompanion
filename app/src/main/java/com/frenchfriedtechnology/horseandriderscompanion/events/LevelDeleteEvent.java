package com.frenchfriedtechnology.horseandriderscompanion.events;

public class LevelDeleteEvent {

    private long levelId;

    public LevelDeleteEvent(long levelId) {
        this.levelId = levelId;
    }

    public long getLevelId() {
        return levelId;
    }
}
