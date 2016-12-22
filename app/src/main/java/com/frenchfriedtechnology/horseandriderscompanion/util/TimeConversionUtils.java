package com.frenchfriedtechnology.horseandriderscompanion.util;

import android.content.res.Resources;

import com.frenchfriedtechnology.horseandriderscompanion.HorseAndRidersCompanion;
import com.frenchfriedtechnology.horseandriderscompanion.R;

import java.util.concurrent.TimeUnit;

public class TimeConversionUtils {

    /**
     * @return a human readable string indicating when the item was last edited
     */
    public static String lastEditDate(long lastEditDate) { // TODO unit test
        long now = System.currentTimeMillis();
        Resources res = HorseAndRidersCompanion.getContext().getResources();


        return res.getString(R.string.edit_time_wild_card,
                getTimeDifferenceAsString(now, lastEditDate));

    }


    private static String getTimeDifferenceAsString(long now, long editDate) {

        long difference = editDate - now;

        int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(difference);
        Resources res = HorseAndRidersCompanion.getContext().getResources();

        if (seconds > 0 && seconds < 60) {
            return res.getQuantityString(R.plurals.time_seconds, seconds, seconds);
        }

        int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(difference);

        if (minutes > 0 && minutes < 60) {
            return res.getQuantityString(R.plurals.time_minutes, minutes, minutes);
        }
        int hours = (int) TimeUnit.MILLISECONDS.toHours(difference);

        if (hours > 0 && hours < 24) {
            return res.getQuantityString(R.plurals.time_hours, hours, hours);
        }

        int days = (int) TimeUnit.MILLISECONDS.toDays(difference);

        if (days > 0 && days < 8) {
            return res.getQuantityString(R.plurals.time_days, days, days);
        }
        long totalSeconds = difference / 1000;
        int weeks = (int) (totalSeconds / (1000 * 60 * 60 * 24) % 7);

        if (weeks > 0 && weeks < 5) {
            return res.getQuantityString(R.plurals.time_weeks, weeks, weeks);
        }

        int months = weeks / 30;

        if (months > 0 && months < 12) {
            return res.getQuantityString(R.plurals.time_months, months, months);
        }

        int years = months / 365;

        if (years > 0) {
            return res.getQuantityString(R.plurals.time_years, years, years);
        }

        return res.getString(R.string.time_unknown);
    }
}
