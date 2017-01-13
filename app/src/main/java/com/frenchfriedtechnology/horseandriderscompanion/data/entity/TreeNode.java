package com.frenchfriedtechnology.horseandriderscompanion.data.entity;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Represents a node in a tree structure, which has references to its parent and children (if any).
 * This is used to model a collapsing tree in the UI. It is possible to removeChild/add nodes
 * onto the tree.
 */

public class TreeNode implements RecyclerModel, Serializable {
    private final List<TreeNode> children;

    private List<TreeNode> sortedList;
    private TreeNode parent;

    private int depth;
    private boolean expanded;
    private boolean optionsExpanded = false;

    private BaseListItem data;

    public TreeNode() {
        this.children = new ArrayList<>();
        expanded = true;
        sortedList = toList();
    }

    /**
     * Adds a child to the last index of this parent node.
     *
     * @param child the child being added to this node
     */
    public void addChild(TreeNode child) {
        if (child != null && !children.contains(child)) {
            child.setParent(this);
            children.add(child);
        }
    }

    /**
     * Adds a child to the first index of this parent node.
     *
     * @param child the child being added to this node
     */
    public void addChildAsFirstElement(TreeNode child) {
        if (child != null && !children.contains(child)) {
            child.setParent(this);
            children.add(0, child);
        }
    }

    /**
     * Removes a child from the parent node.
     *
     * @param child the child being removed from this node
     */
    public void removeChild(TreeNode child) {
        if (child != null) {
            children.remove(child);
            child.setParent(null);
        }
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public BaseListItem getData() {
        return data;
    }

    public void setData(BaseListItem data) {
        this.data = data;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    /**
     * Recurse to give an ordered comment tree list. Node children must be in an expanded state
     * to be added to this list.
     *
     * @return an ordered comment tree list
     */
    public List<TreeNode> toList() {
        List<TreeNode> orderedList = new ArrayList<>();

        for (TreeNode child : children) {
            if (expanded) {
                orderedList.add(child);
                orderedList.addAll(child.toList());
            }
        }
        return orderedList;
    }

    public int size() {
        return sortedList.size();
    }

    public void notifyDataChanged() {
        sortedList = toList();
    }

    public TreeNode getNodeAtIndex(int index) {
        return sortedList.get(index);
    }

    /**
     * Finds the comment by its id, if any exists
     *
     * @param id the comment id
     * @return the matching comment, or null
     */
    public TreeNode getNodeById(String id) {
        Timber.d("TreeNode, getNodeId; " +id);
        for (TreeNode node : sortedList) {
            if (id.equals(node.getData().getId())) {
                return node;
            }
        }
        return null;
    }

    /**
     * Finds the index of the child node
     *
     * @param child the given child
     * @return the index of the child in this list, or -1
     */
    public int getNodeIndex(TreeNode child) {
        for (int i = 0; i < sortedList.size(); i++) {
            TreeNode node = sortedList.get(i);

            if (node.equals(child)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isOptionsExpanded() {
        return optionsExpanded;
    }

    public void setOptionsExpanded(boolean optionsExpanded) {
        this.optionsExpanded = optionsExpanded;
    }
}
