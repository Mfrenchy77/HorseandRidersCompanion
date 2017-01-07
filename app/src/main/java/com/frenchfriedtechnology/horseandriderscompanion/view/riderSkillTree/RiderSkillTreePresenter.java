package com.frenchfriedtechnology.horseandriderscompanion.view.riderSkillTree;

import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.RiderProfileApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.SkillLevel;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.realmServices.RealmProfileService;
import com.frenchfriedtechnology.horseandriderscompanion.data.sync.ProfileSyncer;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BasePresenter;


import java.util.HashMap;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Presenter for representing a Rider's SkillTree
 */

public class RiderSkillTreePresenter extends BasePresenter<RiderSkillTreeMvpView> {

    private ProfileSyncer profileSyncer;
    private final RealmProfileService realmProfileService;
    private RiderProfile riderProfile = new RiderProfile();
    private RiderProfile remoteRiderProfile = new RiderProfile();

    @Inject
    public RiderSkillTreePresenter(RealmProfileService realmProfileService, ProfileSyncer profileSyncer) {
        this.realmProfileService = realmProfileService;
        this.profileSyncer = profileSyncer;
    }

    @Override
    public void attachView(RiderSkillTreeMvpView view) {
        super.attachView(view);
    }

    //---- Profile Actions

    void getRiderProfile(String email) {
        checkViewAttached();
        riderProfile = realmProfileService.getUsersRiderProfile(email);
        RiderProfileApi.getRiderProfile(email, new RiderProfileApi.RiderProfileCallback() {
            @Override
            public void onSuccess(RiderProfile firebaseProfile) {
                remoteRiderProfile = firebaseProfile;
                profileSyncer.syncProfile(riderProfile, firebaseProfile);
                if (isViewAttached()) {
                    getMvpView().getRiderProfile(profileSyncer.syncProfile(riderProfile, remoteRiderProfile));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e(throwable.toString());
            }
        });
        if (remoteRiderProfile != null) {
            getMvpView().getRiderProfile(profileSyncer.syncProfile(riderProfile, remoteRiderProfile));
        } else if (riderProfile != null) {
            getMvpView().getRiderProfile(riderProfile);
        }
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

        realmProfileService.createOrUpdateRiderProfileToRealm(riderProfile, new RealmProfileService.RealmProfileCallback() {
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
}
