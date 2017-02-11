package com.frenchfriedtechnology.horseandriderscompanion.data.endpoints;

import android.support.annotation.NonNull;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;
import com.frenchfriedtechnology.horseandriderscompanion.util.ViewUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelvinapps.rxfirebase.DataSnapshotMapper;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;


import java.util.List;


import javax.inject.Inject;

import rx.Observable;
import timber.log.Timber;

/**
 * Interface for Firebase and Rider's Profile
 */

public class RiderProfileApi {
    @Inject
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
                .child(new ViewUtil().convertEmailToPath(riderProfile.getEmail()))
                .setValue(riderProfile)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Timber.d("Created/Updated Firebase Profile");
                    }
                });
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

    public Observable<RiderProfile> getRiderProfileObservable(String email) {
        Timber.d("getRiderProfileObservable() called: " + email);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        return RxFirebaseDatabase.observeValueEvent(databaseReference.child(Constants.RIDER_PROFILE)
                .child(new ViewUtil().convertEmailToPath(email)), DataSnapshotMapper.of(RiderProfile.class)).asObservable();
    }

    /**
     * retrieve all RiderProfiles
     */
/*
    public Observable<RiderProfile> getRider(String email) {
        //get riderProfile
        return null;
    }*/

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
