package com.frenchfriedtechnology.horseandriderscompanion.data.mappers;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.BaseListItem;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.SkillLevel;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.ListItem;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.RealmRiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.RealmSkillLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmList;
import timber.log.Timber;


/**
 * Mapper for converting Realm Profile to Entity and back again
 */

public class RiderProfileMapper {

    public RealmRiderProfile entityToRealm(RiderProfile riderProfile) {
        if (riderProfile == null) {
            return null;
        }
        RealmRiderProfile realmRiderProfile = new RealmRiderProfile();
        //set fields from Entity
        realmRiderProfile.setEmail(riderProfile.getEmail());
        realmRiderProfile.setId(riderProfile.getId());
        realmRiderProfile.setName(riderProfile.getName());
        realmRiderProfile.setPicUrl(riderProfile.getPicUrl());
        realmRiderProfile.setEditor(riderProfile.isEditor());
        realmRiderProfile.setSubscribed(riderProfile.isSubscribed());
        realmRiderProfile.setSubscriptionDate(riderProfile.getSubscriptionDate());
        realmRiderProfile.setSubscriptionEndDate(riderProfile.getSubscriptionEndDate());
        realmRiderProfile.setLastEditBy(riderProfile.getLastEditBy());
        realmRiderProfile.setLastEditDate(riderProfile.getLastEditDate());

        RealmList<ListItem> ownedHorseList = new RealmList<>();
        if (riderProfile.getOwnedHorses() != null) {
            Timber.d("Owned Horse List: " + riderProfile.getOwnedHorses().size());
            for (int i = 0; i < riderProfile.getOwnedHorses().size(); i++) {
                ListItem item = new ListItem(riderProfile.getOwnedHorses().get(i).getId(),
                        riderProfile.getOwnedHorses().get(i).getName());
                ownedHorseList.add(item);
            }
            realmRiderProfile.setOwnedHorses(ownedHorseList);
        }

        RealmList<ListItem> studentHorseList = new RealmList<>();
        if (riderProfile.getStudentHorses().size() != 0) {
            Timber.d("Student Horse List: " + riderProfile.getStudentHorses().size());
            for (int i = 0; i < riderProfile.getOwnedHorses().size(); i++) {
                ListItem item = new ListItem(riderProfile.getStudentHorses().get(i).getId(),
                        riderProfile.getOwnedHorses().get(i).getName());
                studentHorseList.add(item);
            }
            realmRiderProfile.setStudentHorses(studentHorseList);
        }

        RealmList<ListItem> studentRiderList = new RealmList<>();
        if (riderProfile.getStudents().size() != 0) {
            Timber.d("Student List: " + riderProfile.getStudents().size());
            for (int i = 0; i < riderProfile.getOwnedHorses().size(); i++) {
                ListItem item = new ListItem(riderProfile.getStudents().get(i).getId(),
                        riderProfile.getOwnedHorses().get(i).getName());
                studentRiderList.add(item);
            }
            realmRiderProfile.setStudents(studentRiderList);
        }

        RealmList<ListItem> instructorList = new RealmList<>();
        if (riderProfile.getInstructors().size() != 0) {
            Timber.d("Instructor List: " + riderProfile.getInstructors().size());
            for (int i = 0; i < riderProfile.getOwnedHorses().size(); i++) {
                ListItem item = new ListItem(riderProfile.getInstructors().get(i).getId(),
                        riderProfile.getOwnedHorses().get(i).getName());
                instructorList.add(item);
            }
            realmRiderProfile.setInstructors(instructorList);
        }

        RealmList<RealmSkillLevel> skillLevels = new RealmList<>();
        if (riderProfile.getSkillLevels().size() != 0) {
            for (SkillLevel skillLevel : riderProfile.getSkillLevels().values()) {
                RealmSkillLevel realmSkillLevel = new RealmSkillLevel();
                realmSkillLevel.setLevelId(skillLevel.getLevelId());
                realmSkillLevel.setLastEditDate(skillLevel.getLastEditDate());
                realmSkillLevel.setLastEditBy(skillLevel.getLastEditBy());
                realmSkillLevel.setLevel(skillLevel.getLevel());
                skillLevels.add(realmSkillLevel);
            }
            realmRiderProfile.setSkillLevels(skillLevels);
        }
        return realmRiderProfile;
    }

    @SuppressWarnings("Convert2streamapi")
    public RiderProfile realmToEntity(RealmRiderProfile realmRiderProfile) {
        if (realmRiderProfile == null) {
            return null;
        }
        RiderProfile riderProfileEntity = new RiderProfile();
        //set fields from Realm
        riderProfileEntity.setEmail(realmRiderProfile.getEmail());
        riderProfileEntity.setId(realmRiderProfile.getId());
        riderProfileEntity.setName(realmRiderProfile.getName());
        riderProfileEntity.setPicUrl(realmRiderProfile.getPicUrl());
        riderProfileEntity.setEditor(realmRiderProfile.isEditor());
        riderProfileEntity.setSubscribed(realmRiderProfile.isSubscribed());
        riderProfileEntity.setSubscriptionDate(realmRiderProfile.getSubscriptionDate());
        riderProfileEntity.setSubscriptionEndDate(realmRiderProfile.getSubscriptionEndDate());
        riderProfileEntity.setLastEditBy(realmRiderProfile.getLastEditBy());
        riderProfileEntity.setLastEditDate(realmRiderProfile.getLastEditDate());

        List<BaseListItem> ownedHorseList = new ArrayList<>();
        if (realmRiderProfile.getOwnedHorses() != null) {
            Timber.d("Owned Horse List: " + realmRiderProfile.getOwnedHorses().size());
            for (int i = 0; i < realmRiderProfile.getOwnedHorses().size(); i++) {
                BaseListItem item = new BaseListItem(realmRiderProfile.getOwnedHorses().get(i).getId(),
                        realmRiderProfile.getOwnedHorses().get(i).getName());
                ownedHorseList.add(item);
            }
            riderProfileEntity.setOwnedHorses(ownedHorseList);
        }

        List<BaseListItem> studentHorseList = new ArrayList<>();
        if (realmRiderProfile.getStudentHorses() != null) {
            Timber.d("Student Horse List: " + realmRiderProfile.getStudentHorses().size());
            for (int i = 0; i < realmRiderProfile.getStudentHorses().size(); i++) {
                BaseListItem item = new BaseListItem(realmRiderProfile.getStudentHorses().get(i).getId(),
                        realmRiderProfile.getStudentHorses().get(i).getName());
                studentHorseList.add(item);
            }
            riderProfileEntity.setStudentHorses(studentHorseList);
        }

        List<BaseListItem> studentList = new ArrayList<>();
        if (realmRiderProfile.getStudents() != null) {
            Timber.d("Student List: " + realmRiderProfile.getStudents().size());
            for (int i = 0; i < realmRiderProfile.getStudents().size(); i++) {
                BaseListItem item = new BaseListItem(realmRiderProfile.getStudents().get(i).getId(),
                        realmRiderProfile.getStudents().get(i).getName());
                studentList.add(item);
            }
            riderProfileEntity.setStudents(studentList);
        }

        List<BaseListItem> instructorList = new ArrayList<>();
        if (realmRiderProfile.getInstructors() != null) {
            Timber.d("Instructor List: " + realmRiderProfile.getInstructors().size());
            for (int i = 0; i < realmRiderProfile.getInstructors().size(); i++) {
                BaseListItem item = new BaseListItem(realmRiderProfile.getInstructors().get(i).getId(),
                        realmRiderProfile.getInstructors().get(i).getName());
                instructorList.add(item);
            }
            riderProfileEntity.setInstructors(instructorList);
        }

        HashMap<String, SkillLevel> skillLevels = new HashMap<>();
        for (RealmSkillLevel realmSkillLevel : realmRiderProfile.getSkillLevels()) {
            SkillLevel level = new SkillLevel();
            level.setLevelId(realmSkillLevel.getLevelId());
            level.setLastEditBy(realmSkillLevel.getLastEditBy());
            level.setLastEditDate(realmSkillLevel.getLastEditDate());
            level.setLevel(realmSkillLevel.getLevel());

            skillLevels.put(String.valueOf(level.getLevelId()), level);
        }
        riderProfileEntity.setSkillLevels(skillLevels);
        return riderProfileEntity;
    }
}
