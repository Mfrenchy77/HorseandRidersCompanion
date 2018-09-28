package com.frenchfriedtechnology.horseandriderscompanion.view.horseSkillTree;

import com.frenchfriedtechnology.horseandriderscompanion.AccountManager;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.HorseProfileApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.HorseProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.SkillLevel;
import com.frenchfriedtechnology.horseandriderscompanion.events.LevelAdjustedEvent;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BasePresenter;
import com.squareup.otto.Subscribe;


import java.util.HashMap;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Presenter for Horse Skill Tree
 */

public class HorseSkillTreePresenter extends BasePresenter<HorseSkillTreeMvpView> {

    private HorseProfile horseProfile = new HorseProfile();

    @Inject
    HorseSkillTreePresenter() {
    }

    @Override
    public void attachView(HorseSkillTreeMvpView view) {
        super.attachView(view);
    }

    /**
     * Retrieve Horse Profile
     */
    void getHorseProfile(long id) {
        HorseProfileApi.getHorseProfile(id, new HorseProfileApi.HorseProfileCallback() {
            @Override
            public void onSuccess(HorseProfile firebaseHorseProfile) {
                horseProfile = firebaseHorseProfile;
                if (isViewAttached()) {
                    getMvpView().getHorseProfile(horseProfile);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e("Error Retrieving Horse Profile: " + throwable);
            }
        });
    }

    /**
     * Update Horse's Skill Tree level
     */

    @Subscribe
    public void levelAdjustedEvent(LevelAdjustedEvent event) {
        if (!event.isRider()) {
            SkillLevel skillLevel = new SkillLevel();
            skillLevel.setLevel(event.getProgress());
            skillLevel.setLevelId(event.getLevelId());
            skillLevel.setLastEditDate(System.currentTimeMillis());
            skillLevel.setLastEditBy(AccountManager.currentUser());

            HashMap<String, SkillLevel> levels = new HashMap<>();

            //check for existing skillLevel and remove it
            if (horseProfile.getSkillLevels() != null) {
                Timber.d("UpdateHorseSkillLevel() horseSkillLevel size " + horseProfile.getSkillLevels().size());
                levels = horseProfile.getSkillLevels();
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
            horseProfile.setSkillLevels(levels);
            horseProfile.setLastEditDate(System.currentTimeMillis());
            horseProfile.setLastEditBy(AccountManager.currentUser());

            HorseProfileApi.createOrUpdateHorseProfile(horseProfile);
        }
    }
}

    /*
    void updateHorseSkillTreeLevel(SkillLevel skillLevel) {

        HashMap<String, SkillLevel> levels = new HashMap<>();
        //check for existing skillLevel and remove it
        if (horseProfile.getSkillLevels() != null) {
            Timber.d("UpdateHorseSkillLevel() horseSkillLevel size " + horseProfile.getSkillLevels().size());
            levels = horseProfile.getSkillLevels();
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
            Timber.d("Skill Levels is Null");
        }

        horseProfile.setSkillLevels(levels);
        horseProfile.setLastEditBy(AccountManager.currentUser());
        horseProfile.setLastEditDate(System.currentTimeMillis());

        HorseProfileApi.createOrUpdateHorseProfile(horseProfile);
    }
}*/
