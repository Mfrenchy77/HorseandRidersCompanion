package com.frenchfriedtechnology.horseandriderscompanion.view.base;

import android.content.Intent;
import android.net.Uri;

import com.frenchfriedtechnology.horseandriderscompanion.AccountManager;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.CategoriesApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.LevelsApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.ResourcesApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.SkillsApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Category;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Skill;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.realmServices.RealmSkillTreeService;
import com.frenchfriedtechnology.horseandriderscompanion.data.sync.SkillTreeSyncer;
import com.frenchfriedtechnology.horseandriderscompanion.di.ConfigPersistent;
import com.frenchfriedtechnology.horseandriderscompanion.events.CategoryCreateEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.CategoryDeleteEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.CategoryEditEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.LevelCreateEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.LevelDeleteEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.LevelEditEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.ResourceSelectedEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.ResourceUpdateEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.SkillCreateEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.SkillDeleteEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.SkillUpdateEvent;
import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;
import com.frenchfriedtechnology.horseandriderscompanion.util.ViewUtil;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Base Skill Tree Presenter to be used anywhere a skill tree needs to be represented
 */

@ConfigPersistent
public class BaseSkillTreePresenter extends BasePresenter<BaseSkillTreeMvpView> {

    private final RealmSkillTreeService realmSkillTreeService;
    private SkillTreeSyncer skillTreeSyncer;

    @Inject
    public BaseSkillTreePresenter(SkillTreeSyncer skillTreeSyncer, RealmSkillTreeService realmSkillTreeService) {
        this.skillTreeSyncer = skillTreeSyncer;
        this.realmSkillTreeService = realmSkillTreeService;
    }

    private boolean rider;
    private List<Skill> realmSkills = null;
    private List<Skill> remoteSkills = null;
    private List<Level> realmLevels = null;
    private List<Level> remoteLevels = null;
    private List<Resource> realmResources = null;
    private List<Resource> remoteResources = null;
    private List<Category> realmCategories = null;
    private List<Category> remoteCategories = null;

    @Override
    public void attachView(BaseSkillTreeMvpView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    //----Rider/Horse Profile for Api path
    void setRider(boolean rider) {
        this.rider = rider;
    }

    private boolean isRider() {
        return rider;
    }

    private String skillTreePath() {
        if (isRider()) {
            return Constants.RIDER_SKILL_TREE;
        } else {
            return Constants.HORSE_SKILL_TREE;
        }
    }

    //----Category Actions

    void getCategories() {
        realmCategories = realmSkillTreeService.getCategories(isRider());
        getMvpView().getCategories(realmCategories);
        Timber.d("Realm Category size: " + realmCategories.size());/*
        if (isViewAttached()) {
        }*/
        new CategoriesApi().getCategories(skillTreePath(), new CategoriesApi.CategoryCallback() {
            @Override
            public void onSuccess(List<Category> firebaseCategories) {
                Timber.d("Firebase Categories received size: " + firebaseCategories.size());
                remoteCategories = firebaseCategories;
                for (Category firebaseCategory : remoteCategories) {
                    Category realmCategory = matchCategoryId(firebaseCategory.getId(), realmCategories);
                    skillTreeSyncer.syncCategory(realmCategory, firebaseCategory, skillTreePath());
                }
                if (isViewAttached()) {
                    getMvpView().getCategories(firebaseCategories);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e("Error Retrieving Categories: " + throwable.toString());
            }
        });

    }

    @Subscribe
    public void createCategoryEvent(CategoryCreateEvent event) {
        Category category = new Category();
        category.setName(event.getCategoryName());
        category.setDescription(event.getCategoryDescription());
        category.setId(ViewUtil.createLongId());
        category.setLastEditDate(System.currentTimeMillis());
        category.setLastEditBy(AccountManager.currentUser());
        category.setRider(isRider());
        new CategoriesApi().createOrEditCategory(category, skillTreePath());
    }
/*

    public void createCategory(Category newCategory) {
    }
*/

    @Subscribe
    public void categoryUpdateEvent(CategoryEditEvent event) {
        Category editCategory = event.getCategory();
        editCategory.setLastEditDate(System.currentTimeMillis());
        editCategory.setLastEditBy(AccountManager.currentUser());
        new CategoriesApi().createOrEditCategory(editCategory, skillTreePath());
    }
/*

    public void editCategory(Category editedCategory) {
    }
*/

    @Subscribe
    public void deleteCategoryEvent(CategoryDeleteEvent event) {
        new CategoriesApi().deleteCategory(event.getCategoryName(), skillTreePath());
    }
/*

    public void deleteCategory(String deletedCategoryName) {
    }
*/

    private Category matchCategoryId(long id, List<Category> categoriesToSearch) {
        Category matchedCategory = new Category();
        if (!categoriesToSearch.isEmpty()) {
            for (int i = 0; i < categoriesToSearch.size(); i++) {
                if (categoriesToSearch.get(i).getId() == (id)) {
                    matchedCategory = categoriesToSearch.get(i);
                    return matchedCategory;
                }
            }
            Timber.d("categoriesToSearch() size:  " + categoriesToSearch.size());
        } else {
            Timber.d("categoriesToSearch() isEmpty, adding default Category");
            return matchedCategory;
        }
        return null;
    }


    //---- Skill Actions

    void getSkills() {
        realmSkills = realmSkillTreeService.getSkills(isRider());
        Timber.d("getSkills() size: " + realmSkills.size());
        getMvpView().getSkills(realmSkills);
        new SkillsApi().getSkills(skillTreePath(), new SkillsApi.SkillCallback() {
            @Override
            public void onSuccess(List<Skill> skills) {
                remoteSkills = skills;
                for (Skill firebaseSkill : remoteSkills) {
                    Skill realmSkill = matchSkillIds(firebaseSkill.getId(), realmSkills);
                    skillTreeSyncer.syncSkill(realmSkill, firebaseSkill, skillTreePath());
                }
                if (isViewAttached()) {
                    getMvpView().getSkills(skills);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e("Error Retrieving Skills: " + throwable.toString());
            }
        });
    }

    @Subscribe
    public void createSkillEvent(SkillCreateEvent event) {
        Skill skill = new Skill();
        skill.setCategoryId(event.getCategoryId());
        skill.setId(event.isEdit() ? event.getSkillId() : ViewUtil.createLongId());
        skill.setSkillName(event.getSkillName());
        skill.setDescription(event.getSkillDescription());
        skill.setLastEditDate(System.currentTimeMillis());
        skill.setLastEditBy(AccountManager.currentUser());
        skill.setRider(isRider());
        new SkillsApi().createOrUpdateSkill(skill, skillTreePath());
    }
/*
    public void createSkill(Skill skill) {
    }*/

    @Subscribe
    public void updateSkillEvent(SkillUpdateEvent event) {
        Skill editedSkill = event.getSkill();
        editedSkill.setLastEditBy(AccountManager.currentUser());
        editedSkill.setLastEditDate(System.currentTimeMillis());
        new SkillsApi().createOrUpdateSkill(editedSkill, skillTreePath());
    }
/*
    public void editSkill(Skill editedSkill) {
    }*/

    @Subscribe
    public void deleteSkillEvent(SkillDeleteEvent event) {
        new SkillsApi().deleteSkill(event.getSkillId(), skillTreePath());
    }
/*

    public void deleteSkill(String deleteSkillId) {
    }
*/

    private Skill matchSkillIds(long id, List<Skill> skillsToSearch) {
        Skill matchedSkill = new Skill();
        if (!skillsToSearch.isEmpty()) {
            for (int i = 0; i < skillsToSearch.size(); i++) {
                if (skillsToSearch.get(i).getId() == id) {
                    matchedSkill = skillsToSearch.get(i);
                    return matchedSkill;
                }
            }
        } else {
            Timber.d("skillsToSearch() isEmpty, adding default Skill");
            return matchedSkill;
        }
        return null;
    }
    //---- Level Actions

    void getLevels() {
        realmLevels = realmSkillTreeService.getLevels(isRider());
        Timber.d("getLevels() size: " + realmLevels.size());
        getMvpView().getLevels(realmLevels);
        new LevelsApi().getLevels(skillTreePath(), new LevelsApi.LevelCallback() {
            @Override
            public void onSuccess(List<Level> levels) {
                remoteLevels = levels;
                for (Level firebaseLevel : remoteLevels) {
                    Level realmLevel = matchLevelIds(firebaseLevel.getId(), realmLevels);
                    skillTreeSyncer.syncLevel(realmLevel, firebaseLevel, skillTreePath());
                }
                if (isViewAttached()) {
                    getMvpView().getLevels(levels);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e("Error Retrieving Levels: " + throwable);
            }
        });
    }

    @Subscribe
    public void createLevelEvent(LevelCreateEvent event) {
        Level level = event.getLevel();
        if (!event.isEdit()) {
            level.setRider(isRider());
        }
        new LevelsApi().createOrUpdateLevel(level, skillTreePath());
    }
/*

    public void createLevel(Level level) {
    }

*/

    @Subscribe
    public void updateLevelEvent(LevelEditEvent event) {
        Level updatedLevel = event.getLevel();
        updatedLevel.setLastEditBy(AccountManager.currentUser());
        updatedLevel.setLastEditDate(System.currentTimeMillis());
        new LevelsApi().createOrUpdateLevel(updatedLevel, skillTreePath());
    }
/*

    public void editLevel(Level editedLevel) {
    }
*/

    @Subscribe
    public void deleteLevelEvent(LevelDeleteEvent event) {
        new LevelsApi().deleteLevel(event.getLevelId(), skillTreePath());
    }
/*

    public void deleteLevel(String deletedLevelId) {
    }
*/

    private Level matchLevelIds(long id, List<Level> levelsToSearch) {
        Level matchedLevel = new Level();
        if (!levelsToSearch.isEmpty()) {
            for (int i = 0; i < levelsToSearch.size(); i++) {
                if (levelsToSearch.get(i).getId() == id) {
                    matchedLevel = levelsToSearch.get(i);
                    return matchedLevel;
                }
            }
        } else {
            Timber.d("levelsToSearch() isEmpty, adding default Level");
            return matchedLevel;
        }
        return null;
    }

    //----Resources
    public void getResources() {
        realmResources = realmSkillTreeService.getResources();
        Timber.d("getResources() size: " + realmResources.size());
        new ResourcesApi().getAllResources(new ResourcesApi.ResourcesCallback() {
            @Override
            public void onSuccess(List<Resource> resources) {
                remoteResources = resources;
                if (remoteResources != null) {
                    Timber.d("Firebase Resources size: " + remoteResources.size());
                }
                for (Resource firebaseResource : resources) {
                    Resource realmResource = matchResourceIds(firebaseResource.getId(), realmResources);
                    skillTreeSyncer.syncResource(realmResource, firebaseResource);
                }
                Timber.d("View attached: " + isViewAttached());
                if (isViewAttached()) {
                    getMvpView().getResources(resources);
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    public void createResource(Resource resource) {
        new ResourcesApi().createOrUpdateResource(resource);
    }

    @Subscribe
    public void resourceUpdateEvent(ResourceUpdateEvent event) {
        new ResourcesApi().createOrUpdateResource(event.getResource());
    }

    public void updateResource(Resource updatedResource) {
    }

    public void deleteResource(Resource resource) {
        new ResourcesApi().deleteResource(resource);
    }

    private Resource matchResourceIds(long id, List<Resource> resourcesToSearch) {
        Resource matchedResource = new Resource();
        if (!resourcesToSearch.isEmpty()) {
            for (int i = 0; i < resourcesToSearch.size(); i++) {
                if (resourcesToSearch.get(i).getId() == id) {
                    matchedResource = resourcesToSearch.get(i);
                    return matchedResource;
                }
            }
        } else {
            Timber.d("resourcesToSearch() isEmpty, adding default Resource");
            return matchedResource;
        }
        return null;
    }

    @Subscribe
    public void resourceSelectedEvent(ResourceSelectedEvent event) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(event.getUrl()));
        getMvpView().openIntent(intent);
    }
}


