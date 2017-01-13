package com.frenchfriedtechnology.horseandriderscompanion.view.main;

import com.frenchfriedtechnology.horseandriderscompanion.AccountManager;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.HorseProfileApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.RiderProfileApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.BaseListItem;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.HorseProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.realmServices.RealmProfileService;
import com.frenchfriedtechnology.horseandriderscompanion.data.sync.ProfileSyncer;
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


    private ProfileSyncer profileSyncer;
    private final RealmProfileService realmProfileService;
    private RiderProfile riderProfile = new RiderProfile();
    private RiderProfile remoteRiderProfile = new RiderProfile();

    @Inject
    public MainPresenter(RealmProfileService realmProfileService,
                         ProfileSyncer profileSyncer) {
        this.realmProfileService = realmProfileService;
        this.profileSyncer = profileSyncer;
    }

    @Inject
    FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private boolean profileFetched = false;

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

    @Override
    public void detachView() {
        super.detachView();
    }

    //---- User's Profile
    void getRiderProfile(String email) {
        riderProfile = realmProfileService.getUsersRiderProfile(email);
        getMvpView().getUserProfile(riderProfile);
        RiderProfileApi.getRiderProfile(email, new RiderProfileApi.RiderProfileCallback() {
            @Override
            public void onSuccess(RiderProfile firebaseProfile) {
                remoteRiderProfile = firebaseProfile;
                new UserPrefs().setEditor(firebaseProfile.isEditor());

                getMvpView().getUserProfile(profileSyncer.syncProfile(riderProfile, firebaseProfile));
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
        if (remoteRiderProfile != null) {
            getMvpView().getUserProfile(profileSyncer.syncProfile(riderProfile, remoteRiderProfile));
        } else if (riderProfile != null) {
            getMvpView().getUserProfile(riderProfile);
        }


      /*  checkViewAttached();
        getProfileSubscription = riderProfileHandler.getRealmRiderProfile(email).observeOn(AndroidSchedulers.mainThread())
                .subscribe(realmRiderProfile -> getMvpView().getUserProfile(new RiderProfileMapper().realmToEntity(realmRiderProfile)));
        if (!profileFetched) {
            Timber.d("getRiderProfile() called: " + email);
            RxUtil.unsubscribe(getProfileSubscription);
       */   /*  getProfileSubscription = profileService.getRealmRiderProfile(email)
                    .subscribe(realmRiderProfile ->
                            getMvpView().getUserProfile(
                                    new RiderProfileMapper().realmToEntity(realmRiderProfile)));
*/
//            createUpdateProfileSubscription = profileService.createUpdateRealmRiderProfile(new RiderProfileMapper().entityToRealm(userRiderProfile))
//                    .subscribe(realmRiderProfile -> getMvpView().getUserProfile(new RiderProfileMapper().realmToEntity(realmRiderProfile)), Throwable::printStackTrace);


            /*  subscription = dataManager.getRiderProfile(email)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<RiderProfile>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                            Timber.e("Error fetching RiderProfile", e);
                        }

                        @Override
                        public void onNext(RiderProfile riderProfile) {
                            if (riderProfile == null) {
                                //show empty view
                                Timber.d("Profile is Null");
                            } else {
                                userRiderProfile = riderProfile;
                                Log.d("MainPresenter", "\r\nUser's Rider Profile: " + riderProfile.getLevelName()
                                        + "\r\nEmail: " + riderProfile.getEmail()
                                        + "\r\nId: " + riderProfile.getId()
                                        + "\r\nLast Edit by: " + riderProfile.getLastEditBy()
                                        + "\r\nLast Edit date: " + riderProfile.getLastEditDate()
                                        + "\r\nEditor: " + riderProfile.isEditor()
                                        + "\r\nSubscribed: " + riderProfile.isSubscribed()
                                        + "\r\nSubscription date: " + riderProfile.getSubscriptionDate()
                                        + "\r\nSubscription end date:" + riderProfile.getSubscriptionEndDate()
                                        + "\r\nSkill Level size: " + riderProfile.getSkillLevels().size()
                                        + "\r\nOwned Horses size: " + riderProfile.getOwnedHorses().size());
                                getMvpView().getUserProfile(riderProfile);
                                monitorFirebaseProfile(email);
                            }
                        }
                    });*/
    }


    /**
     * Monitors Firebase RiderProfile for User and syncs to Realm if necessary
     */

  /*  private void monitorFirebaseProfile(String email) {
        RiderProfileApi.getRiderProfile(email, new RiderProfileApi.RiderProfileCallback() {
            @Override
            public void onSuccess(RiderProfile firebaseProfile) {
                Log.d("MainPresenter", "\r\nUser's FireBase Profile: " + firebaseProfile.getLevelName()
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
                new UserPrefs().setEditor(userRiderProfile.isEditor());

                getMvpView().getUserProfile(new ProfileSyncer().syncProfile(userRiderProfile, firebaseProfile, realmProfileService));
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e(throwable.toString());
            }
        });
    }*/

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
        realmProfileService.createOrUpdateRiderProfileToRealm(riderProfile, new RealmProfileService.RealmProfileCallback() {
            @Override
            public void onRealmSuccess() {
                RiderProfileApi.createOrUpdateRiderProfile(riderProfile);

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
    void getHorseProfiles(List<Long> ids) {
        checkViewAttached();
        List<HorseProfile> horseProfiles = new ArrayList<>();
        HorseProfileApi.getAllUsersHorses(ids, new HorseProfileApi.HorseProfileCallback() {
            @Override
            public void onSuccess(HorseProfile firebaseHorseProfile) {
                if (!horseProfiles.contains(firebaseHorseProfile)) {
                    horseProfiles.add(firebaseHorseProfile);
                }
                getMvpView().getHorseProfiles(horseProfiles);
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
                    realmProfileService.createOrUpdateRiderProfileToRealm(riderProfile, new RealmProfileService.RealmProfileCallback() {
                        @Override
                        public void onRealmSuccess() {
                            RiderProfileApi.createOrUpdateRiderProfile(riderProfile);
                            HorseProfileApi.deleteHorseProfile(event.getId());
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
        /*subscription = */
        realmProfileService.deleteRiderProfileFromRealm(riderProfile.getEmail(), new RealmProfileService.RealmProfileCallback() {
            @Override
            public void onRealmSuccess() {
                logoutUser();
            }

            @Override
            public void onRealmError(Throwable e) {
                Timber.e(e.toString());
            }
        });
      /*  deleteProfileSubscription = riderProfileHandler.deleteRealmRiderProfile(new RiderProfileMapper().entityToRealm(userRiderProfile))
                .subscribe(aVoid -> logoutUser());
        profileService.deleteRealmRiderProfile(new RiderProfileMapper().entityToRealm(userRiderProfile))
                .subscribe(aVoid -> logoutUser());
        dataManager.deleteRiderProfile(userRiderProfile.getEmail())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e.toString());
                    }

                    @Override
                    public void onNext(Object o) {
                        logoutUser();
                    }
                })*/
        ;
        logoutUser();
      /*   realmProfileService.deleteRiderProfileFromRealm(userRiderProfile.getEmail(), new RealmProfileService.RealmProfileCallback() {
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
*/
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
