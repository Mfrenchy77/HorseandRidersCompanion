package com.frenchfriedtechnology.horseandriderscompanion.view.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.events.RegisterEvent;
import com.frenchfriedtechnology.horseandriderscompanion.util.DialogFactory;
import com.frenchfriedtechnology.horseandriderscompanion.util.ViewUtil;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BaseActivity;
import com.frenchfriedtechnology.horseandriderscompanion.view.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Activity for Registering a new User
 */
public class RegisterActivity extends BaseActivity implements RegisterMvpView {

    @Inject
    RegisterPresenter registerPresenter;

    private TextInputEditText inputPassword, inputName, inputEmail;
    private ProgressBar progressBar = null;
    private ScrollView root;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);

        registerPresenter.attachView(this);

        root = (ScrollView) findViewById(R.id.register_root);
        inputName = (TextInputEditText) findViewById(R.id.register_input_name);
        inputPassword = (TextInputEditText) findViewById(R.id.register_password);
        inputEmail = (TextInputEditText) findViewById(R.id.register_input_email);

        Button register = (Button) findViewById(R.id.button_register);
        register.setOnClickListener(view -> registerClicked());

        TextView loginLink = (TextView) findViewById(R.id.link_login);
        loginLink.setOnClickListener(view -> gotoLogin());
        ViewUtil.hideKeyboard(this);
    }

    /**
     * Attempt to register new account, send email verification
     */
    void registerClicked() {
        ViewUtil.hideKeyboard(this);
        String name = inputName.getText().toString();
        String email = inputEmail.getText().toString().toLowerCase();
        String password = inputPassword.getText().toString();
        if (name.isEmpty()) {
            inputName.setError("Must enter a UserName");
            inputName.setFocusable(true);
            return;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Please enter a valid Email");
            inputEmail.setFocusable(true);
            return;
        }
        if (password.isEmpty()) {
            inputPassword.setError("Please enter a Password");
            inputPassword.setFocusable(true);
            return;
        }
        registerPresenter.register(context, email, password, name);
    }

    /**
     * Navigate to Login Activity
     */
    void gotoLogin() {
        //goto login activity
        LoginActivity.start(context);
    }

    @Override
    protected void onDestroy() {
        registerPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        registerPresenter.addAuthListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        registerPresenter.removeAuthListener();
    }

    @Subscribe
    public void onRegisterEvent(RegisterEvent event) {
        if (event.isSuccess()) {
            finish();
        } else {
            hideProgress();
            DialogFactory.showErrorSnackBar
                    (context, findViewById(android.R.id.content), new Throwable(event.getMessage())).show();
        }
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

    @Override
    public void showEmailVerificationDialog(FirebaseUser user) {
        Timber.d("Show email verification dialog");
        new AlertDialog.Builder(context)
                .setTitle("Email Verification Needed")
                .setMessage("Send Email To " + user.getEmail() + "?")
                .setCancelable(true)
                .setPositiveButton("OK", (dialog, which) -> {
                    user.sendEmailVerification().addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Snackbar.make(root,
                                    "Email sent to " + user.getEmail(),
                                    Snackbar.LENGTH_SHORT).show();
                            Timber.d("Verification Email Sent");
                        } else {
                            Timber.e("Error Sending Verification Email");
                        }
                    });
                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }
}
