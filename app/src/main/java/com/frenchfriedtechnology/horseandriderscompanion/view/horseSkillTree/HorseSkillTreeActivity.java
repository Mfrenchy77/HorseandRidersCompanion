package com.frenchfriedtechnology.horseandriderscompanion.view.horseSkillTree;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.frenchfriedtechnology.horseandriderscompanion.AccountManager;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Category;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.HorseProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Skill;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.SkillLevel;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.events.CategorySelectEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.LevelsFetch;
import com.frenchfriedtechnology.horseandriderscompanion.events.LevelAdjustedEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.LevelSelectEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.SkillSelectEvent;
import com.frenchfriedtechnology.horseandriderscompanion.view.adapters.SkillTreePagerAdapter;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BaseSkillTreeActivity;
import com.frenchfriedtechnology.horseandriderscompanion.view.dialogs.DialogCreateCategory;
import com.frenchfriedtechnology.horseandriderscompanion.view.dialogs.DialogCreateAdjustLevel;
import com.frenchfriedtechnology.horseandriderscompanion.view.dialogs.DialogCreateSkill;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

import static com.frenchfriedtechnology.horseandriderscompanion.events.LevelSelectEvent.EDIT_LEVEL;
import static com.frenchfriedtechnology.horseandriderscompanion.events.LevelSelectEvent.HORSE_ADJUST;
import static com.frenchfriedtechnology.horseandriderscompanion.events.LevelSelectEvent.NEW_LEVEL;
import static com.frenchfriedtechnology.horseandriderscompanion.events.LevelSelectEvent.RIDER_ADJUST;
import static com.frenchfriedtechnology.horseandriderscompanion.view.dialogs.DialogCreateCategory.EDIT_CATEGORY;
import static com.frenchfriedtechnology.horseandriderscompanion.view.dialogs.DialogCreateSkill.EDIT_SKILL;
import static com.frenchfriedtechnology.horseandriderscompanion.view.dialogs.DialogCreateSkill.NEW_SKILL;


/**
 * Activity to Display Profile and Skill Tree for Horse
 */

public class HorseSkillTreeActivity extends BaseSkillTreeActivity implements HorseSkillTreeMvpView {

    @Inject
    HorseSkillTreePresenter presenter;

    private static final String VIEW_PAGER_ITEM = "VIEW_PAGE_ITEM";
    private static final String HORSE_ID = "HORSE_ID";
    private List<Resource> resources = new ArrayList<>();
    private HorseProfile horseProfile = new HorseProfile();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRider(false);
        initPagerAdapter();
        activityComponent().inject(this);
        presenter.attachView(this);
        presenter.getHorseProfile(getIntent().getLongExtra(HORSE_ID, 0));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Timber.d("ViewPager currentItem: " + viewPager.getCurrentItem());
        outState.putInt(VIEW_PAGER_ITEM, viewPager.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Timber.d("Saved ViewPager Item = " + savedInstanceState.getInt(VIEW_PAGER_ITEM));
        viewPager.setCurrentItem(savedInstanceState.getInt(VIEW_PAGER_ITEM), true);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void getHorseProfile(HorseProfile profile) {
        this.horseProfile = profile;
        skillTreePagerAdapter.setHorseProfile(profile);
        skillTreePagerAdapter.notifyDataSetChanged();
        toolbar.setTitle(horseProfile.getName());
        setTitle(horseProfile.getName());
    }

    @Override
    public void updateAdapter() {
        skillTreePagerAdapter.notifyDataSetChanged();
    }


    private void initPagerAdapter() {
        skillTreePagerAdapter = new SkillTreePagerAdapter(getSupportFragmentManager(),
                getLevels(),
                getSkills(),
                getCategories(),
                getSkillTreeResources(),
                horseProfile,
                null,
                false);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(skillTreePagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    //----Category
/*
    @Subscribe
    public void createCategoryEvent(CategoryCreateEvent event) {
        Category category = new Category();
        category.setName(event.getCategoryName());
        category.setDescription(event.getCategoryDescription());
        category.setId(ViewUtil.createId());
        category.setLastEditDate(System.currentTimeMillis());
        category.setLastEditBy(AccountManager.currentUser());
        category.setPosition(getCategories().size() + 1);
        category.setRider(false);
        basePresenter.createCategory(category);

    }*/

    @Subscribe
    public void categorySelectedEvent(CategorySelectEvent event) {
        Category category = event.getCategory();
        if (event.isEdit()) {
            if (new UserPrefs().isEditor()) {
                DialogCreateCategory.newInstance(EDIT_CATEGORY, category).show(getFragmentManager(), null);
            }
        } else {
            //Show Category Details
        }
    }
/*
    @Subscribe
    public void categoryUpdateEvent(CategoryEditEvent event) {
        Category editCategory = event.getCategory();
        editCategory.setLastEditDate(System.currentTimeMillis());
        editCategory.setLastEditBy(AccountManager.currentUser());
        basePresenter.editCategory(editCategory);
    }*/
/*

    @Subscribe
    public void deleteCategoryEvent(CategoryDeleteEvent event) {
        basePresenter.deleteCategory(event.getCategoryName());
    }
*/

    //----Skills

    @Subscribe
    public void skillSelectedEvent(SkillSelectEvent event) {
        Skill skill = event.getSkill();
        if (event.isEdit()) {
            DialogCreateSkill.newInstance(EDIT_SKILL, skill, event.getCategoryId()).show(getFragmentManager(), null);
        } else {
            DialogCreateSkill.newInstance(NEW_SKILL, skill, event.getCategoryId()).show(getFragmentManager(), null);
        }
    }
/*
    @Subscribe
    public void createSkillEvent(SkillCreateEvent event) {
        Skill skill = new Skill();
        skill.setCategoryId(event.getCategoryId());
        skill.setId(event.isEdit() ? event.getSkillId() : ViewUtil.createId());
        skill.setSkillName(event.getSkillName());
        skill.setDescription(event.getSkillDescription());
        skill.setLastEditDate(System.currentTimeMillis());
        skill.setLastEditBy(AccountManager.currentUser());
        skill.setPosition((getSkills().size() + 1));
        skill.setRider(false);
        basePresenter.createSkill(skill);
    }*/
/*

    @Subscribe
    public void updateSkillEvent(SkillUpdateEvent event) {
        Skill editedSkill = event.getSkill();
        editedSkill.setLastEditBy(AccountManager.currentUser());
        editedSkill.setLastEditDate(System.currentTimeMillis());
        basePresenter.editSkill(editedSkill);
    }
*/
/*
    @Subscribe
    public void deleteSkillEvent(SkillDeleteEvent event) {
        basePresenter.deleteSkill(event.getSkillId());
    }*/

    //----Levels

    @Subscribe
    public void levelSelectedEvent(LevelSelectEvent event) {
        switch (event.getTag()) {
            //Dialog Update Level
            case EDIT_LEVEL:
                DialogCreateAdjustLevel.newInstance(EDIT_LEVEL,
                        event.getLevel(),
                        event.getSkillId(),
                        null)
                        .show(getFragmentManager(), null);
                break;
            case NEW_LEVEL:
                DialogCreateAdjustLevel.newInstance(NEW_LEVEL,
                        null,
                        event.getSkillId(),
                        null)
                        .show(getFragmentManager(), null);
                break;
            //Adjust Rider/Horse SkillLevel
            case RIDER_ADJUST:
                DialogCreateAdjustLevel.newInstance(
                        RIDER_ADJUST,
                        event.getLevel(),
                        event.getSkillId(),
                        getResourcesForLevel(event.getLevel().getId()))
                        .show(getFragmentManager(), null);
                break;
            case HORSE_ADJUST:
                DialogCreateAdjustLevel.newInstance(HORSE_ADJUST,
                        event.getLevel(),
                        event.getSkillId(),
                        getResourcesForLevel(event.getLevel().getId()))
                        .show(getFragmentManager(), null);
                break;
        }
    }

    /*
        @Subscribe
        public void createLevelEvent(LevelCreateEvent event) {
            Level level = event.getLevel();
            if (!event.isEdit()) {
                level.setPosition((getLevels().size() + 1));
                level.setRider(false);
            }
            basePresenter.createLevel(level);
        }

        @Subscribe
        public void updateLevelEvent(LevelEditEvent event) {
            Level updatedLevel = event.getLevel();
            updatedLevel.setLastEditBy(AccountManager.currentUser());
            updatedLevel.setLastEditDate(System.currentTimeMillis());
            basePresenter.editLevel(updatedLevel);
        }


        @Subscribe
        public void deleteLevelEvent(LevelDeleteEvent event) {
            basePresenter.deleteLevel(event.getLevelId());
        }
        */
    @Subscribe
    public void levelAdjustedEvent(LevelAdjustedEvent event) {
        if (!event.isRider()) {
            SkillLevel skillLevel = new SkillLevel();
            skillLevel.setLevel(event.getProgress());
            skillLevel.setLevelId(event.getLevelId());
            skillLevel.setLastEditDate(System.currentTimeMillis());
            skillLevel.setLastEditBy(AccountManager.currentUser());

            presenter.updateHorseSkillTreeLevel(skillLevel);
        }
    }

    @Subscribe
    public void getLevelsEvent(LevelsFetch event) {
        skillTreePagerAdapter.setAllLevels(setSkillLevelToLevel(horseProfile.getSkillLevels().values(), event.getLevels()));
    }

    public static void start(Context context, long horseId) {
        Intent intent = new Intent(context, HorseSkillTreeActivity.class);
        intent.putExtra(HORSE_ID, horseId);
        context.startActivity(intent);
    }
}
