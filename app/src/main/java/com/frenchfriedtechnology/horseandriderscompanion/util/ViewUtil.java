package com.frenchfriedtechnology.horseandriderscompanion.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.inputmethod.InputMethodManager;

import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ViewUtil {
    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static SecureRandom rnd = new SecureRandom();

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    // TODO: 22/12/16 replace this with a better obfuscator
    public String convertEmailToPath(String email) {
        return email.replace(".", "^");
    }

    public String revertPathToEmail(String path) {
        return path.replace("^", ".");
    }

    public static long convertIdToNumber(String id) {
        long converted = 0;
        for (int i = 0; i < id.length(); i++) {
            converted += id.charAt(i) - 'a' + 1;
        }
        return converted;

    }

    public static String createId() {
        int length = 28;
        StringBuilder randomStringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            randomStringBuilder.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return randomStringBuilder.toString();
    }

    public static boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

}

