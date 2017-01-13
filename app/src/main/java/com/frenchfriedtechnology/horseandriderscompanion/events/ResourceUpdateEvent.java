package com.frenchfriedtechnology.horseandriderscompanion.events;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;

public class ResourceUpdateEvent {
    private Resource resource;

    public ResourceUpdateEvent(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }
}
