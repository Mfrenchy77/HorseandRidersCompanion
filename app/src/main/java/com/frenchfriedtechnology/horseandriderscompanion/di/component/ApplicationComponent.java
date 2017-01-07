package com.frenchfriedtechnology.horseandriderscompanion.di.component;

import android.app.Application;
import android.content.Context;

import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.realmServices.RealmProfileService;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.realmServices.RealmSkillTreeService;
import com.frenchfriedtechnology.horseandriderscompanion.data.sync.ProfileSyncer;
import com.frenchfriedtechnology.horseandriderscompanion.data.sync.SkillTreeSyncer;
import com.frenchfriedtechnology.horseandriderscompanion.di.ApplicationContext;
import com.frenchfriedtechnology.horseandriderscompanion.di.module.ApplicationModule;
import com.frenchfriedtechnology.horseandriderscompanion.util.RxEventBus;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    Realm realm();

    RxEventBus eventBus();

    Application application();

    FirebaseAuth firebaseAuth();

    ProfileSyncer profileSyncer();

    SkillTreeSyncer skillTreeSyncer();

    RealmProfileService realmProfileService();

    RealmSkillTreeService realmSkillTreeService();
}
