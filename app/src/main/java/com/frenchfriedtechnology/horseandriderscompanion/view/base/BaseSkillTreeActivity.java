package com.frenchfriedtechnology.horseandriderscompanion.view.base;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;

import com.frenchfriedtechnology.horseandriderscompanion.BusProvider;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Category;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Skill;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.SkillLevel;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.events.LevelsFetch;
import com.frenchfriedtechnology.horseandriderscompanion.view.adapters.SkillTreePagerAdapter;
import com.frenchfriedtechnology.horseandriderscompanion.view.dialogs.DialogCreateCategory;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

import static com.frenchfriedtechnology.horseandriderscompanion.view.dialogs.DialogCreateCategory.NEW_CATEGORY;

/**
 * Base Activity for Horse/Rider Skill Tree and Profile
 */

public class BaseSkillTreeActivity extends BaseActivity implements BaseSkillTreeMvpView {

    @Inject
    protected
    BaseSkillTreePresenter basePresenter;

    private static final String EDIT_MODE = "Edit_Mode";

    private List<Category> categories = new ArrayList<>();
    private List<Skill> skills = new ArrayList<>();
    private List<Level> levels = new ArrayList<>();
    private List<Resource> resources = new ArrayList<>();

    protected SkillTreePagerAdapter skillTreePagerAdapter;
    private FloatingActionButton addCategoryFab;
    protected ViewPager viewPager;
    private boolean editMode = false;
    private boolean rider;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_skill_tree2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);

        basePresenter.attachView(this);
        basePresenter.setRider(isRider());

        editMode = new UserPrefs().isEditMode();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hnr);
        setSupportActionBar(toolbar);
        setupToolbar(toolbar);

        addCategoryFab = (FloatingActionButton) findViewById(R.id.add_category_fab);
        addCategoryFab.setVisibility(new UserPrefs().isEditor() && new UserPrefs().isEditMode() ? View.VISIBLE : View.GONE);
        addCategoryFab.setOnClickListener(view -> addCategory());

        Switch editSwitch = (Switch) findViewById(R.id.edit_switch);
        editSwitch.setChecked(editMode);
        editSwitch = (Switch) findViewById(R.id.edit_switch);
        editSwitch.setVisibility(new UserPrefs().isEditor() ? View.VISIBLE : View.GONE);
        editSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            editMode = b;
            new UserPrefs().setEditMode(b);
            skillTreePagerAdapter.notifyDataSetChanged();
            addCategoryFab.setVisibility(editMode ? View.VISIBLE : View.GONE);
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (basePresenter != null) {
            basePresenter.detachView();
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void getCategories(List<Category> categories) {
        this.categories = categories;
        if (skillTreePagerAdapter != null) {
            skillTreePagerAdapter.setCategories(categories);
        }
    }

    @Override
    public void getSkills(List<Skill> skills) {
        if (skillTreePagerAdapter != null) {
            skillTreePagerAdapter.setAllSkills(skills);
            this.skills = skills;
        }
    }

    @Override
    public void getLevels(List<Level> newLevels) {
        if (skillTreePagerAdapter != null) {
            BusProvider.getBusProviderInstance().post(new LevelsFetch(newLevels));
            skillTreePagerAdapter.setAllLevels(newLevels);
            levels = newLevels;

        }
    }

    @Override
    public void getResources(List<Resource> resources) {
        skillTreePagerAdapter.setResources(resources);
    }

    /**
     * Method for Horse/Rider profile to set saved SkillLevel to Level
     */
    public List<Level> setSkillLevelToLevel(Collection<SkillLevel> skillLevels, List<Level> levels) {
        if (levels != null && skillLevels != null) {
            for (SkillLevel skillLevel : skillLevels) {
                levels = matchSkillLevelToLevel(skillLevel, levels);
            }
        } else {
            Timber.d("Levels are Null");
        }
        return levels;
    }

    private List<Level> matchSkillLevelToLevel(SkillLevel skillLevel, List<Level> levels) {
        for (int i = 0; i < levels.size(); i++) {
            if (levels.get(i).getId().equals(skillLevel.getLevelId())) {
                levels.get(i).setLevel(skillLevel.getLevel());
            }
        }
        return levels;
    }


    public void addCategory() {
        DialogCreateCategory.newInstance(NEW_CATEGORY, null).show(getFragmentManager(), null);
    }

    /**
     * Allows Horse/Rider profile access to Categories, Skills and Levels
     */

    protected List<Category> getCategories() {
        return categories;
    }

    protected List<Level> getLevels() {
        return levels;
    }

    protected List<Skill> getSkills() {
        return skills;
    }


    /**
     * Tells Base Presenter if profile is either Horse or Rider
     */
    protected void setRider(boolean rider) {
        basePresenter.setRider(rider);
        basePresenter.getLevels();
        basePresenter.getSkills();
        basePresenter.getCategories();
        basePresenter.getResources();
        this.rider = rider;
    }

    private boolean isRider() {
        return rider;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("RiderSkillTreeActivity Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EDIT_MODE, editMode);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

}
