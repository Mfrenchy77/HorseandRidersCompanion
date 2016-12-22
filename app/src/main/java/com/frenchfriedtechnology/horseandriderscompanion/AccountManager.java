package com.frenchfriedtechnology.horseandriderscompanion;

import android.content.Context;
import android.content.Intent;

import com.frenchfriedtechnology.horseandriderscompanion.data.local.AppPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.view.login.LoginActivity;

/**
 * Handles management of user accounts, including authentication/switching between accounts.
 */

public final class AccountManager {


    public final void clearActiveUserAuthentication() {
        Context context = HorseAndRidersCompanion.getContext();

        AppPrefs.setActiveUser(null);

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

    }

    public void removeUser() {
        AppPrefs.removeUserFromAccounts(currentUser());
        clearActiveUserAuthentication();
    }

    public static String currentUser() {
        UserPrefs userPrefs = new UserPrefs();
        return (userPrefs.getUser());
    }

}
