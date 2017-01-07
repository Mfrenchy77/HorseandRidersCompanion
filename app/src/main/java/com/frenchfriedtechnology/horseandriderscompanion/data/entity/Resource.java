package com.frenchfriedtechnology.horseandriderscompanion.data.entity;

import org.parceler.Parcel;

import java.util.List;

/**
 * Model of Resource
 */
@Parcel
public class Resource {
    public Resource() {
        //required
    }

    String id;

    String name;

    String thumbnail;

    String description;

    String url;

    long numberOfRates;

    long rating;

    List<BaseListItem> categoryIds;

    List<BaseListItem> skillIds;

    List<BaseListItem> levelIds;

    String lastEditBy;

    long lastEditDate;

    public List<BaseListItem> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<BaseListItem> categoryIds) {
        this.categoryIds = categoryIds;
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

    public List<BaseListItem> getLevelIds() {
        return levelIds;
    }

    public void setLevelIds(List<BaseListItem> levelIds) {
        this.levelIds = levelIds;
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

    public List<BaseListItem> getSkillIds() {
        return skillIds;
    }

    public void setSkillIds(List<BaseListItem> skillIds) {
        this.skillIds = skillIds;
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
}
