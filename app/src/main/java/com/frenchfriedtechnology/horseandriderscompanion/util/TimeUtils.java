package com.frenchfriedtechnology.horseandriderscompanion.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.widget.EditText;

import com.frenchfriedtechnology.horseandriderscompanion.HorseAndRidersCompanion;
import com.frenchfriedtechnology.horseandriderscompanion.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class TimeUtils {
    private String format = "MM/dd/yy";

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
        Timber.d("Now: " + now);
        Timber.d("EditDate: " + editDate);
        long difference = now - editDate;
        Timber.d("difference: " + difference);
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

    /**
     * return a String from a long for a representation of a date
     *
     * @param dateMillis long representation of date
     * @return String from dateMillis
     */
    public String millisToDate(long dateMillis) {
        Date date = new Date(dateMillis);
        return new SimpleDateFormat(format, Locale.US).format(date);
    }

    /**
     * return a long from a String to represent a date
     *
     * @param dateToChange String of date
     * @return date as millis
     */
    public long dateToMillis(String dateToChange) {
        Date date = null;
        try {
            date = new SimpleDateFormat(format, Locale.US).parse(dateToChange);
        } catch (ParseException e) {
            Timber.e(e.getMessage());
        }
        return date != null ? date.getTime() : 0;
    }

    /**
     * returns a string to the view after choosing from date picker dialog
     *
     * @param context    activity context
     * @param targetView view to set the date string on
     */
    public void datePicker(Context context, EditText targetView) {

        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        Calendar currentDate = Calendar.getInstance();
        if (!targetView.getText().toString().equals("")) {
            currentDate.setTime(new Date(dateToMillis(targetView.getText().toString())));
        }
        DatePickerDialog.OnDateSetListener onDateSetListener =
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    targetView.setText(sdf.format(newDate.getTime()));

                };

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                onDateSetListener,
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
