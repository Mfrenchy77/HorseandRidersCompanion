package com.frenchfriedtechnology.horseandriderscompanion;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.v7.app.AppCompatDelegate;

import com.frenchfriedtechnology.horseandriderscompanion.di.component.ApplicationComponent;
import com.frenchfriedtechnology.horseandriderscompanion.di.component.DaggerApplicationComponent;
import com.frenchfriedtechnology.horseandriderscompanion.di.module.ApplicationModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

public class HorseAndRidersCompanion extends Application {
    private static HorseAndRidersCompanion instance;

    /**
     * Sets the Global Theme: Auto Day/Night
     **/
    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    public HorseAndRidersCompanion() {
        instance = this;
    }

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        boolean isDebuggable = (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));

        if (isDebuggable) {
            Timber.plant(new Timber.DebugTree());
        }

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(getResources().getString(R.string.app_name))
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }


    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    public static HorseAndRidersCompanion get(Context context) {
        return (HorseAndRidersCompanion) context.getApplicationContext();
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Timber.e("########## onLowMemory ##########");
    }
}
