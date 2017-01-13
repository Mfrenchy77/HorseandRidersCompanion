package com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.realmServices;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Category;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Skill;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.RealmCategory;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.RealmLevel;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.RealmResource;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.RealmSkill;
import com.frenchfriedtechnology.horseandriderscompanion.data.mappers.CategoryMapper;
import com.frenchfriedtechnology.horseandriderscompanion.data.mappers.LevelMapper;
import com.frenchfriedtechnology.horseandriderscompanion.data.mappers.ResourceMapper;
import com.frenchfriedtechnology.horseandriderscompanion.data.mappers.SkillMapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * Service to create, edit and retrieve Skill Tree Items from Realm
 */

@SuppressWarnings("Convert2streamapi")
public class RealmSkillTreeService {

    private final Provider<Realm> realmProvider;

    @Inject
    public RealmSkillTreeService(Provider<Realm> realmProvider) {
        this.realmProvider = realmProvider;
    }

    /**
     * Return a list of all Categories of a SkillTree
     */
    public List<Category> getCategories(boolean rider) {
        List<Category> categories = new ArrayList<>();
        Timber.d("getCategories() called, rider: " + rider);
        final Realm realm = realmProvider.get();
        RealmResults<RealmCategory> realmResults = realm.where(RealmCategory.class).equalTo("rider", rider).findAll();

        List<RealmCategory> realmCategories = realm.copyFromRealm(realmResults);

        for (RealmCategory category : realmCategories) {
            categories.add(new CategoryMapper().realmToEntity(category));
            Timber.d("categoryName: " + category.getName());
        }
        realm.close();
        Timber.d("Categories: " + categories.size());
        return categories;

    }

    /**
     * Return a single category from a SkillTree
     */
    public Category getCategory(String id) {
        Timber.d("getCategory() " + id + " called");
        final Realm realm = realmProvider.get();
        RealmCategory category = realm.where(RealmCategory.class).equalTo("id", id).findFirst();
        realm.close();
        return new CategoryMapper().realmToEntity(realm.copyFromRealm(category));
    }

    /**
     * Create/Update a category
     */
    public void createOrUpdateCategory(Category category, final RealmSkillTreeCallback callback) {
        Timber.d("createOrUpdateCategory() called");

        Realm realm = null;
        try {
            realm = realmProvider.get();
            realm.executeTransactionAsync(realm1 -> {
                RealmCategory realmCategory = new CategoryMapper().entityToRealm(category);
                realm1.copyToRealmOrUpdate(realmCategory);
            }, () -> {
                if (callback != null) {
                    callback.onRealmSuccess();

                }
            }, error -> {
                if (callback != null) {
                    callback.onRealmError(error);

                }
            });
        } catch (Exception e) {
            Timber.e(e, "There was an error while adding in Realm.");
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * Delete Category from Skill Tree
     */

    public void deleteCategory(String id, boolean rider, final RealmSkillTreeCallback callback) {
        Timber.d("deleteCategoryFromRealm() called");
        Realm realm = null;
        try {
            realm = realmProvider.get();
            realm.executeTransactionAsync(realm1 -> {
                RealmCategory category = realm1.where(RealmCategory.class)
                        .equalTo("id", id).equalTo("rider", rider).findFirst();
                category.deleteFromRealm();
            }, () -> {
                if (callback != null) {
                    callback.onRealmSuccess();
                }
            }, error -> {
                if (callback != null) {
                    callback.onRealmError(error);
                }
            });
        } catch (Exception e) {
            Timber.e(e, "There was an error while deleting in Realm.");
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * Return a list of all Skills of a SkillTree
     */
    public List<Skill> getSkills(boolean rider) {
        List<Skill> skills = new ArrayList<>();
        Timber.d("getSkills() called, rider: " + rider);
        Realm realm = realmProvider.get();
        RealmResults realmResults = realm.where(RealmSkill.class).equalTo("rider", rider).findAll();
        List<RealmSkill> realmSkills = realm.copyFromRealm(realmResults);
        for (RealmSkill skill : realmSkills) {
            skills.add(new SkillMapper().realmToEntity(skill));
        }
        realm.close();
        return skills;

    }

    /**
     * Return a single skill from a SkillTree
     */

    public Skill getSkill(String id) {
        Timber.d("getSkill() " + id + " called");
        Realm realm = realmProvider.get();
        RealmSkill skill = realm.where(RealmSkill.class).equalTo("id", id).findFirst();
        realm.close();
        return new SkillMapper().realmToEntity(skill);
    }

    /**
     * Create/Update a skill
     */
    public void createOrUpdateSkill(Skill skill, final RealmSkillTreeCallback callback) {
        Timber.d("createOrUpdateSkill() called");

        Realm realm = null;
        try {
            realm = realmProvider.get();
            realm.executeTransactionAsync(realm1 -> {
                RealmSkill realmSkill = new SkillMapper().entityToRealm(skill);
                realm1.copyToRealmOrUpdate(realmSkill);
            }, () -> {
                if (callback != null) {
                    callback.onRealmSuccess();
                }
            }, error -> {
                if (callback != null) {
                    callback.onRealmError(error);
                }
            });
        } catch (Exception e) {
            Timber.e(e, "There was an error while adding in Realm.");
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * Delete Skill from Skill Tree
     */

    public void deleteSkill(String id, boolean rider, final RealmSkillTreeCallback callback) {

        Realm realm = null;
        try {
            realm = realmProvider.get();
            realm.executeTransactionAsync(realm1 -> {
                RealmSkill skill = realm1.where(RealmSkill.class)
                        .equalTo("id", id).equalTo("rider", rider).findFirst();
                skill.deleteFromRealm();
            }, () -> {
                if (callback != null) {
                    callback.onRealmSuccess();

                }
            }, error -> {
                if (callback != null) {
                    callback.onRealmError(error);
                }
            });
        } catch (Exception e) {
            Timber.e(e, "There was an error while adding in Realm.");
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * Return a list of all Resources of a SkillTree
     */
    public List<Level> getLevels(boolean rider) {
        List<Level> levels = new ArrayList<>();
        Timber.d("getLevels() called, rider: " + rider);
        Realm realm = realmProvider.get();

        RealmResults realmResults = realm.where(RealmLevel.class).equalTo("rider", rider).findAll();
        List<RealmLevel> realmLevels = realm.copyFromRealm(realmResults);
        for (RealmLevel realmLevel : realmLevels) {
            levels.add(new LevelMapper().realmToEntity(realmLevel));
        }
        realm.close();
        return levels;

    }

    /**
     * Return a single level from a SkillTree
     */

    public Level getLevel(String id) {
        Timber.d("getLevel() " + id + " called");
        Realm realm = realmProvider.get();
        RealmLevel level = realm.where(RealmLevel.class).equalTo("id", id).findFirst();
        realm.close();
        return new LevelMapper().realmToEntity(realm.copyFromRealm(level));
    }

    /**
     * Create/Update a level
     */
    public void createOrUpdateLevel(Level level, final RealmSkillTreeCallback callback) {
        Timber.d("createOrUpdateLevel() called");

        Realm realm = null;
        try {
            realm = realmProvider.get();
            realm.executeTransactionAsync(realm1 -> {
                RealmLevel realmLevel = new LevelMapper().entityToRealm(level);
                realm1.copyToRealmOrUpdate(realmLevel);
            }, () -> {
                if (callback != null) {
                    callback.onRealmSuccess();
                }
            }, error -> {
                if (callback != null) {
                    callback.onRealmError(error);
                }
            });
        } catch (Exception e) {
            Timber.e(e, "There was an error while adding in Realm.");
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * Delete Level from Skill Tree
     */
    public void deleteLevel(String id, boolean rider, final RealmSkillTreeCallback callback) {

        Realm realm = null;
        try {
            realm = realmProvider.get();
            realm.executeTransactionAsync(realm1 -> {
                RealmLevel level = realm1.where(RealmLevel.class)
                        .equalTo("id", id).equalTo("rider", rider).findFirst();
                level.deleteFromRealm();
            }, () -> {
                if (callback != null) {
                    callback.onRealmSuccess();
                }
            }, error -> {
                if (callback != null) {
                    callback.onRealmError(error);
                }
            });
        } catch (Exception e) {
            Timber.e(e, "There was an error while adding in Realm.");
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }


    /**
     * Return a list of all Resources of a SkillTree
     */
    public List<Resource> getResources() {
        List<Resource> resources = new ArrayList<>();
        Timber.d("getResources() called");
        Realm realm = realmProvider.get();
        RealmResults realmResults = realm.where(RealmResource.class).findAll();
        List<RealmResource> realmResources = realm.copyFromRealm(realmResults);
        for (RealmResource realmResource : realmResources) {
            resources.add(new ResourceMapper().realmToEntity(realmResource));
        }
        realm.close();
        return resources;

    }

    /**
     * Return a single resource from a SkillTree
     */
    public Resource getResource(String id) {
        Timber.d("getResource() " + id + " called");
        Realm realm = realmProvider.get();
        RealmResource resource = realm.where(RealmResource.class).equalTo("id", id).findFirst();
        realm.close();
        return new ResourceMapper().realmToEntity(resource);
    }

    /**
     * Create/Update a resource
     */
    public void createOrUpdateResource(Resource resource, final RealmSkillTreeCallback callback) {
        Timber.d("createOrUpdateResource() called");

        Realm realm = null;
        try {
            realm = realmProvider.get();
            realm.executeTransactionAsync(realm1 -> {
                RealmResource realmResource = new ResourceMapper().entityToRealm(resource);
                realm1.copyToRealmOrUpdate(realmResource);
            }, () -> {
                if (callback != null) {
                    callback.onRealmSuccess();
                }
            }, error -> {
                if (callback != null) {
                    callback.onRealmError(error);
                }
            });
        } catch (Exception e) {
            Timber.e(e, "There was an error while adding in Realm.");
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * Delete Resource from Skill Tree
     */
    public void deleteResource(String id, boolean rider, final RealmSkillTreeCallback callback) {
        Realm realm = null;
        try {
            realm = realmProvider.get();
            realm.executeTransactionAsync(realm1 -> {
                RealmResource resource = realm1.where(RealmResource.class)
                        .equalTo("id", id).equalTo("rider", rider).findFirst();
                resource.deleteFromRealm();
            }, () -> {
                if (callback != null) {
                    callback.onRealmSuccess();
                }
            }, error -> {
                if (callback != null) {
                    callback.onRealmError(error);
                }
            });
        } catch (Exception e) {
            Timber.e(e, "There was an error while adding in Realm.");
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * Callbacks to notify presenters
     */
    public interface RealmSkillTreeCallback {
        void onRealmSuccess();

        void onRealmError(final Throwable e);
    }
}
