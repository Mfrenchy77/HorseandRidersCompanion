package com.frenchfriedtechnology.horseandriderscompanion.data.endpoints;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Category;
import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.DataSnapshotMapper;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;

import java.util.List;

import timber.log.Timber;

/**
 * Crud interface for Firebase and Categories
 */

public class CategoriesApi {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public void createOrEditCategory(Category category, String path) {
        Timber.d("CreateOrEditCategory() called for: " + path);
        //create Category
        databaseReference.child(path)
                .child(Constants.CATEGORIES)
                .child(category.getName())
                .setValue(category);
    }

    public void getCategories(String path, CategoryCallback callback) {
        //get all categories
        RxFirebaseDatabase.observeValueEvent(databaseReference
                .child(path)
                .child(Constants.CATEGORIES), DataSnapshotMapper.listOf(Category.class))
                .subscribe(callback::onSuccess, callback::onError);
    }

    public void deleteCategory(String deletedCategoryName, String path) {
        //delete Category
        databaseReference.child(path)
                .child(Constants.CATEGORIES)
                .child(deletedCategoryName)
                .removeValue();
    }

    public interface CategoryCallback {

        void onSuccess(List<Category> categories);

        void onError(final Throwable throwable);
    }
}
