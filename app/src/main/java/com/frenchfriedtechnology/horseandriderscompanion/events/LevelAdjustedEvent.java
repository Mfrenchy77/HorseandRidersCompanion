package com.frenchfriedtechnology.horseandriderscompanion.events;

import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;

public class LevelAdjustedEvent {
    @Constants.LevelState
    private int progress;
    private String levelId;
    private boolean rider;

    public LevelAdjustedEvent(String levelId, @Constants.LevelState int progress, boolean rider) {
        this.progress = progress;
        this.levelId = levelId;
        this.rider = rider;
    }

    public boolean isRider() {
        return rider;
    }

    public String getLevelId() {
        return levelId;
    }

    public int getProgress() {
        return progress;
    }
}
