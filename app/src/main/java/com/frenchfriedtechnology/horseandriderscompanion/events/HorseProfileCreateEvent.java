package com.frenchfriedtechnology.horseandriderscompanion.events;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.HorseProfile;

public class HorseProfileCreateEvent {
    private HorseProfile horseProfile;

    public HorseProfileCreateEvent(HorseProfile horseProfile) {
        this.horseProfile = horseProfile;
    }

    public HorseProfile getHorseProfile() {
        return horseProfile;
    }
}
