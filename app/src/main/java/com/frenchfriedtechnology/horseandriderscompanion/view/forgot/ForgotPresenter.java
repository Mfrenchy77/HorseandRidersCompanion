package com.frenchfriedtechnology.horseandriderscompanion.view.forgot;

import android.app.Activity;
import android.content.Context;

import com.frenchfriedtechnology.horseandriderscompanion.BusProvider;
import com.frenchfriedtechnology.horseandriderscompanion.events.ForgotEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.RegisterEvent;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BasePresenter;
import com.google.firebase.auth.FirebaseAuth;


import javax.inject.Inject;

import timber.log.Timber;


public class ForgotPresenter extends BasePresenter<ForgotMvpView> {

    @Inject
    ForgotPresenter() {
    }

    @Inject
    FirebaseAuth firebaseAuth;

    @Override
    public void attachView(ForgotMvpView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    void resetPassword(Context context, String email) {
        checkViewAttached();
        getMvpView().showProgress();
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener((Activity) context, task -> {
                    getMvpView().hideProgress();

                    if (!task.isSuccessful()) {
                        BusProvider.getBusProviderInstance().post(new RegisterEvent(false, task.getException().getMessage(), null));
                        Timber.e("Unsuccessfully Reset Password : " + task.getException().getMessage());
                    } else {
                        BusProvider.getBusProviderInstance().post(new ForgotEvent(true, "Reset password successfully, Please check your email"));
                        Timber.e("Reset password successfully, Please check your email");
                    }
                });
    }
}
