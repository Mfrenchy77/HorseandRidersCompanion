package com.frenchfriedtechnology.horseandriderscompanion.di.module;

import android.app.Application;
import android.content.Context;

import com.frenchfriedtechnology.horseandriderscompanion.HorseAndRidersCompanion;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.di.ApplicationContext;
import com.google.firebase.auth.FirebaseAuth;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Provide application-level dependencies.
 */

@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    RealmConfiguration provideRealmConfiguration(@ApplicationContext Context context) {
        Realm.init(context);
        RealmConfiguration.Builder builder = new RealmConfiguration.Builder();
        builder.name(String.valueOf(R.string.app_name));
        builder.deleteRealmIfMigrationNeeded();
        return builder.build();
    }

    @Provides
    @Singleton
    FirebaseAuth provideFireBaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    Realm provideRealm(RealmConfiguration realmConfiguration) {
        return Realm.getInstance(realmConfiguration);
    }
}
