package com.frenchfriedtechnology.horseandriderscompanion.events;

import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;

public class LevelAdjustedEvent {
    @Constants.LevelState
    private int progress;
    private long levelId;
    private boolean rider;

    public LevelAdjustedEvent(long levelId, @Constants.LevelState int progress, boolean rider) {
        this.progress = progress;
        this.levelId = levelId;
        this.rider = rider;
    }

    public boolean isRider() {
        return rider;
    }

    public long getLevelId() {
        return levelId;
    }

    public int getProgress() {
        return progress;
    }
}
