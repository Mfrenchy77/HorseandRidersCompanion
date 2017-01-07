package com.frenchfriedtechnology.horseandriderscompanion.data.local.realm;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Realm Object for RiderProfile
 */


public class RealmRiderProfile extends RealmObject{

    @PrimaryKey
    private String email;

    private String id;

    private String picUrl = null;

    private String name;

    private boolean editor;

    private boolean isSubscribed;

    private long lastEditDate = 0;

    private String lastEditBy = null;

    private long subscriptionDate;

    private long subscriptionEndDate;

    private RealmList<ListItem> ownedHorses = new RealmList<>();

    private RealmList<ListItem> studentHorses = new RealmList<>();

    private RealmList<ListItem> instructors = new RealmList<>();

    private RealmList<ListItem> students = new RealmList<>();

    private RealmList<RealmSkillLevel> skillLevels = new RealmList<>();

    public RealmList<ListItem> getInstructors() {
        return instructors;
    }

    public void setInstructors(RealmList<ListItem> instructors) {
        this.instructors = instructors;
    }

    public RealmList<ListItem> getOwnedHorses() {
        return ownedHorses;
    }

    public void setOwnedHorses(RealmList<ListItem> ownedHorses) {
        this.ownedHorses = ownedHorses;
    }

    public RealmList<ListItem> getStudentHorses() {
        return studentHorses;
    }

    public void setStudentHorses(RealmList<ListItem> studentHorses) {
        this.studentHorses = studentHorses;
    }

    public RealmList<ListItem> getStudents() {
        return students;
    }

    public void setStudents(RealmList<ListItem> students) {
        this.students = students;
    }

    public RealmList<RealmSkillLevel> getSkillLevels() {
        return skillLevels;
    }

    public void setSkillLevels(RealmList<RealmSkillLevel> skillLevels) {
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
