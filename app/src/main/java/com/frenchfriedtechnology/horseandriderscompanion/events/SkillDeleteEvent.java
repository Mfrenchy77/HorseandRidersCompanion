package com.frenchfriedtechnology.horseandriderscompanion.events;

public class SkillDeleteEvent {

    private String skillId;

    public SkillDeleteEvent(String skillId) {
        this.skillId = skillId;
    }

    public String getSkillId() {
        return skillId;
    }
}
