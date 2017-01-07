package com.frenchfriedtechnology.horseandriderscompanion.data.sync;

import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.RiderProfileApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.realmServices.RealmProfileService;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Sync RiderProfile to Firebase or Realm based on last edit date
 */

public class ProfileSyncer {

    private RealmProfileService realmProfileService;

    @Inject
    public ProfileSyncer(RealmProfileService realmProfileService) {
        this.realmProfileService = realmProfileService;
    }

    public RiderProfile syncProfile(RiderProfile localProfile, RiderProfile firebaseProfile) {
        if (localProfile == null && firebaseProfile != null) {
            syncRealmToFirebase(firebaseProfile);
        } else if (firebaseProfile == null && localProfile != null) {
            syncFirebaseToRealm(localProfile);
        } else if (localProfile == null && firebaseProfile == null) {
            return null;
        } else if (localProfile.getLastEditDate() > firebaseProfile.getLastEditDate()) {
            syncFirebaseToRealm(localProfile);
            return localProfile;
        } else if (localProfile.getLastEditDate() < firebaseProfile.getLastEditDate()) {
            syncRealmToFirebase(firebaseProfile);
            return firebaseProfile;
        } else if (localProfile.getLastEditDate() == firebaseProfile.getLastEditDate()) {
            Timber.d("Profiles are in Sync");
            return localProfile;
        }
        return null;
    }

    private void syncFirebaseToRealm(RiderProfile localRiderProfile) {
        Timber.d("Syncing Firebase to Realm");
        RiderProfileApi.createOrUpdateRiderProfile(localRiderProfile);
    }

    private void syncRealmToFirebase(RiderProfile firebaseRiderProfile) {
        Timber.d("Syncing Realm to Firebase");
        realmProfileService.createOrUpdateRiderProfileToRealm(firebaseRiderProfile, new RealmProfileService.RealmProfileCallback() {
            @Override
            public void onRealmSuccess() {
                Timber.d("Realm Sync Successful");
            }

            @Override
            public void onRealmError(Throwable e) {
                Timber.e("Error Syncing Realm: " + e.toString());
            }
        });
    }
}
