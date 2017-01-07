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

    public void createOrUpdateResource(Resource resource, ResourceCreatedCallback callaback) {
        databaseReference
                .child(Constants.RESOURCES)
                .child(resource.getId())
                .setValue(resource).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callaback.onSuccess();
            } else {
                callaback.onError("Error creating Resource: " + task.getResult().toString());
            }
        });
    }

    public void getAllResources(ResourcesCallback callback) {
        //get all resources
        RxFirebaseDatabase.observeValueEvent(databaseReference
                .child(Constants.RESOURCES), DataSnapshotMapper.listOf(Resource.class))
                .subscribe(callback::onSuccess, callback::onError);
    }

    public void deleteResource(Resource resource) {
        databaseReference
                .child(Constants.RESOURCES)
                .child(resource.getId())
                .removeValue();
    }

    public interface ResourcesCallback {

        void onSuccess(List<Resource> resources);

        void onError(final Throwable throwable);
    }

    public interface ResourceCreatedCallback {
        void onSuccess();

        void onError(final String error);
    }

}
