package com.frenchfriedtechnology.horseandriderscompanion.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckedTextView;

import com.frenchfriedtechnology.horseandriderscompanion.BusProvider;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.events.ThemeChangedEvent;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BaseActivity;

/**
 * Activity to adjust Settings in the App and details for User
 */

public class SettingsActivity extends BaseActivity {

    private AppCompatCheckedTextView nightModeSwitch, dayNightSwitch, instructorSwitch;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_settings;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        nightModeSwitch = (AppCompatCheckedTextView) findViewById(R.id.night_mode_switch);
        nightModeSwitch.setOnClickListener(view -> onNightModeCheckChange());
        nightModeSwitch.setChecked(new UserPrefs().isNightMode());
        nightModeSwitch.setEnabled(!new UserPrefs().isDayNightMode());

        dayNightSwitch = (AppCompatCheckedTextView) findViewById(R.id.auto_theme_switch);
        dayNightSwitch.setChecked(new UserPrefs().isDayNightMode());
        dayNightSwitch.setOnClickListener(view -> onAutoThemeChange());

        instructorSwitch = (AppCompatCheckedTextView) findViewById(R.id.instructor_switch);
        instructorSwitch.setChecked(new UserPrefs().isInstructor());
        instructorSwitch.setOnClickListener(view -> onChangeInstructor());
    }

    /**
     * Change to global Theme to either day or night
     */
    private void onNightModeCheckChange() {
        nightModeSwitch.setChecked(!nightModeSwitch.isChecked());
        new AlertDialog.Builder(this)
                .setTitle(R.string.change_theme)
                .setMessage(new UserPrefs().isNightMode() ? R.string.change_theme_to_day :
                        R.string.change_theme_to_night)
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    nightModeSwitch.setChecked(new UserPrefs().isNightMode());
                    dialog.dismiss();
                })
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    new UserPrefs().setNightMode(nightModeSwitch.isChecked());
                    BusProvider.getBusProviderInstance().post(new ThemeChangedEvent());
                    finish();
                }).show();

    }

    /**
     * Change global Theme to auto switching between day and night based on user's
     * location and time
     */
    void onAutoThemeChange() {
        dayNightSwitch.setChecked(!dayNightSwitch.isChecked());
        new AlertDialog.Builder(this)
                .setTitle(R.string.change_theme)
                .setMessage(new UserPrefs().isDayNightMode() ? R.string.change_theme_to_night :
                        R.string.change_theme_to_auto)
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    dayNightSwitch.setChecked(new UserPrefs().isDayNightMode());
                    dialog.dismiss();
                })
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    new UserPrefs().setDayNightMode(dayNightSwitch.isChecked());
                    finish();
                }).show();

    }

    /**
     * Set user as Instructor, allows user to add or remove Riders and Horses to their profile
     * and update their skill trees
     */
    void onChangeInstructor() {
        instructorSwitch.setChecked(!instructorSwitch.isChecked());
        new AlertDialog.Builder(this)
                .setTitle("Enable/Disable Instructor ability")
                .setMessage(R.string.instructor_dialog)
                .setNegativeButton(android.R.string.cancel,
                        (dialog, i) -> {
                            instructorSwitch.setChecked(new UserPrefs().isInstructor());
                            dialog.dismiss();
                        })
                .setPositiveButton(new UserPrefs().isInstructor() ? getString(R.string.disable) :
                        getString(R.string.enable), (dialog, i) -> {
                    new UserPrefs().setInstructor(instructorSwitch.isChecked());
                    dialog.dismiss();
                }).show();
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, SettingsActivity.class));
    }
}
