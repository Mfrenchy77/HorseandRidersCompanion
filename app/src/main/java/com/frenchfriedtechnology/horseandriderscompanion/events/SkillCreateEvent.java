package com.frenchfriedtechnology.horseandriderscompanion.events;

public class SkillCreateEvent {

    private String skillName;
    private String skillDescription;
    private long categoryId;
    private boolean edit;
    private long skillId;

    public SkillCreateEvent(long categoryId, String skillDescription, String skillName, boolean edit, long skillId) {
        this.categoryId = categoryId;
        this.edit = edit;
        this.skillId = skillId;
        this.skillDescription = skillDescription;
        this.skillName = skillName;
    }

    public boolean isEdit() {
        return edit;
    }

    public long getSkillId() {
        return skillId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public String getSkillDescription() {
        return skillDescription;
    }

    public String getSkillName() {
        return skillName;
    }
}
