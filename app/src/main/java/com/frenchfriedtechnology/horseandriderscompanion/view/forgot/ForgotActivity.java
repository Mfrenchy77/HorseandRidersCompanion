package com.frenchfriedtechnology.horseandriderscompanion.view.forgot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.events.ForgotEvent;
import com.frenchfriedtechnology.horseandriderscompanion.util.DialogFactory;
import com.frenchfriedtechnology.horseandriderscompanion.util.ViewUtil;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BaseActivity;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

/**
 * If user forgot password Firebase will send the email on record a reset password
 */
public class ForgotActivity extends BaseActivity implements ForgotMvpView {

    private TextInputEditText inputEmail;

    private static final String RESET_EMAIL = "RESET_EMAIL";

    @Inject
    ForgotPresenter forgotPresenter;
    private ProgressBar progressBar = null;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_forgot;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);

        forgotPresenter.attachView(this);

        inputEmail = (TextInputEditText) findViewById(R.id.reset_password_email);
        inputEmail.setText(getIntent().getStringExtra(RESET_EMAIL));
        Button resetPassword = (Button) findViewById(R.id.button_reset_password);
        resetPassword.setOnClickListener(view -> resetPassword());
        ViewUtil.hideKeyboard(this);
    }

    @Override
    protected void onDestroy() {
        forgotPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Subscribe
    public void onEvent(ForgotEvent event) {
        if (event.isSuccess()) {
            DialogFactory.createSimpleOkDialog(context, getString(R.string.app_name), event.getMessage()).show();
        } else {
            DialogFactory.showErrorSnackBar(context, findViewById(android.R.id.content), new Throwable(event.getMessage())).show();
        }
    }

    /**
     * Send the email a notice to reset password
     */
    // TODO: 16/12/16 Implement this!!!
    void resetPassword() {
        ViewUtil.hideKeyboard(this);
        String email = inputEmail
                .getText().toString();
        if (TextUtils.isEmpty(email)) {
            inputEmail
                    .setError("Please enter a valid Email");
            inputEmail
                    .setFocusable(true);
            return;
        }
        if (!ViewUtil.isEmailValid(email)) {
            inputEmail
                    .setError("Format Email Error");
            inputEmail
                    .setFocusable(true);
            return;
        }
        forgotPresenter.resetPassword(context, email);
    }

    @Override
    public void showProgress() {
        if (progressBar == null) {
            progressBar = DialogFactory.DProgressBar(context);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    public static void start(Context context, String email) {
        Intent intent = new Intent(context, ForgotActivity.class);
        intent.putExtra(RESET_EMAIL, email);
        context.startActivity(intent);
    }
}

