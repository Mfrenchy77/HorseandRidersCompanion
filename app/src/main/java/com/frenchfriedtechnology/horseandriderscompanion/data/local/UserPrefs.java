package com.frenchfriedtechnology.horseandriderscompanion.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.frenchfriedtechnology.horseandriderscompanion.HorseAndRidersCompanion;

import net.derohimat.baseapp.util.BasePreferenceUtils;

/**
 * Preferences for individual User's for the App on device
 */

public class UserPrefs extends BasePreferenceUtils {

    private static final String KEY_DAY_NIGHT_MODE = "KEY_DAY_NIGHT_MODE";
    private static final String KEY_NIGHT_MODE = "KEY_NIGHT_MODE";
    private static final String KEY_INSTRUCTOR = "KEY_INSTRUCTOR";
    private static final String KEY_EDIT_MODE = "Key_Edit_Mode";
    private static final String KEY_USER_EDIT = "Key_User_Edit";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_ID = "user_id";

    private String user;

    /**
     * Retrieves the preferences for the currently active user.
     */
    public UserPrefs() {
        this(AppPrefs.getActiveUser());
    }

    /**
     * Retrieves the preferences for the given user, or creates a file with defaults if none exist
     * already.
     *
     * @param username the username
     * @throws IllegalStateException if username is empty
     */
    public UserPrefs(String username) {
        this.user = username;
    }

    /**
     * @return the username for the current preferences
     */
    public String getUser() {
        return user;
    }

    public String getUserId() {
        return getSharedPrefs().getString(KEY_USER_ID, null);
    }

    public void setEditor(boolean editor) {
        editSharedPrefs().putBoolean(KEY_USER_EDIT, editor).apply();
    }

    public boolean isEditor() {
        return getSharedPrefs().getBoolean(KEY_USER_EDIT, false);
    }

    public void setEditMode(boolean editMode) {
        editSharedPrefs().putBoolean(KEY_EDIT_MODE, editMode).apply();
    }

    public boolean isEditMode() {
        return getSharedPrefs().getBoolean(KEY_EDIT_MODE, false);
    }

    public void setUserId(String userId) {
        editSharedPrefs().putString(KEY_USER_ID, userId).apply();
    }

    public String getUserEmail() {
        return getSharedPrefs().getString(KEY_USER_EMAIL, null);
    }

    public void setUserEmail(String email) {
        editSharedPrefs().putString(KEY_USER_EMAIL, email).apply();
    }

    /**
     * @return true if night mode, otherwise false
     */
    public boolean isNightMode() {
        return getSharedPrefs().getBoolean(KEY_NIGHT_MODE, true);
    }

    /**
     * Sets whether night mode should be displayed or not
     *
     * @param nightMode true if night mode, otherwise false
     */
    public void setNightMode(boolean nightMode) {
        editSharedPrefs().putBoolean(KEY_NIGHT_MODE, nightMode).apply();
    }

    /**
     * @return true if Day/Night enabled, otherwise false
     */
    public boolean isDayNightMode() {
        return getSharedPrefs().getBoolean(KEY_DAY_NIGHT_MODE, false);
    }

    /**
     * Sets whether Day/Night mode should be enabled or not
     *
     * @param dayNight true if auto day/night mode, otherwise false
     */
    public void setDayNightMode(boolean dayNight) {
        editSharedPrefs().putBoolean(KEY_DAY_NIGHT_MODE, dayNight).apply();
    }

    /**
     * @return true if Instructor abilities enabled, otherwise false
     */
    public boolean isInstructor() {
        return getSharedPrefs().getBoolean(KEY_INSTRUCTOR, false);
    }

    /**
     * Sets whether Instructor abilities enabled or not
     *
     * @param instructor toggle instructor ability
     */
    public void setInstructor(boolean instructor) {
        editSharedPrefs().putBoolean(KEY_INSTRUCTOR, instructor).apply();
    }

    /**
     * Primary methods to get and edit Shared Prefs
     */
    private SharedPreferences getSharedPrefs() {
        return HorseAndRidersCompanion.getContext().getSharedPreferences(user, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor editSharedPrefs() {
        return getSharedPrefs().edit();
    }

}
