package com.frenchfriedtechnology.horseandriderscompanion.view.main;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.BaseListItem;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.HorseProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.MvpView;

import java.util.List;

interface MainMvpView extends MvpView {

    void showProgress();

    void hideProgress();

    void getUserProfile(RiderProfile riderProfile);

    void getHorseProfiles(List<BaseListItem> horseProfiles);
}
