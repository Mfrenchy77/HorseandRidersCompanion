package com.frenchfriedtechnology.horseandriderscompanion.events;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Category;

public class CategoryEditEvent {
    private Category category;

    public CategoryEditEvent(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }
}
