package com.frenchfriedtechnology.horseandriderscompanion.di.component;

import android.app.Application;
import android.content.Context;

import com.frenchfriedtechnology.horseandriderscompanion.di.ApplicationContext;
import com.frenchfriedtechnology.horseandriderscompanion.di.module.ApplicationModule;
import com.frenchfriedtechnology.horseandriderscompanion.util.RxEventBus;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();


    RxEventBus eventBus();

    Application application();

    FirebaseAuth firebaseAuth();
}
