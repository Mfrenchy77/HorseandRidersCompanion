package com.frenchfriedtechnology.horseandriderscompanion.data.local;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.frenchfriedtechnology.horseandriderscompanion.HorseAndRidersCompanion;

import java.util.HashSet;
import java.util.Set;

/**
 * Global App Prefs, Total User Account's created on device
 */

public class AppPrefs {

    private static final String USER_LIST = "USER_LIST";
    private static final String AUTH_CURRENT_USER = "AUTH_CURRENT_USER";

    /**
     * @return the current active user
     */
    public static String getActiveUser() {
        return getDefaultSharedPrefs().getString(AUTH_CURRENT_USER, null);
    }

    /**
     * Sets the username of the currently active user
     *
     * @param userName the username
     */
    public static void setActiveUser(String userName) {
        addUserToAccounts(userName);
        editDefaultSharedPrefs().putString(AUTH_CURRENT_USER, userName).apply();
    }

    /**
     * @return the list of user accounts which have been logged into the app at least once.
     */
    public static Set<String> getUserAccounts() {
        Set<String> accountSet = getDefaultSharedPrefs().getStringSet(USER_LIST, new HashSet<>());
        accountSet.remove(null);
        accountSet.remove("");
        return getDefaultSharedPrefs().getStringSet(USER_LIST, new HashSet<>());
    }

    /**
     * Adds a user to the list of accounts available.
     *
     * @param userName the username to be added
     */
    private static void addUserToAccounts(String userName) {
        Set<String> userList = getUserAccounts();

        if (!userList.contains(userName)) {
            userList.add(userName);
        }
        editDefaultSharedPrefs().remove(USER_LIST).apply();
        editDefaultSharedPrefs().putStringSet(USER_LIST, userList).apply();
    }

    /**
     * Removes a user from the list of available accounts
     *
     * @param userName the user to be removed
     */
    public static void removeUserFromAccounts(String userName) {
        Set<String> userList = getUserAccounts();

        if (userList.contains(userName)) {
            userList.remove(userName);
        }
        editDefaultSharedPrefs().remove(USER_LIST).apply();
        editDefaultSharedPrefs().putStringSet(USER_LIST, userList).apply();
    }

    private static SharedPreferences getDefaultSharedPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(HorseAndRidersCompanion.getContext());
    }

    private static SharedPreferences.Editor editDefaultSharedPrefs() {
        return getDefaultSharedPrefs().edit();
    }

}
