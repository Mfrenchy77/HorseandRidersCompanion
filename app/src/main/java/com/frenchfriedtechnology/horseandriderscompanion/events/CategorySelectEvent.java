package com.frenchfriedtechnology.horseandriderscompanion.events;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Category;

public class CategorySelectEvent {
    private Category category;
    private boolean edit;

    public CategorySelectEvent(Category category, boolean edit) {
        this.category = category;
        this.edit = edit;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEdit() {
        return edit;
    }
}
