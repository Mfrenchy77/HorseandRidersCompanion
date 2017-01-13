package com.frenchfriedtechnology.horseandriderscompanion.events;

public class ResourceSelectedEvent {
    private String url;

    public ResourceSelectedEvent(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
