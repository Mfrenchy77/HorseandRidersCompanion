package com.frenchfriedtechnology.horseandriderscompanion.view.login;

import com.frenchfriedtechnology.horseandriderscompanion.view.base.MvpView;
import com.google.firebase.auth.FirebaseUser;


interface LoginMvpView extends MvpView {

    void showProgress();

    void hideProgress();

    void showEmailVerifyDialog(FirebaseUser user);

}
