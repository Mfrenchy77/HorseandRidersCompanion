package com.frenchfriedtechnology.horseandriderscompanion.data.entity;


import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Main model for Rider Profile
 */

@IgnoreExtraProperties
@Parcel
public class RiderProfile {

    public RiderProfile() {
        //required
    }

    String id;

    String picUrl; //maybe get rid of this

    String name;

    String email;

    boolean editor;

    boolean isSubscribed;

    long lastEditDate = 0;

    String lastEditBy = null;

    long subscriptionDate;

    long subscriptionEndDate;

    HashMap<String, SkillLevel> skillLevels = new HashMap<>(0);

    List<String> ownedHorses = new ArrayList<>(0);

    List<String> studentHorses = new ArrayList<>(0);

    List<String> students = new ArrayList<>(0);

    List<String> instructors = new ArrayList<>(0);

    public List<String> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<String> instructors) {
        this.instructors = instructors;
    }

    public List<String> getOwnedHorses() {
        return ownedHorses;
    }

    public void setOwnedHorses(List<String> ownedHorses) {
        this.ownedHorses = ownedHorses;
    }

    public List<String> getStudentHorses() {
        return studentHorses;
    }

    public void setStudentHorses(List<String> studentHorses) {
        this.studentHorses = studentHorses;
    }

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(List<String> students) {
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