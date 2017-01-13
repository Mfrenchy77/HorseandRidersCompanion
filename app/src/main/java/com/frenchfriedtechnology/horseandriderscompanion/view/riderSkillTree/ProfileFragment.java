package com.frenchfriedtechnology.horseandriderscompanion.view.riderSkillTree;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.HorseProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.util.TimeUtils;

import org.parceler.Parcels;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import timber.log.Timber;

public class ProfileFragment extends Fragment {

    @StringDef({HORSE_PROFILE, RIDER_PROFILE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Tags {
    }

    public static final String RIDER_PROFILE = "RIDER_PROFILE";
    public static final String HORSE_PROFILE = "HORSE_PROFILE";
    public static final String TAG = "TAG";

    public ProfileFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ProfileFragment newInstance(@StringRes String tag, @Nullable HorseProfile horseProfile, @Nullable RiderProfile riderProfile) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(TAG, tag);
        args.putParcelable(HORSE_PROFILE, Parcels.wrap(horseProfile));
        args.putParcelable(RIDER_PROFILE, Parcels.wrap(riderProfile));
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    private String tag;
    private HorseProfile horseProfile;
    private RiderProfile riderProfile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = getArguments().getString(TAG);
        horseProfile = Parcels.unwrap(getArguments().getParcelable(HORSE_PROFILE));
        riderProfile = Parcels.unwrap(getArguments().getParcelable(RIDER_PROFILE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(tag.equals(RIDER_PROFILE) ?
                R.layout.fragment_rider_profile : R.layout.fragment_horse_profile, container, false);
        if (tag.equals(RIDER_PROFILE)) {
            inflateRiderView(rootView);
        } else {
            inflateHorseView(rootView);
        }
        return rootView;

    }

    private void inflateRiderView(View rootView) {
        Timber.d("ProfileName: " + riderProfile.getName());

        TextView profileName = (TextView) rootView.findViewById(R.id.profile_rider_name);
        profileName.setText(riderProfile.getName());

        TextView editDate = (TextView) rootView.findViewById(R.id.profile_rider_last_edit);
        editDate.setText(String.format("Edited: %s %s",
                TimeUtils.lastEditDate(riderProfile.getLastEditDate()), " ago"));

        TextView editBy = (TextView) rootView.findViewById(R.id.profile_rider_last_edit_by);
        editBy.setText(String.format("By: %s", riderProfile.getLastEditBy()));

        TextView email = (TextView) rootView.findViewById(R.id.profile_rider_email);
        email.setText(String.format("Email: %s", riderProfile.getEmail()));

        TextView subscription = (TextView) rootView.findViewById(R.id.profile_rider_subscription);
        subscription.setText(String.format("Subscribed: %s", riderProfile.isSubscribed()));

        TextView editor = (TextView) rootView.findViewById(R.id.profile_rider_editor);
        if (riderProfile.isEditor()) {
            editor.setText("You have been adorned with the \"Editor\" ability," +
                    " this allows you to make significant changes to the way " +
                    "Horse & Rider's Companion Looks.\n\nUse it Wisely!");
        }
    }

    private void inflateHorseView(View rootView) {
        Timber.d("ProfileName: " + horseProfile.getName());

        TextView profileName = (TextView) rootView.findViewById(R.id.profile_horse_name);
        profileName.setText(horseProfile.getName());

        TextView editDate = (TextView) rootView.findViewById(R.id.profile_horse_last_edit);
        editDate.setText(String.format("Edited: %s %s",
                TimeUtils.lastEditDate(horseProfile.getLastEditDate()), " ago"));

        TextView editBy = (TextView) rootView.findViewById(R.id.profile_horse_last_edit_by);
        editBy.setText(String.format("By: %s", horseProfile.getLastEditBy()));

        TextView breedAndColor = (TextView) rootView.findViewById(R.id.profile_horse_breed);
        breedAndColor.setText(String.format("%s - %s", horseProfile.getBreed(), horseProfile.getColor()));

        TextView horseAge = (TextView) rootView.findViewById(R.id.profile_horse_age);
        horseAge.setText(String.format("Age: %s - Dob: %s", horseProfile.getAge(), new TimeUtils().millisToDate(horseProfile.getDateOfBirth())));

        TextView horseHeight = (TextView) rootView.findViewById(R.id.profile_horse_height);
        horseHeight.setText(String.format("Height: %s", horseProfile.getHeight()));

        TextView horseOwner = (TextView) rootView.findViewById(R.id.profile_horse_owner);
        horseOwner.setText(String.format("Owned By: %s", horseProfile.getCurrentOwner()));

        TextView horseLastPurchase = (TextView) rootView.findViewById(R.id.profile_horse_purchase);
        horseLastPurchase.setText(String.format("Last Purchased: %s For: %s",
                new TimeUtils().millisToDate(horseProfile.getDateOfPurchase()),
                horseProfile.getPurchasePrice()));


    }


}
