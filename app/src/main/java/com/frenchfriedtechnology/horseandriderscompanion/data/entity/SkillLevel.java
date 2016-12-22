package com.frenchfriedtechnology.horseandriderscompanion.data.entity;

import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Model of a SkillLevel for Horse/Rider profile
 */
@Parcel
public class SkillLevel implements Serializable {

    public SkillLevel() {
        //required
    }

    String levelId;

    String lastEditBy;

    long lastEditDate;

    @Constants.LevelState
    int level;

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

    @Constants.LevelState
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
