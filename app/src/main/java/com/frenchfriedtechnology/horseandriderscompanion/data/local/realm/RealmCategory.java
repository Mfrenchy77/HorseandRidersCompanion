package com.frenchfriedtechnology.horseandriderscompanion.data.local.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Realm Category
 */
public class RealmCategory extends RealmObject {


    @PrimaryKey
    private long id;

    private String name;

    private String description;

    private int position = -1;

    private boolean rider;

    private String lastEditBy;

    private long lastEditDate = 0;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
