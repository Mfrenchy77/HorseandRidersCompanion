package com.frenchfriedtechnology.horseandriderscompanion.events;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;

import java.util.List;

public class LevelsFetch {
    private List<Level> levels;

    public LevelsFetch(List<Level> levels) {
        this.levels = levels;
    }

    public List<Level> getLevels() {
        return levels;
    }
}
