package com.frenchfriedtechnology.horseandriderscompanion.data.endpoints;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;
import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.DataSnapshotMapper;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;

import java.util.List;


/**
 * Crud interface for Firebase and Levels of Skill Tree
 */

public class LevelsApi {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public void createOrEditLevel(Level level, String path) {
        //create Level
        databaseReference.child(path)
                .child(Constants.LEVELS)
                .child(level.getId())
                .setValue(level);
    }

    public void getLevels(String path, LevelCallback callback) {
        //get all Levels
        RxFirebaseDatabase.observeValueEvent(databaseReference
                .child(path)
                .child(Constants.LEVELS), DataSnapshotMapper.listOf(Level.class))
                .subscribe(callback::onSuccess, callback::onError);
    }

    public void deleteLevel(String deletedLevelId, String path) {
        //delete Level
        databaseReference.child(path)
                .child(Constants.LEVELS)
                .child(deletedLevelId)
                .removeValue();

    }

    public interface LevelCallback {

        void onSuccess(List<Level> levels);

        void onError(final Throwable throwable);
    }
}
