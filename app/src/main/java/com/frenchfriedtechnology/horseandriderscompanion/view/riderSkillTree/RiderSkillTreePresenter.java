package com.frenchfriedtechnology.horseandriderscompanion.view.riderSkillTree;

import com.frenchfriedtechnology.horseandriderscompanion.AccountManager;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.RiderProfileApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.SkillLevel;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.events.LevelAdjustedEvent;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BasePresenter;
import com.squareup.otto.Subscribe;


import java.util.HashMap;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Presenter for representing a Rider's SkillTree
 */

public class RiderSkillTreePresenter extends BasePresenter<RiderSkillTreeMvpView> {

    private RiderProfile riderProfile = new RiderProfile();

    @Inject
    public RiderSkillTreePresenter() {
    }

    @Override
    public void attachView(RiderSkillTreeMvpView view) {
        super.attachView(view);
    }

    //---- Profile Actions

    void getRiderProfile(String email) {
        checkViewAttached();
        RiderProfileApi.getRiderProfile(email, new RiderProfileApi.RiderProfileCallback() {
            @Override
            public void onSuccess(RiderProfile firebaseProfile) {
                riderProfile = firebaseProfile;
                new UserPrefs().setEditor(firebaseProfile.isEditor());
                if (isViewAttached()) {
                    getMvpView().getRiderProfile(riderProfile);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e(throwable.toString());
            }
        });
    }

    /**
     * Update User's Rider Profile with a new Skill Level
     */

    @Subscribe
    public void levelAdjustedEvent(LevelAdjustedEvent event) {
        if (event.isRider()) {
            SkillLevel skillLevel = new SkillLevel();
            skillLevel.setLevel(event.getProgress());
            skillLevel.setLevelId(event.getLevelId());
            skillLevel.setLastEditDate(System.currentTimeMillis());
            skillLevel.setLastEditBy(AccountManager.currentUser());

            HashMap<String, SkillLevel> levels = new HashMap<>();

            //check for existing skillLevel and remove it
            if (riderProfile.getSkillLevels() != null) {
                Timber.d("UpdateRiderSkillLevel() riderSKillLevel size " + riderProfile.getSkillLevels().size());
                levels = riderProfile.getSkillLevels();
                if (levels.containsKey(skillLevel.getLevelId())) {
                    //replace old with new
                    levels.remove(skillLevel.getLevelId());
                    levels.put(String.valueOf(skillLevel.getLevelId()), skillLevel);
                    Timber.d("Replaced Skill Level");
                } else {
                    //create new
                    Timber.d("Created new Skill Level");
                    levels.put(String.valueOf(skillLevel.getLevelId()), skillLevel);
                }
            } else {
                Timber.d("SkillLevel is Null");
            }
            riderProfile.setSkillLevels(levels);
            riderProfile.setLastEditBy(riderProfile.getName());
            riderProfile.setLastEditDate(System.currentTimeMillis());

            RiderProfileApi.createOrUpdateRiderProfile(riderProfile);


        }
    }
}

