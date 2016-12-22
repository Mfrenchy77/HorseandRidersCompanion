package com.frenchfriedtechnology.horseandriderscompanion.view.riderSkillTree;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.MvpView;

/**
 * Created by matteo on 10/12/16 for HorseandRidersCompanion.
 */
interface RiderSkillTreeMvpView extends MvpView {

    void getRiderProfile(RiderProfile riderProfile);

    void updateAdapter();

}
