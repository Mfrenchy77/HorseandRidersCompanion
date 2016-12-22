package com.frenchfriedtechnology.horseandriderscompanion.data.endpoints;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;
import com.frenchfriedtechnology.horseandriderscompanion.util.ViewUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;


import timber.log.Timber;

/**
 * Interface for Firebase and Rider's Profile
 */

public class RiderProfileApi {
    public RiderProfileApi() {
    }

    /**
     * create a RidersProfile for user
     *
     * @param riderProfile profile
     */

    public static void createOrUpdateRiderProfile(RiderProfile riderProfile) {
        //create profile
        Timber.d("createOrUpdateRiderProfile() called");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Constants.RIDER_PROFILE)
                .child(new ViewUtil().convertEmailToPath(riderProfile.getEmail())).setValue(riderProfile);
    }

    /**
     * Retrieve the user's profile
     *
     * @param email      user's email
     * @param onCallback callback
     */
    public static void getRiderProfile(String email, RiderProfileCallback onCallback) {
        Timber.d("getRiderProfile() called: " + email);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Constants.RIDER_PROFILE)
                .child(new ViewUtil().convertEmailToPath(email))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        RiderProfile profile = dataSnapshot.getValue(RiderProfile.class);
                        onCallback.onSuccess(profile);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        onCallback.onError(databaseError.toException());
                    }
                });
    }

    /**
     * retrieve all RiderProfiles
     */

    public List<RiderProfile> getAllProfiles() {
        //get all profiles
        return null;
    }

    /**
     * Delete user's RiderProfile
     *
     * @param email profile to be deleted
     */

    public static void deleteRiderProfile(String email) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference
                .child(Constants.RIDER_PROFILE)
                .child(new ViewUtil().convertEmailToPath(email)).removeValue();
    }

    /**
     * Callback for sending info back to Presenters
     */
    public interface RiderProfileCallback {

        void onSuccess(RiderProfile firebaseProfile);

        void onError(final Throwable throwable);
    }

}
