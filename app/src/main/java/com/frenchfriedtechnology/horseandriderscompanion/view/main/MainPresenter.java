package com.frenchfriedtechnology.horseandriderscompanion.view.main;

import android.util.Log;

import com.frenchfriedtechnology.horseandriderscompanion.AccountManager;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.HorseProfileApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.RiderProfileApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.HorseProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.RealmService;
import com.frenchfriedtechnology.horseandriderscompanion.data.sync.ProfileSyncer;
import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BasePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;


public class MainPresenter extends BasePresenter<MainMvpView> {

    @Inject
    MainPresenter() {
    }

    @Inject
    FirebaseAuth mAuth;

    private RiderProfile userRiderProfile = new RiderProfile();
    private RealmService realmService = new RealmService();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;

    private boolean profileFetched = false;

    @Override
    public void attachView(MainMvpView view) {
        super.attachView(view);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        //perform profile fetch and sync
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                //no connection or signed out go to login
                Timber.d("Auth User is NULL");
            } else {
                //user authorized and signed in
                Timber.d("\n\nUser Authorized, Monitoring...\n\n");
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    //---- User's Profile
    private void setProfileFetched(boolean profileFetched) {
        this.profileFetched = profileFetched;
    }

    void getRiderProfile(String email) {
        checkViewAttached();
        if (!profileFetched) {
            this.userRiderProfile = realmService.getUsersRiderProfile(email);
            if (userRiderProfile != null) {
              /*  Log.d("MainPresenter", "\r\nUser's Rider Profile: " + userRiderProfile.getName()
                        + "\r\nEmail: " + userRiderProfile.getEmail()
                        + "\r\nId: " + userRiderProfile.getId()
                        + "\r\nLast Edit by: " + userRiderProfile.getLastEditBy()
                        + "\r\nLast Edit date: " + userRiderProfile.getLastEditDate()
                        + "\r\nEditor: " + userRiderProfile.isEditor()
                        + "\r\nSubscribed: " + userRiderProfile.isSubscribed()
                        + "\r\nSubscription date: " + userRiderProfile.getSubscriptionDate()
                        + "\r\nSubscription end date:" + userRiderProfile.getSubscriptionEndDate()
                        + "\r\nSkill Level size: " + userRiderProfile.getSkillLevels().size()
                        + "\r\nOwned Horses size: " + userRiderProfile.getOwnedHorses().size());
*/
                monitorFirebaseProfile();
                setProfileFetched(true);
                getMvpView().getUserProfile(userRiderProfile);
            } else {
                setProfileFetched(false);
                Timber.d("Realm Profile is Null");
            }
        }
    }

    /**
     * Monitors Firebase RiderProfile for User and syncs to Realm if necessary
     */

    private void monitorFirebaseProfile() {
        RiderProfileApi.getRiderProfile(userRiderProfile.getEmail(), new RiderProfileApi.RiderProfileCallback() {
            @Override
            public void onSuccess(RiderProfile firebaseProfile) {
              /*  Log.d("MainPresenter", "\r\nUser's FireBase Profile: " + firebaseProfile.getName()
                        + "\r\nEmail: " + firebaseProfile.getEmail()
                        + "\r\nId: " + firebaseProfile.getId()
                        + "\r\nLast Edit by: " + firebaseProfile.getLastEditBy()
                        + "\r\nLast Edit date: " + firebaseProfile.getLastEditDate()
                        + "\r\nEditor: " + firebaseProfile.isEditor()
                        + "\r\nSubscribed: " + firebaseProfile.isSubscribed()
                        + "\r\nSubscription date: " + firebaseProfile.getSubscriptionDate()
                        + "\r\nSubscription end date:" + firebaseProfile.getSubscriptionEndDate()
                        + "\r\nSkill Levels size: " + firebaseProfile.getSkillLevels().size()
                        + "\r\nOwned Horses size: " + firebaseProfile.getOwnedHorses().size());
                new UserPrefs().setEditor(userRiderProfile.isEditor());*/

                getMvpView().getUserProfile(new ProfileSyncer().syncProfile(userRiderProfile, firebaseProfile, realmService));
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    //-----Horse Profile Actions

    void createOrUpdateHorseProfile(HorseProfile horseProfile) {
        //create horse profile and add the id to a list in user's Rider Profile
        List<String> horseList = userRiderProfile.getOwnedHorses();
        if (!horseList.contains(horseProfile.getId())) {
            Timber.d("Adding a new horse");
            horseList.add(horseProfile.getId());
            userRiderProfile.setLastEditBy(userRiderProfile.getName());
            userRiderProfile.setLastEditDate(System.currentTimeMillis());
        }
        userRiderProfile.setOwnedHorses(horseList);
        realmService.createOrUpdateRiderProfileToRealm(userRiderProfile, new RealmService.OnTransactionCallback() {
            @Override
            public void onRealmSuccess() {
                RiderProfileApi.createOrUpdateRiderProfile(userRiderProfile);

                HorseProfileApi.createOrUpdateHorseProfile(horseProfile);
            }

            @Override
            public void onRealmError(Throwable e) {
                Timber.e("Error Creating/Updating Horse Profile: " + e);
            }
        });
    }

    /**
     * get's all horse profiles for user
     */
    void getHorseProfiles(List<String> ids) {
        checkViewAttached();
        List<HorseProfile> horseProfiles = new ArrayList<>();
        HorseProfileApi.getAllUsersHorses(ids, new HorseProfileApi.HorseProfileCallback() {
            @Override
            public void onSuccess(HorseProfile firebaseHorseProfile) {
                if (!horseProfiles.contains(firebaseHorseProfile)) {
                    horseProfiles.add(firebaseHorseProfile);
                }
                getMvpView().getHorseProfile(horseProfiles);
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e("Get Horse Profiles Error: " + throwable);
            }
        });
    }

    void deleteHorseProfile(String id) {
        if (userRiderProfile.getOwnedHorses().contains(id)) {
            for (int i = 0; i < userRiderProfile.getOwnedHorses().size(); i++) {
                if (userRiderProfile.getOwnedHorses().get(i).equals(id)) {
                    userRiderProfile.getOwnedHorses().remove(i);
                    userRiderProfile.setLastEditBy(userRiderProfile.getName());
                    userRiderProfile.setLastEditDate(System.currentTimeMillis());
                    realmService.createOrUpdateRiderProfileToRealm(userRiderProfile, new RealmService.OnTransactionCallback() {
                        @Override
                        public void onRealmSuccess() {
                            RiderProfileApi.createOrUpdateRiderProfile(userRiderProfile);
                            HorseProfileApi.deleteHorseProfile(id);
                        }

                        @Override
                        public void onRealmError(Throwable e) {

                        }
                    });
                    break;
                }
            }
        }
    }

    /**
     * Logout Current User
     */
    void logoutUser() {
        mAuth.signOut();
        new AccountManager().clearActiveUserAuthentication();
    }

    /**
     * Remove User from Firebase and Realm
     */
    void deleteUser() {
        realmService.deleteRiderProfileFromRealm(userRiderProfile.getEmail(), new RealmService.OnTransactionCallback() {
            @Override
            public void onRealmSuccess() {
                RiderProfileApi.deleteRiderProfile(userRiderProfile.getEmail());
                Timber.d("Deleted Profile");
                databaseReference
                        .child(Constants.RIDER_PROFILE)
                        .child(userRiderProfile.getEmail()).removeValue();
                new AccountManager().removeUser();
                logoutUser();
            }

            @Override
            public void onRealmError(Throwable e) {
                Timber.d("Error Deleting Profile; " + e);
            }
        });

    }

    /**
     * Auth listeners for Firebase
     */
    void removeAuthListener() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    void addAuthListener() {
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }
}
