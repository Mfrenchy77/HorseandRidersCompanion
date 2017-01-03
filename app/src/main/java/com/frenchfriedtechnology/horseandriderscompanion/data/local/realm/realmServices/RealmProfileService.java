package com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.realmServices;


import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.RealmRiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.mappers.RiderProfileMapper;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import io.realm.Realm;
import timber.log.Timber;

/**
 * Service to create and retrieve user's Rider Profile from Realm
 */
@Singleton
public class RealmProfileService {

    private final Provider<Realm> mRealmProvider;

    public RealmProfileService(Provider<Realm> mRealmProvider) {
        this.mRealmProvider = mRealmProvider;
    }
@Inject
    /**
     * Get the User's Rider Profile
     */
    public RiderProfile getUsersRiderProfile(String email) {
        Timber.d("getUserRiderProfile() called");
        Realm realm = mRealmProvider.get();
        RealmRiderProfile realmRiderProfile;
        realmRiderProfile = realm.where(RealmRiderProfile.class).equalTo("email", email).findFirst();

       /* Log.d("RealmProfileService", "\r\nRealm Rider Profile: " + riderProfile.getName()
                + "\r\nEmail: " + riderProfile.getEmail()
                + "\r\nId: " + riderProfile.getId()
                + "\r\nLast Edit by: " + riderProfile.getLastEditBy()
                + "\r\nLast Edit date: " + riderProfile.getLastEditDate()
                + "\r\nEditor: " + riderProfile.isEditor()
                + "\r\nSubscribed: " + riderProfile.isSubscribed()
                + "\r\nSubscription date: " + riderProfile.getSubscriptionDate()
                + "\r\nSubscription end date:" + riderProfile.getSubscriptionEndDate()
                + "\r\nSkill Level size: " + riderProfile.getSkillLevels().size());*/
        RiderProfile profile = new RiderProfileMapper().realmToEntity(realmRiderProfile);
        realm.close();
        return profile;
    }

    /**
     * Create or update user's Rider Profile
     */
    public void createOrUpdateRiderProfileToRealm(RiderProfile riderProfile, final RealmProfileCallback onTransactionCallback) {
        Timber.d("createOrUpdateRiderProfileToRealm() called");
        Realm realm = null;
        try {
            realm = mRealmProvider.get();
            realm.executeTransactionAsync(realm1 -> {
                RealmRiderProfile realmRiderProfile = new RiderProfileMapper().entityToRealm(riderProfile);
                realm1.copyToRealmOrUpdate(realmRiderProfile);
            }, () -> {
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmSuccess();
                }
            }, error -> {
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmError(error);
                }
            });
        } catch (Exception e) {
            Timber.e(e, "There was an error while adding in Realm.");
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * Delete user's Rider Profile
     */
    public void deleteRiderProfileFromRealm(String email, final RealmProfileCallback onTransactionCallback) {
        Realm realm = mRealmProvider.get();
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
    public interface RealmProfileCallback {
        void onRealmSuccess();

        void onRealmError(final Throwable e);
    }
}