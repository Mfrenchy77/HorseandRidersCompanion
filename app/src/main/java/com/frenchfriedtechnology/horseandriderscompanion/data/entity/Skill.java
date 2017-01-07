package com.frenchfriedtechnology.horseandriderscompanion.data.entity;

import android.support.annotation.NonNull;

import org.parceler.Parcel;


@Parcel
public class Skill implements Comparable<Skill> {
    public Skill() {
        //required
    }

    String id;

    String skillName;

    String categoryId;

    String description;

    int position = -1;

    boolean rider;

    String lastEditBy;

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
