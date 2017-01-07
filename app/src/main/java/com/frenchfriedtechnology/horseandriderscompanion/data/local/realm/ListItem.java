package com.frenchfriedtechnology.horseandriderscompanion.data.local.realm;

import com.google.firebase.database.IgnoreExtraProperties;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Realm Object to simulate a List
 */
@IgnoreExtraProperties
public class ListItem extends RealmObject {

    public ListItem() {
    }

    public ListItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @PrimaryKey
    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
