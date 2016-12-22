package com.frenchfriedtechnology.horseandriderscompanion.events;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;


public class LevelCreateEvent {
    private boolean edit;
    private Level level = new Level();

    public LevelCreateEvent(Level level,boolean edit) {
        this.level = level;
        this.edit = edit;
    }

    public boolean isEdit() {
        return edit;
    }

    public Level getLevel() {
        return level;
    }
}


