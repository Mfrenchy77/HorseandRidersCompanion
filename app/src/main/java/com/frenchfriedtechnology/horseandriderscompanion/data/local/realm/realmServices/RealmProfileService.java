package com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.realmServices;


import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.RealmRiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.mappers.RiderProfileMapper;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Service to create and retrieve user's Rider Profile from Realm
 */
@Singleton
public class RealmProfileService {

    private final Provider<Realm> realmProvider;

    @Inject
    public RealmProfileService(Provider<Realm> realmProvider) {
        this.realmProvider = realmProvider;
    }

    /**
     * Get the User's Rider Profile
     */
    public RiderProfile getUsersRiderProfile(String email) {
        Timber.d("getUserRiderProfile() called");
        final Realm realm = realmProvider.get();
        RealmRiderProfile realmRiderProfile;
        realmRiderProfile = realm.where(RealmRiderProfile.class).equalTo("email", email).findFirst();
        if (realmRiderProfile != null) {
            return new RiderProfileMapper().realmToEntity(realm.copyFromRealm(realmRiderProfile));
        } else {
            return null;
        }
    }

    public Observable<RiderProfile> getUsersRiderProfileObservable(String email) {
        Timber.d("getUserRiderProfileObservable() called");
        final Realm realm = realmProvider.get();
        RealmRiderProfile realmRiderProfile = realm.where(RealmRiderProfile.class).equalTo("email", email).findFirst();

        return Observable.just(new RiderProfileMapper().realmToEntity(realmRiderProfile));
    }


    /**
     * Create or update user's Rider Profile
     */
    public void createOrUpdateRiderProfileToRealm(RiderProfile riderProfile, final RealmProfileCallback onTransactionCallback) {
        Timber.d("createOrUpdateRiderProfileToRealm() called");
        Realm realm = null;
        try {
            realm = realmProvider.get();
            realm.executeTransactionAsync(realm1 -> {
                RealmRiderProfile realmRiderProfile = new RiderProfileMapper().entityToRealm(riderProfile);
                realm1.copyToRealmOrUpdate(realmRiderProfile);
            }, () -> {
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmSuccess();
                }
            }, error -> {
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmError(error);
                }
            });
        } catch (Exception e) {
            Timber.e(e, "There was an error while adding in Realm.");
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public Observable<RiderProfile> createOrUpdateRiderProfileObservable(RiderProfile riderProfile) {
        Timber.d("createOrUpdateRiderProfile() called");
        return Observable.create(new Observable.OnSubscribe<RiderProfile>() {
            @Override
            public void call(Subscriber<? super RiderProfile> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Realm realm = null;
                try {
                    realm = realmProvider.get();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealmOrUpdate(new RiderProfileMapper().entityToRealm(riderProfile));
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            subscriber.onNext(riderProfile);
                        }
                    });
                } catch (Exception e) {
                    Timber.e(e, "There was an error while adding in Realm.");
                    subscriber.onError(e);
                } finally {
                    if (realm != null) {
                        realm.close();
                    }
                }
            }
        });
    }

    /**
     * Delete user's Rider Profile
     */
    public void deleteRiderProfileFromRealm(String email, final RealmProfileCallback onTransactionCallback) {
        Timber.d("deleteRiderProfileFromRealm() called");
        Realm realm = null;
        try {
            realm = realmProvider.get();
            realm.executeTransactionAsync(realm1 -> {
                RealmRiderProfile profile = realm1.where(RealmRiderProfile.class).equalTo("email", email).findFirst();
                profile.deleteFromRealm();
            }, () -> {
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmSuccess();
                }
            }, error -> {
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmError(error);
                }
            });
        } catch (Exception e) {
            Timber.e(e, "There was an error while deleting in Realm.");
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public Observable deleteRiderProfileFromRealmObservable(String email) {
        Timber.d("deleteRiderProfile() called");
        return Observable.create(new Observable.OnSubscribe<RiderProfile>() {
            @Override
            public void call(Subscriber<? super RiderProfile> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Realm realm = null;
                try {
                    realm = realmProvider.get();
                    realm.executeTransactionAsync(realm1 -> {
                        RealmRiderProfile profile = realm1.where(RealmRiderProfile.class).equalTo("email", email).findFirst();
                        profile.deleteFromRealm();
                    }, () -> subscriber.onNext(null), subscriber::onError);
                } catch (Exception e) {
                    Timber.e(e, "There was an error while deleting in Realm.");
                } finally {
                    if (realm != null) {
                        realm.close();
                    }
                }
            }
        });
    }

// TODO: 27/12/16 Think about using Observables here, when you are smarter

    /**
     * Callbacks to notify presenters
     */
    public interface RealmProfileCallback {
        void onRealmSuccess();

        void onRealmError(final Throwable e);
    }
}