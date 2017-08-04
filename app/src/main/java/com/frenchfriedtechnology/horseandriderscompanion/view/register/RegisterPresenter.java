package com.frenchfriedtechnology.horseandriderscompanion.view.register;

import android.app.Activity;
import android.content.Context;

import com.frenchfriedtechnology.horseandriderscompanion.BusProvider;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.RiderProfileApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.AppPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.realmServices.RealmProfileService;
import com.frenchfriedtechnology.horseandriderscompanion.events.RegisterEvent;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BasePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Register User to Firebase and create a basic local copy of Rider Profile.
 */

public class RegisterPresenter extends BasePresenter<RegisterMvpView> {

    private FirebaseAuth.AuthStateListener authStateListener;
    private RealmProfileService realmProfileService;

    @Inject
    RegisterPresenter(RealmProfileService realmProfileService) {
        this.realmProfileService = realmProfileService;
    }

    @Inject
    FirebaseAuth auth;

    @Override
    public void attachView(RegisterMvpView view) {
        super.attachView(view);
        authStateListener = firebaseAuth -> {

            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null && user.getDisplayName() != null) {
                //user is authenticated
                createNewRiderProfile(user.getDisplayName(), user.getEmail(), user.getUid());
                Timber.d("user signed in: " + user.getDisplayName());
                BusProvider.getBusProviderInstance().post(
                        new RegisterEvent(true, "Successfully created user ", user.getDisplayName()));

            } else {
                Timber.d("user signed out");
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    void removeAuthListener() {
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }

    void addAuthListener() {
        if (authStateListener != null) {
            auth.addAuthStateListener(authStateListener);
        }
    }

    /**
     * Register User with Firebase and rename Display nome to chosen name
     */
    // TODO: 16/12/16 Need to send email confirmation if registering with email
    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    void register(Context context, String email, String password, String riderName) {
        checkViewAttached();
        getMvpView().showProgress();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, task -> {
                    if (!task.isSuccessful()) {
                        //registration Failed
                        getMvpView().hideProgress();
                        BusProvider.getBusProviderInstance().post(new RegisterEvent(false, task.getException().getMessage(), null));
                        Timber.e("Unsuccessfully Registered : " + task.getException().getMessage());
                    } else {
                        //registration Successful, change display name
                        FirebaseUser user = task.getResult().getUser();
                        getMvpView().showEmailVerificationDialog(user);
                        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(riderName)
                                .build();
                        if (auth.getCurrentUser() != null) {
                            auth.getCurrentUser().updateProfile(changeRequest).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    //this is needed for display name to show up in auth listener
                                    user.reload();
                                    auth.signOut();
                                    auth.signInWithEmailAndPassword(email, password);
                                    getMvpView().hideProgress();
                                } else {
                                    Timber.d("Error Changing Display Name");
                                }
                            });
                        }
                    }
                });
    }

    /**
     * Create new Rider Profile for Realm then Firebase
     */
    private void createNewRiderProfile(String riderName, String email, String uid) {
        //create a default RiderProfile
        Timber.d("create new Rider Profile: " + riderName);

        RiderProfile riderProfile = new RiderProfile();
        riderProfile.setId(uid);
        riderProfile.setEmail(email);
        riderProfile.setName(riderName);
        riderProfile.setLastEditBy(riderName);
        riderProfile.setLastEditDate(System.currentTimeMillis());
        realmProfileService.createOrUpdateRiderProfileToRealm(riderProfile, new RealmProfileService.RealmProfileCallback() {
            @Override
            public void onRealmSuccess() {
                RiderProfileApi.createOrUpdateRiderProfile(riderProfile);
                Timber.d("Created Realm Profile");
            }

            @Override
            public void onRealmError(Throwable e) {
                Timber.e("Error creating RealmRider: " + e);
            }
        });

        AppPrefs.setActiveUser(riderName);
        new UserPrefs().setUserId(uid);
        new UserPrefs().setUserEmail(email);
    }
}
