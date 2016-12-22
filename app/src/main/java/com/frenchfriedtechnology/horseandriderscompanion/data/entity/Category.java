package com.frenchfriedtechnology.horseandriderscompanion.data.entity;

import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;


/**
 * Model for Skill Tree Category
 */

@Parcel
@IgnoreExtraProperties
public class Category implements Comparable<Category> {

    public Category() {
        //required
    }

    String id;

    String name;

    String description;

    String lastEditBy;

    long lastEditDate;

    int position = -1;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(@NonNull Category category) {
        if (this.getPosition() > category.getPosition())
            return 1;
        else if (this.getPosition() < category.getPosition())
            return -1;
        else return 0;
    }
}
