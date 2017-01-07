package com.frenchfriedtechnology.horseandriderscompanion.data.mappers;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.RealmLevel;

/**
 * Mapper for converting Level Realm/Entity and back
 */

public class LevelMapper {

    public Level realmToEntity(RealmLevel realmLevel) {
        if (realmLevel == null) {
            return null;
        }
        Level levelEntity = new Level();
        //set fields from Dto
        levelEntity.setId(realmLevel.getId());
        levelEntity.setLevelName(realmLevel.getLevelName());
        levelEntity.setSkillId(realmLevel.getSkillId());
        levelEntity.setDescription(realmLevel.getDescription());
        levelEntity.setLearningDescription(realmLevel.getLearningDescription());
        levelEntity.setCompleteDescription(realmLevel.getCompleteDescription());
        levelEntity.setPosition(realmLevel.getPosition());
        levelEntity.setRider(realmLevel.isRider());
        levelEntity.setLevel(realmLevel.getLevel());
        levelEntity.setLastEditBy(realmLevel.getLastEditBy());
        levelEntity.setLastEditDate(realmLevel.getLastEditDate());
        return levelEntity;
    }

    public RealmLevel entityToRealm(Level levelEntity) {
        if (levelEntity == null) {
            return null;
        }
        RealmLevel realmLevel = new RealmLevel();
        //set fields from Entity
        realmLevel.setId(levelEntity.getId());
        realmLevel.setLevelName(levelEntity.getLevelName());
        realmLevel.setDescription(levelEntity.getDescription());
        realmLevel.setLearningDescription(levelEntity.getLearningDescription());
        realmLevel.setCompleteDescription(levelEntity.getCompleteDescription());
        realmLevel.setRider(levelEntity.isRider());
        realmLevel.setPosition(levelEntity.getPosition());
        realmLevel.setSkillId(levelEntity.getSkillId());
        realmLevel.setLevel(levelEntity.getLevel());
        realmLevel.setLastEditBy(levelEntity.getLastEditBy());
        realmLevel.setLastEditDate(levelEntity.getLastEditDate());
        return realmLevel;
    }
}