package com.frenchfriedtechnology.horseandriderscompanion.view.base;

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
import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;

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
    public void createCategory(Category newCategory) {
        new CategoriesApi().createOrEditCategory(newCategory, skillTreePath());
    }

    void getCategories() {
        realmCategories = realmSkillTreeService.getCategories(isRider());
        Timber.d("Realm Category size: " + realmCategories.size());
        if (isViewAttached()) {
            getMvpView().getCategories(realmCategories);
        }
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

    public void editCategory(Category editedCategory) {
        new CategoriesApi().createOrEditCategory(editedCategory, skillTreePath());
    }

    public void deleteCategory(String deletedCategoryName) {
        new CategoriesApi().deleteCategory(deletedCategoryName, skillTreePath());
    }

    private Category matchCategoryId(String id, List<Category> categoriesToSearch) {
        Category matchedCategory = new Category();
        if (!categoriesToSearch.isEmpty()) {
            for (int i = 0; i < categoriesToSearch.size(); i++) {
                if (categoriesToSearch.get(i).getId().equals(id)) {
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

    public void createSkill(Skill skill) {
        new SkillsApi().createOrUpdateSkill(skill, skillTreePath());
    }

    public void editSkill(Skill editedSkill) {
        new SkillsApi().createOrUpdateSkill(editedSkill, skillTreePath());
    }

    public void deleteSkill(String deleteSkillId) {
        new SkillsApi().deleteSkill(deleteSkillId, skillTreePath());
    }

    private Skill matchSkillIds(String id, List<Skill> skillsToSearch) {
        Skill matchedSkill = new Skill();
        if (!skillsToSearch.isEmpty()) {
            for (int i = 0; i < skillsToSearch.size(); i++) {
                if (skillsToSearch.get(i).getId().equals(id)) {
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

    public void createLevel(Level level) {
        new LevelsApi().createOrUpdateLevel(level, skillTreePath());
    }

    public void editLevel(Level editedLevel) {
        new LevelsApi().createOrUpdateLevel(editedLevel, skillTreePath());
    }

    public void deleteLevel(String deletedLevelId) {
        new LevelsApi().deleteLevel(deletedLevelId, skillTreePath());
    }

    private Level matchLevelIds(String id, List<Level> levelsToSearch) {
        Level matchedLevel = new Level();
        if (!levelsToSearch.isEmpty()) {
            for (int i = 0; i < levelsToSearch.size(); i++) {
                if (levelsToSearch.get(i).getId().equals(id)) {
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
        new ResourcesApi().createOrUpdateResource(resource, new ResourcesApi.ResourceCreatedCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void editResource(Resource editedResource) {
        new ResourcesApi().createOrUpdateResource(editedResource, new ResourcesApi.ResourceCreatedCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void deleteResource(Resource resource) {
        new ResourcesApi().deleteResource(resource);
    }

    private Resource matchResourceIds(String id, List<Resource> resourcesToSearch) {
        Resource matchedResource = new Resource();
        if (!resourcesToSearch.isEmpty()) {
            for (int i = 0; i < resourcesToSearch.size(); i++) {
                if (resourcesToSearch.get(i).getId().equals(id)) {
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
}


