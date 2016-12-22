package com.frenchfriedtechnology.horseandriderscompanion.view.horseSkillTree;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.HorseProfile;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.MvpView;


interface HorseSkillTreeMvpView extends MvpView {

    void getHorseProfile(HorseProfile horseProfile);

    void updateAdapter();
}
