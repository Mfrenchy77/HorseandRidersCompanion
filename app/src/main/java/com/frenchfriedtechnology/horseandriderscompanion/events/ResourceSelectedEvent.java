package com.frenchfriedtechnology.horseandriderscompanion.events;

/**
 * Created by matteo on 06/01/17 for HorseandRidersCompanion.
 */
public class ResourceSelectedEvent {
    private String url;

    public ResourceSelectedEvent(String url) {
        this.url = url;
    }
}
