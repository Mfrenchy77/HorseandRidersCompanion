package com.frenchfriedtechnology.horseandriderscompanion.view.resources;

import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.ResourcesApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BasePresenter;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Submit new Resource
 */

public class CreateResourcePresenter extends BasePresenter<CreateResourceMvpView> {
    @Inject
    CreateResourcePresenter() {
    }


    void submitNewResource(Resource resource) {
        getMvpView().setPosting(true);
        new ResourcesApi().createOrUpdateResource(resource, new ResourcesApi.ResourceCreatedCallback() {
            @Override
            public void onSuccess() {
                getMvpView().setPosting(false);
            }

            @Override
            public void onError(String error) {
                getMvpView().setPosting(false);
                Timber.e(error);
            }
        });
    }
}
