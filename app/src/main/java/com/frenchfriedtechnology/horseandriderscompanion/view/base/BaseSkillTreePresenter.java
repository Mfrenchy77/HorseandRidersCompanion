package com.frenchfriedtechnology.horseandriderscompanion.view.base;

import android.content.Context;

import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.CategoriesApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.LevelsApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.ResourcesApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.SkillsApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Category;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Skill;
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

    @Inject
    BaseSkillTreePresenter() {
    }
// TODO: 22/12/16 Add Realm and syncing for SkillTree
/*
    private RealmService realmService = new RealmService();
*/

    private boolean rider;

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
        new CategoriesApi().getCategories(skillTreePath(), new CategoriesApi.CategoryCallback() {
            @Override
            public void onSuccess(List<Category> categories) {
                getMvpView().getCategories(categories);
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


    //---- Skill Actions

    void getSkills() {
        new SkillsApi().getSkills(skillTreePath(), new SkillsApi.SkillCallback() {
            @Override
            public void onSuccess(List<Skill> skills) {
                getMvpView().getSkills(skills);
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

    //---- Level Actions

    void getLevels() {
        new LevelsApi().getLevels(skillTreePath(), new LevelsApi.LevelCallback() {
            @Override
            public void onSuccess(List<Level> levels) {
                getMvpView().getLevels(levels);
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e("Error Retrieving Levels: " + throwable);
            }
        });
    }

    public void createLevel(Level level) {
        new LevelsApi().createOrEditLevel(level, skillTreePath());
    }

    public void editLevel(Level editedLevel) {
        new LevelsApi().createOrEditLevel(editedLevel, skillTreePath());
    }

    public void deleteLevel(String deletedLevelId) {
        new LevelsApi().deleteLevel(deletedLevelId, skillTreePath());
    }

}


