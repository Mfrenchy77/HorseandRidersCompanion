package com.frenchfriedtechnology.horseandriderscompanion.data.sync;

import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.CategoriesApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.LevelsApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.ResourcesApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.SkillsApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Category;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Skill;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.realmServices.RealmSkillTreeService;


import javax.inject.Inject;

import timber.log.Timber;

/**
 * Sync SkillTree components to Firebase or Realm based on last edit date
 */

public class SkillTreeSyncer {
    private RealmSkillTreeService skillTreeService;

    @Inject
    public SkillTreeSyncer(RealmSkillTreeService skillTreeService) {
        this.skillTreeService = skillTreeService;
    }

    //----Category
    public void syncCategory(Category realmCategory, Category firebaseCategory, String path) {

        if (realmCategory == null) {
            syncCategoryRealmToFirebase(firebaseCategory);
        } else if (realmCategory.getLastEditDate() > firebaseCategory.getLastEditDate()) {
            syncCategoryFirebaseToRealm(realmCategory, path);
        } else if (realmCategory.getLastEditDate() < firebaseCategory.getLastEditDate()) {
            syncCategoryRealmToFirebase(firebaseCategory);
        } else if (realmCategory.getLastEditDate() == firebaseCategory.getLastEditDate()) {
            Timber.d("Category in Sync");
        }
    }


    private void syncCategoryFirebaseToRealm(Category localCategory, String path) {
        Timber.d("Syncing Firebase to Realm ");
        new CategoriesApi().createOrEditCategory(localCategory, path);
    }


    private void syncCategoryRealmToFirebase(Category firebaseCategory) {
        Timber.d("Syncing Realm to Firebase");
        skillTreeService.createOrUpdateCategory(firebaseCategory, new RealmSkillTreeService.RealmSkillTreeCallback() {
            @Override
            public void onRealmSuccess() {
                Timber.d("Realm Category Sync Successful");
            }

            @Override
            public void onRealmError(Throwable e) {
                Timber.e("Error Syncing Realm Category: " + e.toString());
            }
        });
    }

    //-----Skill
    public void syncSkill(Skill localSkill, Skill firebaseSkill, String path) {
        if (localSkill == null) {
            syncSkillRealmToFirebase(firebaseSkill);
        } else if (localSkill.getLastEditDate() > firebaseSkill.getLastEditDate()) {
            syncSkillFirebaseToRealm(localSkill, path);
        } else if (localSkill.getLastEditDate() < firebaseSkill.getLastEditDate()) {
            syncSkillRealmToFirebase(firebaseSkill);
        } else if (localSkill.getLastEditDate() == firebaseSkill.getLastEditDate()) {
            Timber.d("Skills are in Sync");
        }
    }

    private void syncSkillFirebaseToRealm(Skill localSkill, String path) {
        Timber.d("Skill Syncing Firebase to Realm ");
        new SkillsApi().createOrUpdateSkill(localSkill, path);
    }


    private void syncSkillRealmToFirebase(Skill firebaseSkill) {
        Timber.d("Skill Syncing Realm to Firebase");
        skillTreeService.createOrUpdateSkill(firebaseSkill, new RealmSkillTreeService.RealmSkillTreeCallback() {
            @Override
            public void onRealmSuccess() {
                Timber.d("Realm Skill Sync Successful");

            }

            @Override
            public void onRealmError(Throwable e) {
                Timber.e("Error Syncing Realm Skill: " + e.toString());

            }
        });
    }

    //----Level
    public void syncLevel(Level localLevel, Level firebaseLevel, String path) {
        if (localLevel == null) {
            syncLevelRealmToFirebase(firebaseLevel);
        } else if (localLevel.getLastEditDate() > firebaseLevel.getLastEditDate()) {
            syncLevelFirebaseToRealm(localLevel, path);
        } else if (localLevel.getLastEditDate() < firebaseLevel.getLastEditDate()) {
            syncLevelRealmToFirebase(firebaseLevel);
        } else if (localLevel.getLastEditDate() == firebaseLevel.getLastEditDate()) {
            Timber.d("Levels are in Sync");
        }
    }

    private void syncLevelFirebaseToRealm(Level localLevel, String path) {
        Timber.d("Level Syncing Firebase to Realm ");
        new LevelsApi().createOrUpdateLevel(localLevel, path);
    }


    private void syncLevelRealmToFirebase(Level firebaseLevel) {
        Timber.d("Level Syncing Realm to Firebase");
        skillTreeService.createOrUpdateLevel(firebaseLevel, new RealmSkillTreeService.RealmSkillTreeCallback() {
            @Override
            public void onRealmSuccess() {
                Timber.d("Realm Level Sync Successful");
            }

            @Override
            public void onRealmError(Throwable e) {
                Timber.e("Error Syncing Realm Level: " + e.toString());
            }
        });
    }

    //----Resource
    public void syncResource(Resource localResource, Resource firebaseResource) {
        if (localResource == null) {
            syncResourceRealmToFirebase(firebaseResource);
        } else if (localResource.getLastEditDate() > firebaseResource.getLastEditDate()) {
            syncResourceFirebaseToRealm(localResource);
        } else if (localResource.getLastEditDate() < firebaseResource.getLastEditDate()) {
            syncResourceRealmToFirebase(firebaseResource);
        } else if (localResource.getLastEditDate() == firebaseResource.getLastEditDate()) {
            Timber.d("Resources are in Sync");
        }
    }

    private void syncResourceFirebaseToRealm(Resource localResource) {
        Timber.d("Resource Syncing Firebase to Realm ");
        new ResourcesApi().createOrUpdateResource(localResource);
    }

    private void syncResourceRealmToFirebase(Resource firebaseResource) {
        Timber.d("Resource Syncing Realm to Firebase");
        skillTreeService.createOrUpdateResource(firebaseResource, new RealmSkillTreeService.RealmSkillTreeCallback() {
            @Override
            public void onRealmSuccess() {
                Timber.d("Realm Resource Sync Successful");
            }

            @Override
            public void onRealmError(Throwable e) {
                Timber.e("Error Syncing Realm Resource: " + e.toString());
            }
        });
    }
}
