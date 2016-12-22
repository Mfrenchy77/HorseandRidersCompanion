package com.frenchfriedtechnology.horseandriderscompanion.data.local.realm;


import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.mappers.RiderProfileMapper;

import io.realm.Realm;
import timber.log.Timber;

/**
 * Service to create and retrieve user's Rider Profile from Realm
 */
public class RealmService {
    public RealmService() {
    }

    /**
     * Get the User's Rider Profile
     */
    public RiderProfile getUsersRiderProfile(String email) {
        Timber.d("getUserRiderProfile() called");
        Realm realm = Realm.getDefaultInstance();
        RealmRiderProfile riderProfile = realm.where(RealmRiderProfile.class).equalTo("email", email).findFirst();
       /* Log.d("RealmService", "\r\nRealm Rider Profile: " + riderProfile.getName()
                + "\r\nEmail: " + riderProfile.getEmail()
                + "\r\nId: " + riderProfile.getId()
                + "\r\nLast Edit by: " + riderProfile.getLastEditBy()
                + "\r\nLast Edit date: " + riderProfile.getLastEditDate()
                + "\r\nEditor: " + riderProfile.isEditor()
                + "\r\nSubscribed: " + riderProfile.isSubscribed()
                + "\r\nSubscription date: " + riderProfile.getSubscriptionDate()
                + "\r\nSubscription end date:" + riderProfile.getSubscriptionEndDate()
                + "\r\nSkill Level size: " + riderProfile.getSkillLevels().size());*/
        RiderProfile profile = new RiderProfileMapper().realmToEntity(riderProfile);
        realm.close();
        return profile;
    }

    /**
     * Create or update user's Rider Profile
     */
    public void createOrUpdateRiderProfileToRealm(RiderProfile riderProfile, final OnTransactionCallback onTransactionCallback) {
        Timber.d("createOrUpdateRiderProfileToRealm() called");
      /*  Log.d("RealmService", "\r\nUser's Rider Profile: " + riderProfile.getName()
                + "\r\nEmail: " + riderProfile.getEmail()
                + "\r\nId: " + riderProfile.getId()
                + "\r\nLast Edit by: " + riderProfile.getLastEditBy()
                + "\r\nLast Edit date: " + riderProfile.getLastEditDate()
                + "\r\nEditor: " + riderProfile.isEditor()
                + "\r\nSubscribed: " + riderProfile.isSubscribed()
                + "\r\nSubscription date: " + riderProfile.getSubscriptionDate()
                + "\r\nSubscription end date:" + riderProfile.getSubscriptionEndDate()
                + "\r\nSkill Level size: " + riderProfile.getSkillLevels().size());*/

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(realm1 -> {
            RealmRiderProfile realmRiderProfile = new RiderProfileMapper().entityToRealm(riderProfile);
            realm1.copyToRealmOrUpdate(realmRiderProfile);
        }, () -> {
            if (onTransactionCallback != null) {
                onTransactionCallback.onRealmSuccess();
                if (realm != null) {
                    realm.close();
                }
            }
        }, error -> {
            if (onTransactionCallback != null) {
                onTransactionCallback.onRealmError(error);
                if (realm != null) {
                    realm.close();
                }
            }
        });
    }

    /**
     * Delete user's Rider Profile
     */
    public void deleteRiderProfileFromRealm(String email, final OnTransactionCallback onTransactionCallback) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(realm1 -> {
            RealmRiderProfile profile = realm1.where(RealmRiderProfile.class).equalTo("email", email).findFirst();
            profile.deleteFromRealm();
        }, () -> {
            if (onTransactionCallback != null) {
                onTransactionCallback.onRealmSuccess();
                if (realm != null) {
                    realm.close();
                }
            }
        }, error -> {
            if (onTransactionCallback != null) {
                onTransactionCallback.onRealmError(error);
                if (realm != null) {
                    realm.close();
                }
            }
        });
    }

    /**
     * Callbacks to notify presenters
     */
    public interface OnTransactionCallback {
        void onRealmSuccess();

        void onRealmError(final Throwable e);
    }
}