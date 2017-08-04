package com.frenchfriedtechnology.horseandriderscompanion.view.register;

import com.frenchfriedtechnology.horseandriderscompanion.view.base.MvpView;
import com.google.firebase.auth.FirebaseUser;

interface RegisterMvpView extends MvpView {

    void showProgress();

    void hideProgress();

    void showEmailVerificationDialog(FirebaseUser user);
}

