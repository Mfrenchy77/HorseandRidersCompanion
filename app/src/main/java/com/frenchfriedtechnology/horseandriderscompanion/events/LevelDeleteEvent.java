package com.frenchfriedtechnology.horseandriderscompanion.events;

public class LevelDeleteEvent {

    private String levelId;

    public LevelDeleteEvent(String levelId) {
        this.levelId = levelId;
    }

    public String getLevelId() {
        return levelId;
    }
}
