package com.frenchfriedtechnology.horseandriderscompanion.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.frenchfriedtechnology.horseandriderscompanion.view.login.LoginActivity;

/**
 * Splash Screen to Show while App is Launching and data is Loading
 */

public class Launcher extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

