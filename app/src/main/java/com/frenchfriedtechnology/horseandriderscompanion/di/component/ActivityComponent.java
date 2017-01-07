package com.frenchfriedtechnology.horseandriderscompanion.di.component;

import com.frenchfriedtechnology.horseandriderscompanion.di.PerActivity;
import com.frenchfriedtechnology.horseandriderscompanion.di.module.ActivityModule;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BaseSkillTreeActivity;
import com.frenchfriedtechnology.horseandriderscompanion.view.forgot.ForgotActivity;
import com.frenchfriedtechnology.horseandriderscompanion.view.horseSkillTree.HorseSkillTreeActivity;
import com.frenchfriedtechnology.horseandriderscompanion.view.login.LoginActivity;
import com.frenchfriedtechnology.horseandriderscompanion.view.main.MainActivity;
import com.frenchfriedtechnology.horseandriderscompanion.view.register.RegisterActivity;
import com.frenchfriedtechnology.horseandriderscompanion.view.resources.CreateResourceActivity;
import com.frenchfriedtechnology.horseandriderscompanion.view.riderSkillTree.RiderSkillTreeActivity;

import dagger.Subcomponent;


/**
 * This component inject dependencies to all Activities across the application
 */

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(ForgotActivity forgotActivity);

    void inject(LoginActivity loginActivity);

    void inject(RegisterActivity registerActivity);

    void inject(BaseSkillTreeActivity baseSkillTreeActivity);

    void inject(RiderSkillTreeActivity riderSkillTreeActivity);

    void inject(HorseSkillTreeActivity horseSkillTreeActivity);

    void inject(CreateResourceActivity createResourceActivity);

}
