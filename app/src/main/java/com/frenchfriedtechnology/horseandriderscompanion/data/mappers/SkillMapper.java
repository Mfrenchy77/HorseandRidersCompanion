package com.frenchfriedtechnology.horseandriderscompanion.data.mappers;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Skill;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.RealmSkill;

/**
 * Mapper for converting Skill Objects into Entities/Realm and back
 */

public class SkillMapper {

    public Skill realmToEntity(RealmSkill realmSkill) {
        if (realmSkill == null) {
            return null;
        }
        Skill skillEntity = new Skill();
        //set fields from Realm
        skillEntity.setId(realmSkill.getId());
        skillEntity.setSkillName(realmSkill.getSkillName());
        skillEntity.setCategoryId(realmSkill.getCategoryId());
        skillEntity.setDescription(realmSkill.getDescription());
        skillEntity.setPosition(realmSkill.getPosition());
        skillEntity.setRider(realmSkill.isRider());
        skillEntity.setLastEditBy(realmSkill.getLastEditBy());
        skillEntity.setLastEditDate(realmSkill.getLastEditDate());
        return skillEntity;
    }

    public RealmSkill entityToRealm(Skill skillEntity) {
        if (skillEntity == null) {
            return null;
        }
        RealmSkill realmSkill = new RealmSkill();
        //set fields from Entity
        realmSkill.setId(skillEntity.getId());
        realmSkill.setSkillName(skillEntity.getSkillName());
        realmSkill.setCategoryId(skillEntity.getCategoryId());
        realmSkill.setDescription(skillEntity.getDescription());
        realmSkill.setPosition(skillEntity.getPosition());
        realmSkill.setRider(skillEntity.isRider());
        realmSkill.setLastEditBy(skillEntity.getLastEditBy());
        realmSkill.setLastEditDate(skillEntity.getLastEditDate());
        return realmSkill;
    }
}