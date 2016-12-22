package com.frenchfriedtechnology.horseandriderscompanion.events;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;

public class LevelEditEvent {
    private Level level;

    public LevelEditEvent(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }
}
