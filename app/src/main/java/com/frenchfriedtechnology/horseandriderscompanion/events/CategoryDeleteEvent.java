package com.frenchfriedtechnology.horseandriderscompanion.events;

public class CategoryDeleteEvent {
    private String categoryName;

    public CategoryDeleteEvent(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
