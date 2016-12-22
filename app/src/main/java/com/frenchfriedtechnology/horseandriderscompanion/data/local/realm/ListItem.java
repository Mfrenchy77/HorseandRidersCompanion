package com.frenchfriedtechnology.horseandriderscompanion.data.local.realm;

import io.realm.RealmObject;

/**
 * Realm Object to simulate a List
 */

public class ListItem extends RealmObject {
    public ListItem() {
    }

    private String id;

    public ListItem(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
