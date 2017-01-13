package com.frenchfriedtechnology.horseandriderscompanion.data.entity;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Model of Resource
 */
@Parcel
public class Resource {
    public Resource() {
        //required
    }

    long id;

    String name;

    String thumbnail;

    String description;

    String url;

    long numberOfRates = 0;

    long rating = 0;

    List<BaseListItem> skillTreeIds = new ArrayList<>();

    List<BaseListItem> usersWhoRated = new ArrayList<>();

    String lastEditBy;

    long lastEditDate;

    public List<BaseListItem> getUsersWhoRated() {
        return usersWhoRated;
    }

    public void setUsersWhoRated(List<BaseListItem> usersWhoRated) {
        this.usersWhoRated = usersWhoRated;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNumberOfRates() {
        return numberOfRates;
    }

    public void setNumberOfRates(long numberOfRates) {
        this.numberOfRates = numberOfRates;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<BaseListItem> getSkillTreeIds() {
        return skillTreeIds;
    }

    public void setSkillTreeIds(List<BaseListItem> skillTreeIds) {
        this.skillTreeIds = skillTreeIds;
    }
}
