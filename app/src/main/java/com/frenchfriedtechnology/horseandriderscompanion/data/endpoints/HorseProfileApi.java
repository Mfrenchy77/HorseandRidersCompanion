package com.frenchfriedtechnology.horseandriderscompanion.data.endpoints;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.HorseProfile;
import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import timber.log.Timber;

/**
 * Interface for Firebase and HorseProfile
 */

public class HorseProfileApi {

    /**
     * create a HorsesProfile for user
     *
     * @param horseProfile profile
     */

    public static void createOrUpdateHorseProfile(HorseProfile horseProfile) {
        //create Horse profile
        Timber.d("createOrUpdateHorseProfile() called");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Constants.HORSE_PROFILE)
                .child(horseProfile.getId()).setValue(horseProfile);
    }

    /**
     * retrieve all horse profiles for user
     *
     * @param ids horses' ids
     */
    public static void getAllUsersHorses(List<String> ids, HorseProfileCallback callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        //retrieve users profile
        for (String id : ids) {
            databaseReference.child(Constants.HORSE_PROFILE)
                    .child(id)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HorseProfile horseProfile = dataSnapshot.getValue(HorseProfile.class);
                            callback.onSuccess(horseProfile);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            callback.onError(databaseError.toException());
                        }
                    });
        }

    }

    /**
     * Retrieve Horse Profile
     *
     * @param id horse's id
     */

    public static void getHorseProfile(String id, HorseProfileCallback callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Constants.HORSE_PROFILE)
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HorseProfile horseProfile = dataSnapshot.getValue(HorseProfile.class);
                        callback.onSuccess(horseProfile);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onError(databaseError.toException());
                    }
                });
    }

    /**
     * Delete Horse Profile
     *
     * @param id horse's id
     */

    public static void deleteHorseProfile(String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Constants.HORSE_PROFILE)
                .child(id).removeValue();
    }

    public interface HorseProfileCallback {

        void onSuccess(HorseProfile firebaseHorseProfile);

        void onError(final Throwable throwable);
    }

}
