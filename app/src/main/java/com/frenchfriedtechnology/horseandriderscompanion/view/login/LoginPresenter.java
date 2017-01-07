package com.frenchfriedtechnology.horseandriderscompanion.view.login;

import android.app.Activity;
import android.content.Context;

import com.frenchfriedtechnology.horseandriderscompanion.BusProvider;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.AppPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.realmServices.RealmProfileService;
import com.frenchfriedtechnology.horseandriderscompanion.events.LoginEvent;
import com.frenchfriedtechnology.horseandriderscompanion.util.DialogFactory;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BasePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import javax.inject.Inject;

import timber.log.Timber;

/**
 * Login for User's who have already created an account, if not saved to app yet a local copy
 * will be created
 */
public class LoginPresenter extends BasePresenter<LoginMvpView> {

    private final RealmProfileService realmProfileService;
    private FirebaseAuth.AuthStateListener authListener;
    private Context context;

    @Inject
    LoginPresenter(RealmProfileService realmProfileService) {
        this.realmProfileService = realmProfileService;
    }

    @Inject
    FirebaseAuth mAuth;

    /**
     * This main authorization check happens here
     * if(user != null) Firebase authorization is successful
     * <p>
     * Create or update Realm for user's profile, on Success allow Login to Open Main Activity
     */
    @Override
    public void attachView(LoginMvpView view) {
        super.attachView(view);
        authListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Timber.d("user signed in");
                //user is authorized--create or update Realm

                RiderProfile riderProfile = realmProfileService.getUsersRiderProfile(user.getEmail());
                if (riderProfile == null) {
                    //No Profile saved in Realm
                    //create a default profile, if user has a firebase account it will download
                    //in Main Activity
                    riderProfile = new RiderProfile();
                    riderProfile.setEmail(user.getEmail());
                    riderProfile.setName(user.getDisplayName());
                    riderProfile.setId(user.getUid());
                    riderProfile.setLastEditBy(user.getDisplayName());
                    realmProfileService.createOrUpdateRiderProfileToRealm(riderProfile, new RealmProfileService.RealmProfileCallback() {
                        @Override
                        public void onRealmSuccess() {
                            //Realm profile created
                            RiderProfile profile = realmProfileService.getUsersRiderProfile(user.getEmail());
                            //setup App preferences for user
                            AppPrefs.setActiveUser(profile.getName());
                            new UserPrefs().setUserEmail(profile.getEmail());
                            new UserPrefs().setUserId(profile.getId());
                            //send to main activity
                            BusProvider.getBusProviderInstance().post(new LoginEvent(true, "User signed in", user.getEmail()));
                        }

                        @Override
                        public void onRealmError(Throwable e) {
                            DialogFactory.createGenericErrorDialog(context, "Realm Error: " + e);
                            Timber.e("Error creating Realm Profile: " + e);
                        }
                    });
                } else {
                    //user already has a saved profile, send to main activity
                    new UserPrefs().setEditor(riderProfile.isEditor());
                    BusProvider.getBusProviderInstance().post(new LoginEvent(true, "User signed in", user.getEmail()));
                }
            } else {
                Timber.d("user signed out");
            }
        };
    }

    /**
     * Login to Firebase
     */
    void login(Context context, String email, String password) {
        checkViewAttached();
        getMvpView().showProgress();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, task -> {
                    if (!task.isSuccessful()) {
                        BusProvider.getBusProviderInstance().post(new LoginEvent(false, task.getException().getMessage(), email));
                    }
                    getMvpView().hideProgress();
                });
    }

    void removeAuthListener() {
        if (authListener != null) {
            mAuth.removeAuthStateListener(authListener);
        }
    }

    void addAuthListener() {
        if (authListener != null) {
            mAuth.addAuthStateListener(authListener);
        }
    }
}
