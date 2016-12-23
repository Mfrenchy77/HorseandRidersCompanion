package com.frenchfriedtechnology.horseandriderscompanion.data.endpoints;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.DataSnapshotMapper;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;

import java.util.List;

/**
 * Crud interface for Firebase and Resources
 */

public class ResourcesApi {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public void createOrEditResource(String levelId, Resource resource) {
        databaseReference.child(Constants.RESOURCES)
                .child(levelId).child(resource.getId())
                .setValue(resource);
    }

    public void getResourcesForLevel(String id, ResourcesCallback callback) {
        //get resources for levelId
        RxFirebaseDatabase.observeValueEvent(databaseReference
                .child(Constants.RESOURCES)
                .child(id), DataSnapshotMapper.listOf(Resource.class))
                .subscribe(callback::onSuccess, callback::onError);
    }

    public void deleteResource(String levelId, Resource resource) {
        databaseReference.child(Constants.RESOURCES)
                .child(levelId).child(resource.getId())
                .removeValue();
    }

    public interface ResourcesCallback {

        void onSuccess(List<Resource> resources);

        void onError(final Throwable throwable);
    }


}
