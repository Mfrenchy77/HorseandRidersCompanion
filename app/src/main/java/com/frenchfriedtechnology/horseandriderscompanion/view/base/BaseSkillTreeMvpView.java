package com.frenchfriedtechnology.horseandriderscompanion.view.base;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Category;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Skill;

import java.util.List;

/**
 * Actions for Base Skill Tree
 */

interface BaseSkillTreeMvpView extends MvpView {
    void showProgress();

    void hideProgress();

    void getCategories(List<Category> categories);

    void getSkills(List<Skill> skills);

    void getLevels(List<Level> levels);
}
