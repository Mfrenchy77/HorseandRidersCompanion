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

    private RealmList<ListItem> skillTreeIds;

    private String lastEditBy;

    private long lastEditDate;


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

    public RealmList<ListItem> getSkillTreeIds() {
        return skillTreeIds;
    }

    public void setSkillTreeIds(RealmList<ListItem> skillTreeIds) {
        this.skillTreeIds = skillTreeIds;
    }
}
