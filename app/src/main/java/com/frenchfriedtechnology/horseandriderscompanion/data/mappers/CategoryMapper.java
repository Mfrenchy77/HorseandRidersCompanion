package com.frenchfriedtechnology.horseandriderscompanion.data.mappers;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Category;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.RealmCategory;

/**
 * Mapper for converting Category from Realm/Entity and back
 */

public class CategoryMapper {

    public Category realmToEntity(RealmCategory realmCategory) {
        if (realmCategory == null) {
            return null;
        }
        Category categoryEntity = new Category();
        // set fields from Realm
        categoryEntity.setId(realmCategory.getId());
        categoryEntity.setName(realmCategory.getName());
        categoryEntity.setDescription(realmCategory.getDescription());
        categoryEntity.setPosition(realmCategory.getPosition());
        categoryEntity.setRider(realmCategory.isRider());
        categoryEntity.setLastEditBy(realmCategory.getLastEditBy());
        categoryEntity.setLastEditDate(realmCategory.getLastEditDate());
        return categoryEntity;
    }

    public RealmCategory entityToRealm(Category categoryEntity) {
        if (categoryEntity == null) {
            return null;
        }
        RealmCategory realmCategory = new RealmCategory();
        // set fields from Entity
        realmCategory.setId(categoryEntity.getId());
        realmCategory.setName(categoryEntity.getName());
        realmCategory.setDescription(categoryEntity.getDescription());
        realmCategory.setPosition(categoryEntity.getPosition());
        realmCategory.setRider(categoryEntity.isRider());
        realmCategory.setLastEditBy(categoryEntity.getLastEditBy());
        realmCategory.setLastEditDate(categoryEntity.getLastEditDate());
        return realmCategory;
    }
}
