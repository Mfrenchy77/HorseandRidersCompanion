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

    String levelName;

    String id;

    String skillId;

    String description;

    String learningDescription;

    String completeDescription;

    @Constants.LevelState
    @Exclude
    int level = Constants.NO_PROGRESS;

    String lastEditBy;

    long lastEditDate;

    int position = -1;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    @Exclude
    @Constants.LevelState
    public int getLevel() {
        return level;
    }

    @Exclude
    public void setLevel(@Constants.LevelState int level) {
        this.level = level;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
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
