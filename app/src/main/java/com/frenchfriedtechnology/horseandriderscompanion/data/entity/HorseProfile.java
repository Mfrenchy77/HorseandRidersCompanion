package com.frenchfriedtechnology.horseandriderscompanion.data.entity;


import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.List;

/**
 * Model for Horse's Profile
 */

@IgnoreExtraProperties
@Parcel
public class HorseProfile {

    public HorseProfile() {
        //required
    }

    long id;

    String name;

    String breed;

    String picUrl;

    long dateOfBirth;

    String age;

    String color;

    String height;

    String currentOwner;

    long dateOfPurchase;

    long purchasePrice;

    HashMap<String, SkillLevel> skillLevels = new HashMap<>(0);

    long lastEditDate = 0;

    String lastEditBy = null;

    List<BaseListItem>notes;

    public List<BaseListItem> getNotes() {
        return notes;
    }

    public void setNotes(List<BaseListItem> notes) {
        this.notes = notes;
    }

    public HashMap<String, SkillLevel> getSkillLevels() {
        return skillLevels;
    }

    public void setSkillLevels(HashMap<String, SkillLevel> skillLevels) {
        this.skillLevels = skillLevels;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCurrentOwner() {
        return currentOwner;
    }

    public void setCurrentOwner(String currentOwner) {
        this.currentOwner = currentOwner;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public long getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(long dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public long getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(long purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
}
