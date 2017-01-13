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

    public BaseListItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    String id;

    String name;
    @Exclude
    int depth;
    @Exclude
    boolean selected = false;
    @Exclude
    boolean collapsed;
    @Exclude
    String parentId = null;

    @Exclude
    public String getParentId() {
        return parentId;
    }

    @Exclude
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

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

    @Exclude
    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    @Exclude
    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public boolean isSelected() {
        return selected;
    }

    @Exclude
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void toggle() {
        collapsed = !isCollapsed();
    }
}

