package com.frenchfriedtechnology.horseandriderscompanion.events;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Skill;

public class SkillSelectEvent {
    private Skill skill;
    private boolean edit;
    private long categoryId;

    public SkillSelectEvent(long categoryId, boolean edit, Skill skill) {
        this.categoryId = categoryId;
        this.edit = edit;
        this.skill = skill;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public boolean isEdit() {
        return edit;
    }

    public Skill getSkill() {
        return skill;
    }
}