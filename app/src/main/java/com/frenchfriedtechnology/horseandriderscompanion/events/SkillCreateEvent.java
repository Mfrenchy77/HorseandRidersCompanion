package com.frenchfriedtechnology.horseandriderscompanion.events;

public class SkillCreateEvent {

    private String skillName;
    private String skillDescription;
    private String categoryId;
    private boolean edit;
    private String skillId;

    public SkillCreateEvent(String categoryId, String skillDescription, String skillName, boolean edit, String skillId) {
        this.categoryId = categoryId;
        this.edit = edit;
        this.skillId = skillId;
        this.skillDescription = skillDescription;
        this.skillName = skillName;
    }

    public boolean isEdit() {
        return edit;
    }

    public String getSkillId() {
        return skillId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getSkillDescription() {
        return skillDescription;
    }

    public String getSkillName() {
        return skillName;
    }
}
