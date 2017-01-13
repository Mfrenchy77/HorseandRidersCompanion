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
        //set fields from Realm
        resourceEntity.setId(realmResource.getId());
        resourceEntity.setName(realmResource.getName());
        resourceEntity.setThumbnail(realmResource.getThumbnail());
        resourceEntity.setDescription(realmResource.getDescription());
        resourceEntity.setUrl(realmResource.getUrl());
        resourceEntity.setNumberOfRates(realmResource.getNumberOfRates());
        resourceEntity.setRating(realmResource.getRating());
        resourceEntity.setLastEditBy(realmResource.getLastEditBy());
        resourceEntity.setLastEditDate(realmResource.getLastEditDate());

        List<BaseListItem> skillTreeIdList = new ArrayList<>();
        if (realmResource.getSkillTreeIds() != null) {
            Timber.d("Category List: " + realmResource.getSkillTreeIds().size());
            for (int i = 0; i < realmResource.getSkillTreeIds().size(); i++) {
                BaseListItem item = new BaseListItem(realmResource.getSkillTreeIds().get(i).getId(),
                        realmResource.getSkillTreeIds().get(i).getName());
                skillTreeIdList.add(item);
            }
            resourceEntity.setSkillTreeIds(skillTreeIdList);
        }

        List<BaseListItem> usersWhoRated = new ArrayList<>();
        if (realmResource.getUsersWhoRated() != null) {
            Timber.d("Category List: " + realmResource.getUsersWhoRated().size());
            for (int i = 0; i < realmResource.getUsersWhoRated().size(); i++) {
                BaseListItem item = new BaseListItem(realmResource.getUsersWhoRated().get(i).getId(),
                        realmResource.getUsersWhoRated().get(i).getName());
                usersWhoRated.add(item);
            }
            resourceEntity.setUsersWhoRated(usersWhoRated);
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

        RealmList<ListItem> skillTreeIdsList = new RealmList<>();
        if (resourceEntity.getSkillTreeIds() != null) {
            for (int i = 0; i < resourceEntity.getSkillTreeIds().size(); i++) {
                ListItem item = new ListItem(resourceEntity.getSkillTreeIds().get(i).getId(),
                        resourceEntity.getSkillTreeIds().get(i).getName());
                skillTreeIdsList.add(item);
            }
            realmResource.setSkillTreeIds(skillTreeIdsList);
        }

        RealmList<ListItem> usersWhoRated = new RealmList<>();
        if (resourceEntity.getUsersWhoRated() != null) {
            for (int i = 0; i < resourceEntity.getUsersWhoRated().size(); i++) {
                ListItem item = new ListItem(resourceEntity.getUsersWhoRated().get(i).getId(),
                        resourceEntity.getUsersWhoRated().get(i).getName());
                usersWhoRated.add(item);
            }
            realmResource.setUsersWhoRated(usersWhoRated);
        }

        return realmResource;
    }
}