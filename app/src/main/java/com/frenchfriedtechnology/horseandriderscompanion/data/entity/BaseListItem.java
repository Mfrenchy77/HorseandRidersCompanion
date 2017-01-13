package com.frenchfriedtechnology.horseandriderscompanion.data.entity;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

/**
 * Base Item
 */
@Parcel
@IgnoreExtraProperties
public class BaseListItem {

    public BaseListItem() {
        //required
    }

    public BaseListItem(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public BaseListItem(String stringId, String name) {
        this.stringId = stringId;
        this.name = name;
    }

    long id;

    String stringId;

    String name;

    String message = null;
    @Exclude
    int depth;
    @Exclude
    boolean selected = false;
    @Exclude
    boolean collapsed;
    @Exclude
    long parentId = 0;

    public String getStringId() {
        return stringId;
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Exclude
    public long getParentId() {
        return parentId;
    }

    @Exclude
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public boolean isCollapsed() {
        return collapsed;
    }

    @Exclude
    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    @Exclude
    public int getDepth() {
        return depth;
    }

    @Exclude
    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Exclude
    public boolean isSelected() {
        return selected;
    }

    @Exclude
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Exclude
    public void toggle() {
        if (collapsed) {
            setCollapsed(false);
        } else {
            setCollapsed(true);
        }
    }
}

