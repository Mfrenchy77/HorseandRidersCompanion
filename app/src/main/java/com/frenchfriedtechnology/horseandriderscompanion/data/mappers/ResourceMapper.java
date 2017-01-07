package com.frenchfriedtechnology.horseandriderscompanion.data.mappers;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.BaseListItem;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.ListItem;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.RealmResource;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import timber.log.Timber;

/**
 * Mapper for converting Resource Realm/Entity and back
 */

public class ResourceMapper {

    public Resource realmToEntity(RealmResource realmResource) {
        if (realmResource == null) {
            return null;
        }
        Resource resourceEntity = new Resource();
        //set fields from Dto
        resourceEntity.setId(realmResource.getId());
        resourceEntity.setName(realmResource.getName());
        resourceEntity.setThumbnail(realmResource.getThumbnail());
        resourceEntity.setDescription(realmResource.getDescription());
        resourceEntity.setUrl(realmResource.getUrl());
        resourceEntity.setNumberOfRates(realmResource.getNumberOfRates());
        resourceEntity.setRating(realmResource.getRating());
        resourceEntity.setLastEditBy(realmResource.getLastEditBy());
        resourceEntity.setLastEditDate(realmResource.getLastEditDate());

        List<BaseListItem> categoriesList = new ArrayList<>();
        if (realmResource.getCategoryIds() != null) {
            Timber.d("Category List: " + realmResource.getCategoryIds().size());
            for (int i = 0; i < realmResource.getCategoryIds().size(); i++) {
                BaseListItem item = new BaseListItem(realmResource.getCategoryIds().get(i).getId(),
                        realmResource.getCategoryIds().get(i).getName());
                categoriesList.add(item);
            }
            resourceEntity.setCategoryIds(categoriesList);
        }

        List<BaseListItem> skillsList = new ArrayList<>();
        if (realmResource.getSkillIds() != null) {
            Timber.d("Skills List: " + realmResource.getSkillIds().size());
            for (int i = 0; i < realmResource.getSkillIds().size(); i++) {
                BaseListItem item = new BaseListItem(realmResource.getSkillIds().get(i).getId(),
                        realmResource.getSkillIds().get(i).getName());
                skillsList.add(item);
            }
            resourceEntity.setSkillIds(skillsList);
        }

        List<BaseListItem> levelList = new ArrayList<>();
        if (realmResource.getLevelIds() != null) {
            Timber.d("Level List: " + realmResource.getLevelIds().size());
            for (int i = 0; i < realmResource.getLevelIds().size(); i++) {
                BaseListItem item = new BaseListItem(realmResource.getLevelIds().get(i).getId(),
                        realmResource.getLevelIds().get(i).getName());
                levelList.add(item);
            }
            resourceEntity.setLevelIds(levelList);
        }

        return resourceEntity;
    }

    public RealmResource entityToRealm(Resource resourceEntity) {
        if (resourceEntity == null) {
            return null;
        }
        RealmResource realmResource = new RealmResource();
        //set fields from Entity
        realmResource.setId(resourceEntity.getId());
        realmResource.setName(resourceEntity.getName());
        realmResource.setThumbnail(resourceEntity.getThumbnail());
        realmResource.setDescription(resourceEntity.getDescription());
        realmResource.setUrl(resourceEntity.getUrl());
        realmResource.setNumberOfRates(resourceEntity.getNumberOfRates());
        realmResource.setRating(resourceEntity.getRating());
        realmResource.setLastEditBy(resourceEntity.getLastEditBy());
        realmResource.setLastEditDate(resourceEntity.getLastEditDate());

        RealmList<ListItem> categoryList = new RealmList<>();
        if (resourceEntity.getCategoryIds() != null) {
            Timber.d("Category List: " + resourceEntity.getCategoryIds().size());
            for (int i = 0; i < resourceEntity.getCategoryIds().size(); i++) {
                ListItem item = new ListItem(resourceEntity.getCategoryIds().get(i).getId(),
                        resourceEntity.getCategoryIds().get(i).getName());
                categoryList.add(item);
            }
            realmResource.setCategoryIds(categoryList);
        }

        RealmList<ListItem> skillList = new RealmList<>();
        if (resourceEntity.getSkillIds() != null) {
            Timber.d("Skill List: " + resourceEntity.getSkillIds().size());
            for (int i = 0; i < resourceEntity.getSkillIds().size(); i++) {
                ListItem item = new ListItem(resourceEntity.getSkillIds().get(i).getId(),
                        resourceEntity.getSkillIds().get(i).getName());
                skillList.add(item);
            }
            realmResource.setSkillIds(skillList);
        }

        RealmList<ListItem> levelList = new RealmList<>();
        if (resourceEntity.getLevelIds() != null) {
            Timber.d("Level List: " + resourceEntity.getLevelIds().size());
            for (int i = 0; i < resourceEntity.getLevelIds().size(); i++) {
                ListItem item = new ListItem(resourceEntity.getLevelIds().get(i).getId(),
                        resourceEntity.getLevelIds().get(i).getName());
                levelList.add(item);
            }
            realmResource.setLevelIds(levelList);
        }

        return realmResource;
    }
}