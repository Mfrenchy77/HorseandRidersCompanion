package com.frenchfriedtechnology.horseandriderscompanion.data.entity;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Model for Skill Tree Category
 */

@IgnoreExtraProperties
public class Category implements Comparable<Category>, Parcelable {

    public Category() {
        //required
    }

    private long id;

    private String name;

    private String description;

    private int position = -1;

    private boolean rider;

    private String lastEditBy;

    private long lastEditDate = 0;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRider() {
        return rider;
    }

    public void setRider(boolean rider) {
        this.rider = rider;
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

    @Override
    public int compareTo(@NonNull Category category) {
        if (this.getPosition() > category.getPosition())
            return 1;
        else if (this.getPosition() < category.getPosition())
            return -1;
        else return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeInt(this.position);
        dest.writeByte(this.rider ? (byte) 1 : (byte) 0);
        dest.writeString(this.lastEditBy);
        dest.writeLong(this.lastEditDate);
    }

    protected Category(android.os.Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.description = in.readString();
        this.position = in.readInt();
        this.rider = in.readByte() != 0;
        this.lastEditBy = in.readString();
        this.lastEditDate = in.readLong();
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(android.os.Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
