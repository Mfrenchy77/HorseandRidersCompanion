package com.frenchfriedtechnology.horseandriderscompanion.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.frenchfriedtechnology.horseandriderscompanion.R;

import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.android.gms.internal.zzs.TAG;


public class ViewUtil {

    private String height, hands, inches;
    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static SecureRandom rnd = new SecureRandom();
    /**
     * @param dp a Density-Independent Pixel value
     * @return the pixels for the current screen
     */
    public static float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem()
                .getDisplayMetrics());
    }

    // TODO: 22/12/16 replace this with a better obfuscator
    public String convertEmailToPath(String email) {
        String convertedEmail = email.replace(".", "666");
        convertedEmail = convertedEmail.replace("@", "999");
        convertedEmail = convertedEmail.replace("f", "5");
        convertedEmail = convertedEmail.replace("e", "1");
        convertedEmail = convertedEmail.replace("g", "2");
        convertedEmail = convertedEmail.replace("m", "");
        convertedEmail = convertedEmail.replace("a", "p");
        convertedEmail = convertedEmail.replace("i", "a");
        convertedEmail = convertedEmail.replace("co", "l");

        return convertedEmail;
    }

    public String revertPathToEmail(String path) {

        String convertedEmail = path.replace("666", ".");
        convertedEmail = convertedEmail.replace("999", "@");
        convertedEmail = convertedEmail.replace("5", "f");
        convertedEmail = convertedEmail.replace("1", "e");
        convertedEmail = convertedEmail.replace("2", "g");
        convertedEmail = convertedEmail.replace("!", "m");
        convertedEmail = convertedEmail.replace("p", "a");
        convertedEmail = convertedEmail.replace("a", "i");
        convertedEmail = convertedEmail.replace("l", "co");

        return convertedEmail;
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

    /**
     * Checks of String contains a number
     *
     * @param s string to check
     * @return true if a digit is detected
     */
    public final boolean containsLetter(String s) {
        boolean containsLetter = true;

        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (!Character.isDigit(c)) {
                    containsLetter = true;
                    break;
                }
                containsLetter = false;
            }
        }
        return containsLetter;
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

    /**
     * show a dialog to choose the height of a horse
     */
    public void horseHeightChooser(Activity context, EditText editText) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.horse_height_picker, null);
        builder.setTitle("Horse's Height");
        builder.setMessage("Select Horses Height");
        builder.setView(view);
        TextView inchesText = (TextView) view.findViewById(R.id.number_picker_inches_text);
        TextView handsText = (TextView) view.findViewById(R.id.number_picker_hands_text);

        final NumberPicker handsNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker_hands);
        final NumberPicker inchesNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker_inches);
        handsNumberPicker.setMaxValue(22);
        handsNumberPicker.setMinValue(1);
        if (editText.getText() != null && !editText.getText().toString().equals("")) {
            String total = editText.getText().toString();
            String[] split = total.split("\\,");
            int hands = Integer.parseInt(split[0]);
            int inches = Integer.parseInt(split[1]);
            handsNumberPicker.setValue(hands);
            inchesNumberPicker.setValue(inches);
        } else {
            handsNumberPicker.setValue(15);
        }
        handsNumberPicker.setWrapSelectorWheel(false);
        handsNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker handsNumberPicker, int i, int i1) {
                Log.d(TAG, "onValueChange: " + i + " secondVale: " + i1);
                handsText.setText(i1 + " Hands ");
            }
        });
        inchesNumberPicker.setMaxValue(11);
        inchesNumberPicker.setMinValue(1);
        inchesNumberPicker.setWrapSelectorWheel(false);
        inchesNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker inchesNumberPicker, int i, int i1) {
                Log.d(TAG, "onValueChange: " + i + " secondVale: " + i1);
                inchesText.setText(i1 + " Inches");
            }
        });
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "onClick: " + height);
                height = String.format("%s,%s", String.valueOf(handsNumberPicker.getValue()),
                        String.valueOf(inchesNumberPicker.getValue()));

                editText.setText(height);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}

