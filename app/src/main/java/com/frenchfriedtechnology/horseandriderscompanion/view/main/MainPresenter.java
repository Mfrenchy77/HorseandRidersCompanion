package com.frenchfriedtechnology.horseandriderscompanion.view.main;

import com.frenchfriedtechnology.horseandriderscompanion.AccountManager;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.HorseProfileApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.RiderProfileApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.BaseListItem;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.HorseProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.events.HorseProfileCreateEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.HorseProfileDeleteEvent;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BasePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.otto.Subscribe;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;


public class MainPresenter extends BasePresenter<MainMvpView> {


    private RiderProfile riderProfile = new RiderProfile();

    @Inject
    public MainPresenter() {
    }

    @Inject
    FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    public void attachView(MainMvpView view) {
        super.attachView(view);


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


    //---- User's Profile
    void getRiderProfile(String email) {
        RiderProfileApi.getRiderProfile(email, new RiderProfileApi.RiderProfileCallback() {
            @Override
            public void onSuccess(RiderProfile firebaseProfile) {
                riderProfile = firebaseProfile;
                getMvpView().getUserProfile(riderProfile);
                new UserPrefs().setEditor(firebaseProfile.isEditor());
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e("Error retrieving RiderProfile from Firebase", throwable);
            }
        });
    }


    /**
     * Monitors Firebase RiderProfile for User and syncs to Realm if necessary
     */

    //-----Horse Profile Actions
    @Subscribe
    public void onCreateOrEditHorseProfile(HorseProfileCreateEvent event) {
        //create horse profile and add the id to a list in user's Rider Profile
        List<BaseListItem> horseList;
        HorseProfile horseProfile = event.getHorseProfile();
        horseProfile.setLastEditBy(AccountManager.currentUser());
        horseProfile.setLastEditDate(System.currentTimeMillis());
        horseList = riderProfile.getOwnedHorses();
        BaseListItem listItem = new BaseListItem();
        listItem.setId(horseProfile.getId());
        listItem.setName(horseProfile.getName());

        for (int i = 0; i < horseList.size(); i++) {
            if (horseList.get(i).getId() == horseProfile.getId()) {
                Timber.d("Updating a horse");
                horseList.remove(i);
                riderProfile.setLastEditBy(AccountManager.currentUser());
                riderProfile.setLastEditDate(System.currentTimeMillis());
            }
        }
        horseList.add(listItem);

        riderProfile.setOwnedHorses(horseList);
        RiderProfileApi.createOrUpdateRiderProfile(riderProfile);

        HorseProfileApi.createOrUpdateHorseProfile(horseProfile);

    }

    /**
     * get's all horse profiles for user
     */
    void getHorseProfiles(List<Long> ids) {
        checkViewAttached();
        getMvpView().getHorseProfiles(riderProfile.getOwnedHorses());
        List<HorseProfile> horseProfiles = new ArrayList<>();
        HorseProfileApi.getAllUsersHorses(ids, new HorseProfileApi.HorseProfileCallback() {
            @Override
            public void onSuccess(HorseProfile firebaseHorseProfile) {
                if (!horseProfiles.contains(firebaseHorseProfile)) {
                    horseProfiles.add(firebaseHorseProfile);
                }
/*
                getMvpView().getHorseProfiles(horseProfiles);
*/
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e("Get Horse Profiles Error: " + throwable);
            }
        });
    }

    void getHorseProfile(long id) {
        HorseProfileApi.getHorseProfile(id, new HorseProfileApi.HorseProfileCallback() {
            @Override
            public void onSuccess(HorseProfile firebaseHorseProfile) {
                Timber.d("got Horsey: " + firebaseHorseProfile.getName());
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e("Error retrieving HorseProfile: " + throwable);
            }
        });
    }


    @Subscribe
    public void onDeleteHorseEvent(HorseProfileDeleteEvent event) {

        if (riderProfile.getOwnedHorses().contains(event.getId())) {
            for (int i = 0; i < riderProfile.getOwnedHorses().size(); i++) {
                if (riderProfile.getOwnedHorses().get(i).getId() == event.getId()) {
                    riderProfile.getOwnedHorses().remove(i);
                    riderProfile.setLastEditBy(AccountManager.currentUser());
                    riderProfile.setLastEditDate(System.currentTimeMillis());

                    RiderProfileApi.createOrUpdateRiderProfile(riderProfile);
                    HorseProfileApi.deleteHorseProfile(event.getId());

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
     * Remove User from Firebase
     */
    void deleteUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        RiderProfileApi.deleteRiderProfile(user.getEmail());
        Timber.d("Deleted Profile");
        new AccountManager().removeUser();
        mAuth.getCurrentUser().delete();
        logoutUser();
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
