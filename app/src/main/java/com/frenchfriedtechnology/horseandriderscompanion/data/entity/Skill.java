package com.frenchfriedtechnology.horseandriderscompanion.data.entity;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.parceler.Parcel;

@Parcel
public class Skill implements Comparable<Skill> {
    public Skill() {
        //required
    }

    String skillName;

    String id;

    String categoryId;

    String description;

    int position = -1;

    String lastEditedBy;

    long lastEditDate;


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public long getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(long lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public String getLastEditedBy() {
        return lastEditedBy;
    }

    public void setLastEditedBy(String lastEditedBy) {
        this.lastEditedBy = lastEditedBy;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    @Override
    public int compareTo(@NonNull Skill skill) {
        if (this.getPosition() > skill.getPosition())
            return 1;
        else if (this.getPosition() < skill.getPosition())
            return -1;
        else return 0;
    }

}
