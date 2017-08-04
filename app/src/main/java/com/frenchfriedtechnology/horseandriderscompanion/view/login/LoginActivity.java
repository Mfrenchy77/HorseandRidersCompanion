package com.frenchfriedtechnology.horseandriderscompanion.view.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.frenchfriedtechnology.horseandriderscompanion.AccountManager;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.events.LoginEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.SwitchAccountEvent;
import com.frenchfriedtechnology.horseandriderscompanion.util.DialogFactory;
import com.frenchfriedtechnology.horseandriderscompanion.util.ViewUtil;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BaseActivity;
import com.frenchfriedtechnology.horseandriderscompanion.view.dialogs.DialogSwitchAccount;
import com.frenchfriedtechnology.horseandriderscompanion.view.forgot.ForgotActivity;
import com.frenchfriedtechnology.horseandriderscompanion.view.main.MainActivity;
import com.frenchfriedtechnology.horseandriderscompanion.view.register.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Activity for User to Login
 */
public class LoginActivity extends BaseActivity implements LoginMvpView {

    @Inject
    LoginPresenter mPresenter;

    private ProgressBar mProgressBar = null;
    private TextInputEditText inputEmail;
    private EditText inputPassword;
    private ScrollView root;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);

        mPresenter.attachView(this);

        root = (ScrollView) findViewById(R.id.login_root);
        inputEmail = (TextInputEditText) findViewById(R.id.login_email);
        inputPassword = (EditText) findViewById(R.id.login_password);
        Button loginButton = (Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(view -> loginClicked());
        TextView forgotPassword = (TextView) findViewById(R.id.forgot_password);
        forgotPassword.setOnClickListener(view -> gotoForgot());
        TextView registerButton = (TextView) findViewById(R.id.login_link_register);
        registerButton.setOnClickListener(view -> gotoRegister());
        ImageView loginAccounts = (ImageView) findViewById(R.id.login_account_button);
        loginAccounts.setOnClickListener(view -> onClickAccountSwitch());
        ViewUtil.hideKeyboard(this);
    }

    /**
     * Opens Dialog with other user's who have signed in previously
     */
    void onClickAccountSwitch() {
        new DialogSwitchAccount().show(getSupportFragmentManager(), null);
    }

    /**
     * Attempts to login to Firebase Auth
     */
    void loginClicked() {
        ViewUtil.hideKeyboard(this);
        String email = inputEmail.getText().toString().toLowerCase();
        String password = inputPassword.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Please enter a valid email");
            inputEmail.setFocusable(true);
            return;
        }
        if (password.isEmpty() || password.length() < 6 || password.length() > 16) {
            inputPassword.setError("Please enter a valid password 6-16 characters");
            inputPassword.setFocusable(true);
            return;
        }
        mPresenter.login(context, email, password);
    }

    /**
     * Switched to Registration Activity
     */
    void gotoRegister() {
        mPresenter.removeAuthListener();
        RegisterActivity.start(context);
    }

    /**
     * Switches to Forgot Password Activity
     */
    void gotoForgot() {
        ForgotActivity.start(context, inputEmail.getText().toString());
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.addAuthListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.removeAuthListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.removeAuthListener();
    }

    @Override
    public void showProgress() {
        if (mProgressBar == null) {
            mProgressBar = DialogFactory.DProgressBar(context);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showEmailVerifyDialog(FirebaseUser user) {
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
        Snackbar.make(root, "Email not verified", Snackbar.LENGTH_SHORT).show();
    }

    @Subscribe
    public void LoginEvent(LoginEvent event) {
        if (event.isSuccess()) {
            MainActivity.start(context, event.getEmail(), true);
            finish();
        } else {
            DialogFactory.showErrorSnackBar(context, findViewById(android.R.id.content), new Throwable(event.getMessage())).show();
        }
    }

    @Subscribe
    public void accountSwitched(SwitchAccountEvent event) {
        inputEmail.setText(new UserPrefs().getUserEmail());
        Timber.d("Switched Account to: " + AccountManager.currentUser() + " Email: " + new UserPrefs().getUserEmail());
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
