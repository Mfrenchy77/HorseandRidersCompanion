package com.frenchfriedtechnology.horseandriderscompanion.view.riderSkillTree;

import android.content.Context;

import com.frenchfriedtechnology.horseandriderscompanion.HorseAndRidersCompanion;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.ResourcesApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.RiderProfileApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.SkillLevel;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.RealmService;
import com.frenchfriedtechnology.horseandriderscompanion.data.sync.ProfileSyncer;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BasePresenter;


import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by matteo on 10/12/16 for HorseandRidersCompanion.
 */

public class RiderSkillTreePresenter extends BasePresenter<RiderSkillTreeMvpView> {
    @Inject
    RiderSkillTreePresenter(/*Context context*/) {/*
        ((HorseAndRidersCompanion) context.getApplicationContext()).getComponent().inject(this);*/
    }

    private RealmService realmService = new RealmService();
    private RiderProfile riderProfile = new RiderProfile();

    @Override
    public void attachView(RiderSkillTreeMvpView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    //---- Profile Actions

    void getRiderProfile(String email) {

        this.riderProfile = realmService.getUsersRiderProfile(email);
       /* Log.d("SkillTreePresenter", "\r\nUser's Rider Profile: " + riderProfile.getName()
                + "\r\nEmail: " + riderProfile.getEmail()
                + "\r\nId: " + riderProfile.getId()
                + "\r\nLast Edit by: " + riderProfile.getLastEditBy()
                + "\r\nLast Edit date: " + riderProfile.getLastEditDate()
                + "\r\nEditor: " + riderProfile.isEditor()
                + "\r\nSubscribed: " + riderProfile.isSubscribed()
                + "\r\nSubscription date: " + riderProfile.getSubscriptionDate()
                + "\r\nSubscription end date:" + riderProfile.getSubscriptionEndDate()
                + "\r\nSkill Level size: " + riderProfile.getSkillLevels().size());
        new UserPrefs().setEditor(riderProfile.isEditor());*/
        if (isViewAttached()) {
            getMvpView().getRiderProfile(riderProfile);
            getMvpView().updateAdapter();
            Timber.d("getRiderProfile() called");
        }
        RiderProfileApi.getRiderProfile(email, new RiderProfileApi.RiderProfileCallback() {

            @Override
            public void onSuccess(RiderProfile firebaseRiderProfile) {
                //sync and update
                new UserPrefs().setEditor(firebaseRiderProfile.isEditor());
                Timber.d("RiderProfile sync and update: " +
                        riderProfile.getName());
                if (isViewAttached()) {
                    getMvpView().getRiderProfile(new ProfileSyncer().syncProfile(riderProfile, firebaseRiderProfile, realmService));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e("Get Profile Error: " + throwable);
            }
        });

    }

    /**
     * Update User's Rider Profile with a new Skill Level
     */
    void updateRiderSkillLevel(SkillLevel skillLevel) {

        HashMap<String, SkillLevel> levels = new HashMap<>();

        //check for existing skillLevel and remove it
        if (riderProfile.getSkillLevels() != null) {
            Timber.d("UpdateRiderSkillLevel() riderSKillLevel size " + riderProfile.getSkillLevels().size());
            levels = riderProfile.getSkillLevels();
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
            Timber.d("SkillLevel is Null");
        }
        riderProfile.setSkillLevels(levels);
        riderProfile.setLastEditBy(riderProfile.getName());
        riderProfile.setLastEditDate(System.currentTimeMillis());

        realmService.createOrUpdateRiderProfileToRealm(riderProfile, new RealmService.OnTransactionCallback() {
            @Override
            public void onRealmSuccess() {
                RiderProfileApi.createOrUpdateRiderProfile(riderProfile);
                Timber.d("Updated Realm Profile");
            }

            @Override
            public void onRealmError(Throwable e) {
                Timber.d("Error Updating Realm Profile");

            }
        });
    }

    //----Resources
    public void getResourcesForLevel(String id) {
        new ResourcesApi().getResourcesForLevel(id, new ResourcesApi.ResourcesCallback() {
            @Override
            public void onSuccess(List<Resource> resources) {
                getMvpView().getResources(resources);
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }
}
