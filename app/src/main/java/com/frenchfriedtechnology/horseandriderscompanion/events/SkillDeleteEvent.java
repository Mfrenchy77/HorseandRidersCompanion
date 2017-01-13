package com.frenchfriedtechnology.horseandriderscompanion.events;

public class SkillDeleteEvent {

    private long skillId;

    public SkillDeleteEvent(long skillId) {
        this.skillId = skillId;
    }

    public long getSkillId() {
        return skillId;
    }
}
