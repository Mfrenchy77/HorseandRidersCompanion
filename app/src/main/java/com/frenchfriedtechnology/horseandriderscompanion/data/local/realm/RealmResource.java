package com.frenchfriedtechnology.horseandriderscompanion.data.local.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Realm Resource Model
 */
public class RealmResource extends RealmObject {

    @PrimaryKey
    private String id;

    private String name;

    private String thumbnail;

    private String description;

    private String url;

    private long numberOfRates;

    private long rating;

    private RealmList<ListItem> categoryIds;

    private RealmList<ListItem> skillIds;

    private RealmList<ListItem> levelIds;

    private String lastEditBy;

    private long lastEditDate;

    public RealmList<ListItem> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(RealmList<ListItem> categoryIds) {
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

    public RealmList<ListItem> getLevelIds() {
        return levelIds;
    }

    public void setLevelIds(RealmList<ListItem> levelIds) {
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

    public RealmList<ListItem> getSkillIds() {
        return skillIds;
    }

    public void setSkillIds(RealmList<ListItem> skillIds) {
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
