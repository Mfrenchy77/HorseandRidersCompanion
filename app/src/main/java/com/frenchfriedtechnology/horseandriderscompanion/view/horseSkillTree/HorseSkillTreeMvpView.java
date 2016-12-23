package com.frenchfriedtechnology.horseandriderscompanion.view.horseSkillTree;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.HorseProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.MvpView;

import java.util.List;


interface HorseSkillTreeMvpView extends MvpView {

    void getHorseProfile(HorseProfile horseProfile);

    void updateAdapter();

    void getResources(List<Resource> resources);
}
