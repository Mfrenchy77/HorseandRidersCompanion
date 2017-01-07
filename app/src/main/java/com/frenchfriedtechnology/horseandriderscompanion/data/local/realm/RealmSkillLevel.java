package com.frenchfriedtechnology.horseandriderscompanion.data.local.realm;

import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Realm Object for SkillLevel to be saved in RiderProfile
 */

public class RealmSkillLevel extends RealmObject {

    @PrimaryKey
    private
    String levelId;

    @Constants.LevelState
    private int level;

    private String lastEditBy;

    private long lastEditDate;


    public String getLastEditBy() {
        return lastEditBy;
    }

    public void setLastEditBy(String lastEditBy) {
        this.lastEditBy = lastEditBy;
    }

    public long getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(long lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }
}
