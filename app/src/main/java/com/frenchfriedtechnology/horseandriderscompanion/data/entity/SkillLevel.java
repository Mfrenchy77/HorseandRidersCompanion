package com.frenchfriedtechnology.horseandriderscompanion.data.entity;

import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;
import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Model of a SkillLevel for Horse/Rider profile
 */
@Parcel
@IgnoreExtraProperties
public class SkillLevel implements Serializable {

    public SkillLevel() {
        //required
    }

    String levelId;

    @Constants.LevelState
    int level;

    String name;

    String lastEditBy;

    long lastEditDate;

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Constants.LevelState
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
