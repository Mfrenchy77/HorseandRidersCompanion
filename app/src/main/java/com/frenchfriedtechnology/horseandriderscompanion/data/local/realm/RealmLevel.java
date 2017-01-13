package com.frenchfriedtechnology.horseandriderscompanion.data.local.realm;

import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;
import com.google.firebase.database.Exclude;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Realm Level model
 */
public class RealmLevel extends RealmObject {

    @PrimaryKey
    private long id;

    private String levelName;

    private long skillId;

    private String description;

    private String learningDescription;

    private String completeDescription;

    @Constants.LevelState
    @Exclude
    private int level = Constants.NO_PROGRESS;

    private int position = -1;

    private boolean rider;

    private String lastEditBy;

    private long lastEditDate;

    public String getCompleteDescription() {
        return completeDescription;
    }

    public void setCompleteDescription(String completeDescription) {
        this.completeDescription = completeDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public String getLearningDescription() {
        return learningDescription;
    }

    public void setLearningDescription(String learningDescription) {
        this.learningDescription = learningDescription;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isRider() {
        return rider;
    }

    public void setRider(boolean rider) {
        this.rider = rider;
    }

    public long getSkillId() {
        return skillId;
    }

    public void setSkillId(long skillId) {
        this.skillId = skillId;
    }
}
