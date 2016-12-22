package com.frenchfriedtechnology.horseandriderscompanion.data.sync;

import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.RiderProfileApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.RealmService;

import timber.log.Timber;

/**
 * SyncFirebase or Realm based on last edit date
 */

public class ProfileSyncer {


    public RiderProfile syncProfile(RiderProfile localProfile, RiderProfile firebaseProfile, RealmService realmService) {
        if (localProfile.getLastEditDate() > firebaseProfile.getLastEditDate()) {
            syncFirebaseToRealm(localProfile);
            return localProfile;
        } else if (localProfile.getLastEditDate() < firebaseProfile.getLastEditDate()) {
            syncRealmToFirebase(firebaseProfile, realmService);
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


    private void syncRealmToFirebase(RiderProfile firebaseRiderProfile, RealmService realmService) {
        Timber.d("Syncing Realm to Firebase");
        realmService.createOrUpdateRiderProfileToRealm(firebaseRiderProfile, new RealmService.OnTransactionCallback() {
            @Override
            public void onRealmSuccess() {

            }

            @Override
            public void onRealmError(Throwable e) {

            }
        });
    }
}
