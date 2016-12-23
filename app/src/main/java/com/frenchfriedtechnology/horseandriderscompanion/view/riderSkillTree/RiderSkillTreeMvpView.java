package com.frenchfriedtechnology.horseandriderscompanion.view.riderSkillTree;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.MvpView;

import java.util.List;

/**
 * Created by matteo on 10/12/16 for HorseandRidersCompanion.
 */
interface RiderSkillTreeMvpView extends MvpView {

    void getRiderProfile(RiderProfile riderProfile);

    void updateAdapter();

    void getResources(List<Resource> resources);
}
