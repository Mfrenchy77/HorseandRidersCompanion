package com.frenchfriedtechnology.horseandriderscompanion.view.resources;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.BaseListItem;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Category;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Skill;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.TreeNode;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.MvpView;

import java.util.List;

/**
 * Created by matteo on 25/12/16 for HorseandRidersCompanion.
 */

public interface CreateResourceMvpView extends MvpView {

    void setPosting(boolean posting);

    void getTreeNode(TreeNode treeNode);

    void getSkillTreeList(List<BaseListItem> skillTreeList);
}
