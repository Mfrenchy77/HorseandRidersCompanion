package com.frenchfriedtechnology.horseandriderscompanion.view.login;

import android.app.Activity;
import android.content.Context;

import com.frenchfriedtechnology.horseandriderscompanion.BusProvider;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.AppPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
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

    private FirebaseAuth.AuthStateListener authListener;
    private Context context;

    @Inject
    LoginPresenter() {
    }

    @Inject
    FirebaseAuth mAuth;

    /**
     * This main authorization check happens here
     * if(user != null) Firebase authorization is successful
     * <p>
     * Create or update Realm for user's profile, on Success allow Login to Open Main Activity
     */
    // TODO: 03/08/17 add email verification check
    @Override
    public void attachView(LoginMvpView view) {
        super.attachView(view);
        authListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Timber.d("user signed in");
                //user is authorized--create or update Realm
                if (!emailVerified(user)) {
                    Timber.d("Email Not Verified");
                    getMvpView().showEmailVerifyDialog(user);
                } else {
                    //user already has a saved profile, send to main activity
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

    private boolean emailVerified(FirebaseUser user) {
        Timber.d("email verification check");
        return user.isEmailVerified();
    }

    void logoutUser() {
        mAuth.signOut();
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
