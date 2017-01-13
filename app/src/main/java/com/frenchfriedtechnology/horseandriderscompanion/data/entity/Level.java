package com.frenchfriedtechnology.horseandriderscompanion.data.entity;

import android.support.annotation.NonNull;

import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

/**
 * Model representing a Level in the Skill Tree
 */

@SuppressWarnings("WeakerAccess")
@IgnoreExtraProperties
@Parcel
public class Level implements Comparable<Level> {

    public Level() {
        //required
    }

    long id;

    String levelName;

    long skillId;

    String description;

    String learningDescription;

    String completeDescription;

    @Constants.LevelState
    @Exclude
    int level = Constants.NO_PROGRESS;

    int position = -1;

    boolean rider;

    String lastEditBy;

    long lastEditDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public boolean isRider() {/**/
        return rider;
    }

    public void setRider(boolean rider) {
        this.rider = rider;
    }

    public String getCompleteDescription() {
        return completeDescription;
    }

    public void setCompleteDescription(String completeDescription) {
        this.completeDescription = completeDescription;
    }

    public String getLearningDescription() {
        return learningDescription;
    }

    public void setLearningDescription(String learningDescription) {
        this.learningDescription = learningDescription;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Exclude
    @Constants.LevelState
    public int getLevel() {
        return level;
    }

    @Exclude
    public void setLevel(@Constants.LevelState int level) {
        this.level = level;
    }

    public long getSkillId() {
        return skillId;
    }

    public void setSkillId(long skillId) {
        this.skillId = skillId;
    }

    @Override
    public int compareTo(@NonNull Level level) {
        if (this.getPosition() > level.getPosition())
            return 1;
        else if (this.getPosition() < level.getPosition())
            return -1;
        else return 0;
    }
}
