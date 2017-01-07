package com.frenchfriedtechnology.horseandriderscompanion.data.entity;


import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.ListItem;
import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;


/**
 * Main model for Rider Profile
 */

@IgnoreExtraProperties
@Parcel
public class RiderProfile {

    public RiderProfile() {
        //required
    }

    String picUrl; //maybe get rid of this

    String name;

    String id;
    long lastEditDate = 0;

    String lastEditBy = null;

    String email;

    boolean editor;

    boolean isSubscribed;

    long subscriptionDate;

    long subscriptionEndDate;

    HashMap<String, SkillLevel> skillLevels = new HashMap<>(0);

    List<BaseListItem> ownedHorses = new ArrayList<>(0);

    List<BaseListItem> studentHorses = new ArrayList<>(0);

    List<BaseListItem> students = new ArrayList<>(0);

    List<BaseListItem> instructors = new ArrayList<>(0);

    public List<BaseListItem> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<BaseListItem> instructors) {
        this.instructors = instructors;
    }

    public List<BaseListItem> getOwnedHorses() {
        return ownedHorses;
    }

    public void setOwnedHorses(List<BaseListItem> ownedHorses) {
        this.ownedHorses = ownedHorses;
    }

    public List<BaseListItem> getStudentHorses() {
        return studentHorses;
    }

    public void setStudentHorses(List<BaseListItem> studentHorses) {
        this.studentHorses = studentHorses;
    }

    public List<BaseListItem> getStudents() {
        return students;
    }

    public void setStudents(List<BaseListItem> students) {
        this.students = students;
    }

    public HashMap<String, SkillLevel> getSkillLevels() {
        return skillLevels;
    }

    public void setSkillLevels(HashMap<String, SkillLevel> skillLevels) {
        this.skillLevels = skillLevels;
    }

    public boolean isEditor() {
        return editor;
    }

    public void setEditor(boolean editor) {
        this.editor = editor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public long getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(long subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public long getSubscriptionEndDate() {
        return subscriptionEndDate;
    }

    public void setSubscriptionEndDate(long subscriptionEndDate) {
        this.subscriptionEndDate = subscriptionEndDate;
    }
}