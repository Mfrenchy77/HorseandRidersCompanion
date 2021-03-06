package com.frenchfriedtechnology.horseandriderscompanion.view.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.frenchfriedtechnology.horseandriderscompanion.BusProvider;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.HorseProfile;
import com.frenchfriedtechnology.horseandriderscompanion.events.HorseProfileCreateEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.HorseProfileDeleteEvent;
import com.frenchfriedtechnology.horseandriderscompanion.util.ViewUtil;

import org.parceler.Parcels;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import timber.log.Timber;

/**
 * Dialog to edit or create a Horse Profile
 */

public class DialogHorseProfile extends DialogFragment {


    @StringDef({NEW_HORSE, EDIT_HORSE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Tags {
    }

    private static final String HORSE_PROFILE = "HorseProfile";
    public static final String EDIT_HORSE = "EDIT_HORSE";
    public static final String NEW_HORSE = "NEW_HORSE";

    private TextInputEditText horseProfileName, horseProfileBreed, horseProfileDob, horseProfileAge,
            horseProfileColor, horseProfileHeight, horseProfileCurrentOwner,
            horseProfileDateOfPurchase, horseProfilePurchasePrice;
    public static final String TAG = "TAG";

    public static DialogHorseProfile newInstance(@StringRes String tag,
                                                 @Nullable HorseProfile horseProfile) {

        Bundle args = new Bundle();
        args.putParcelable(HORSE_PROFILE, Parcels.wrap(horseProfile));
        args.putString(TAG, tag);

        DialogHorseProfile fragment = new DialogHorseProfile();
        fragment.setArguments(args);
        return fragment;
    }

    private String tag;
    private HorseProfile horseProfile = new HorseProfile();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = getArguments().getString(TAG);
        horseProfile = Parcels.unwrap(getArguments().getParcelable(HORSE_PROFILE));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final FrameLayout fl = new FrameLayout(getActivity());
        final View view = inflater.inflate(R.layout.dialog_horse_profile, fl, false);
        fl.addView(view);

        horseProfileName = (TextInputEditText) view.findViewById(R.id.horse_name);
        horseProfileBreed = (TextInputEditText) view.findViewById(R.id.horse_breed);
        horseProfileDob = (TextInputEditText) view.findViewById(R.id.horse_dob);
        horseProfileAge = (TextInputEditText) view.findViewById(R.id.horse_age);
        horseProfileColor = (TextInputEditText) view.findViewById(R.id.horse_color);
        horseProfileHeight = (TextInputEditText) view.findViewById(R.id.horse_height);
        horseProfileCurrentOwner = (TextInputEditText) view.findViewById(R.id.horse_owner);
        horseProfileDateOfPurchase = (TextInputEditText) view.findViewById(R.id.horse_date_of_purchase);
        horseProfilePurchasePrice = (TextInputEditText) view.findViewById(R.id.horse_purchase_price);

        TextView dialogTitle = (TextView) view.findViewById(R.id.dialog_title);

        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.delete_item_button);
        deleteButton.setVisibility(tag.equals(EDIT_HORSE) ? View.VISIBLE : View.GONE);
        deleteButton.setOnClickListener(view1 -> {
            if (horseProfile.getHorseName() != null) {
                BusProvider.getBusProviderInstance().post(new HorseProfileDeleteEvent(horseProfile.getId()));
                dismiss();
            } else {
                Timber.d("DeleteHorseProfile() Error:  name is null");
            }
        });
        dialogTitle.setText(tag.equals(EDIT_HORSE) ? "Edit Horse Profile" : "Create New Horse Profile");

        //Edit HorseProfile()
        if (tag.equals(EDIT_HORSE)) {
            horseProfileName.setText(horseProfile.getHorseName());
            horseProfileBreed.setText(horseProfile.getBreed());
////set millis to date            horseProfileDob.setText(horseProfile.getDateOfBirth());
//            horseProfileDateOfPurchase.setText(horseProfile.getDateOfPurchase());
//            horseProfilePurchasePrice.setText(horseProfile.getPurchasePrice());
            horseProfileAge.setText(horseProfile.getAge());
            horseProfileColor.setText(horseProfile.getColor());
            horseProfileHeight.setText(horseProfile.getHeight());
            horseProfileCurrentOwner.setText(horseProfile.getCurrentOwner());
        }

        LinearLayout createButton = (LinearLayout) view.findViewById(R.id.accept_button_horse);
        createButton.setOnClickListener(v -> {
            String name = horseProfileName.getText().toString().trim();
            String owner = horseProfileCurrentOwner.getText().toString().trim();

            // TODO: 12/12/16 Flesh out this entire interface more, open date picker for stuff ect
            if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(owner)) {
                HorseProfile horseProfile = new HorseProfile();
                horseProfile.setId(tag.equals(EDIT_HORSE) ? horseProfile.getId() : ViewUtil.createId());
                horseProfile.setHorseName(horseProfileName.getText().toString());
                horseProfile.setBreed(horseProfileBreed.getText().toString());
//            horseProfile.setDateOfBirth(horseProfileDob.getText());
//            horseProfile.setDateOfPurchase(horseProfileDateOfPurchase.getText());
//            horseProfile.setPurchasePrice(horseProfilePurchasePrice.getText());
                horseProfile.setAge(horseProfileAge.getText().toString());
                horseProfile.setColor(horseProfileColor.getText().toString());
                horseProfile.setHeight(horseProfileHeight.getText().toString());
                horseProfile.setCurrentOwner(horseProfileCurrentOwner.getText().toString());
                BusProvider.getBusProviderInstance().post(new HorseProfileCreateEvent(horseProfile));
                dismiss();
            } else {
                Toast.makeText(getActivity(), "Fields must not be left blank", Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout cancelButton = (LinearLayout) view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> dismiss());
        builder.setView(fl);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        BusProvider.getBusProviderInstance().register(this);

        dialog.show();

        return dialog;
    }
}
