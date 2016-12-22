package com.frenchfriedtechnology.horseandriderscompanion.view.horseSkillTree;

import android.content.Context;

import com.frenchfriedtechnology.horseandriderscompanion.AccountManager;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.HorseProfileApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.HorseProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.SkillLevel;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.RealmService;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BasePresenter;


import java.util.HashMap;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Presenter for Horse Skill Tree
 */

public class HorseSkillTreePresenter extends BasePresenter<HorseSkillTreeMvpView> {


    private Context context;

    @Inject
    HorseSkillTreePresenter() {
    }

    // TODO: 22/12/16 Add syncing for Skill Tree and Realm
    private RealmService realmService = new RealmService();
    private HorseProfile horseProfile = new HorseProfile();

    @Override
    public void attachView(HorseSkillTreeMvpView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    /**
     * Retrieve Horse Profile
     */
    void getHorseProfile(String id) {
        checkViewAttached();
        HorseProfileApi.getHorseProfile(id, new HorseProfileApi.HorseProfileCallback() {
            @Override
            public void onSuccess(HorseProfile firebaseHorseProfile) {
                if (isViewAttached()) {
                    getMvpView().getHorseProfile(firebaseHorseProfile);
                }
                getMvpView().updateAdapter();
                horseProfile = firebaseHorseProfile;
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
    void updateHorseSkillTreeLevel(SkillLevel skillLevel) {

        HashMap<String, SkillLevel> levels = new HashMap<>();
        //check for existing skillLevel and remove it
        if (horseProfile.getSkillLevels() != null) {
            Timber.d("UpdateHorseSkillLevel() horseSkillLevel size " + horseProfile.getSkillLevels().size());
            levels = horseProfile.getSkillLevels();
            if (levels.containsKey(skillLevel.getLevelId())) {
                //replace old with new
                levels.remove(skillLevel.getLevelId());
                levels.put(skillLevel.getLevelId(), skillLevel);
                Timber.d("Replaced Skill Level");
            } else {
                //create new
                Timber.d("Created new Skill Level");
                levels.put(skillLevel.getLevelId(), skillLevel);
            }
        } else {
            Timber.d("Skill Levels is Null");
        }

        horseProfile.setSkillLevels(levels);
        horseProfile.setLastEditBy(AccountManager.currentUser());
        horseProfile.setLastEditDate(System.currentTimeMillis());

        HorseProfileApi.createOrUpdateHorseProfile(horseProfile);
    }
}
