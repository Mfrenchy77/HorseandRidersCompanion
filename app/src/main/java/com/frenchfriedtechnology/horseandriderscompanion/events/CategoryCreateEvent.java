package com.frenchfriedtechnology.horseandriderscompanion.events;

public class CategoryCreateEvent {

    private String categoryName;
    private String categoryDescription;

    public CategoryCreateEvent(String categoryDescription, String categoryName) {
        this.categoryDescription = categoryDescription;
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
