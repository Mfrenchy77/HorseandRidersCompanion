package com.frenchfriedtechnology.horseandriderscompanion.view.resources;

import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.CategoriesApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.LevelsApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.ResourcesApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.SkillsApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.BaseListItem;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Category;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Skill;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.TreeNode;
import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Submit new Resource
 */

public class CreateResourcePresenter extends BasePresenter<CreateResourceMvpView> {

    private List<Category> riderCategories = new ArrayList<>();
    private List<Skill> riderSkills = new ArrayList<>();
    private List<Level> riderLevels = new ArrayList<>();
    private List<Category> horseCategories = new ArrayList<>();
    private List<Skill> horseSkills = new ArrayList<>();
    private List<Level> horseLevels = new ArrayList<>();

    private TreeNode skillTree;

    @Inject
    public CreateResourcePresenter() {
    }

    void getSkillTreeItems() {
        new CategoriesApi().getCategories(Constants.RIDER_SKILL_TREE, new CategoriesApi.CategoryCallback() {
            @Override
            public void onSuccess(List<Category> categories) {
                riderCategories = categories;
                if (riderCategories.isEmpty()) {
                    Timber.d("riderCategories are Null");
                } else {
                    getMvpView().getSkillTreeList(matchSkillTreeItem());

                    Timber.d("RiderCategories size: " + riderCategories.size());
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
        new CategoriesApi().getCategories(Constants.HORSE_SKILL_TREE, new CategoriesApi.CategoryCallback() {
            @Override
            public void onSuccess(List<Category> categories) {
                horseCategories = categories;
                getMvpView().getSkillTreeList(matchSkillTreeItem());
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

        new SkillsApi().getSkills(Constants.RIDER_SKILL_TREE, new SkillsApi.SkillCallback() {
            @Override
            public void onSuccess(List<Skill> skills) {
                riderSkills = skills;
                getMvpView().getSkillTreeList(matchSkillTreeItem());
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
        new SkillsApi().getSkills(Constants.HORSE_SKILL_TREE, new SkillsApi.SkillCallback() {
            @Override
            public void onSuccess(List<Skill> skills) {
                horseSkills = skills;
                getMvpView().getSkillTreeList(matchSkillTreeItem());
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

        new LevelsApi().getLevels(Constants.RIDER_SKILL_TREE, new LevelsApi.LevelCallback() {
            @Override
            public void onSuccess(List<Level> levels) {
                riderLevels = levels;
                getMvpView().getSkillTreeList(matchSkillTreeItem());
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
        new LevelsApi().getLevels(Constants.HORSE_SKILL_TREE, new LevelsApi.LevelCallback() {
            @Override
            public void onSuccess(List<Level> levels) {
                horseLevels = levels;
                getMvpView().getSkillTreeList(matchSkillTreeItem());
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

        List<BaseListItem> sortedList = matchSkillTreeItem();
        skillTree = generateSkillTree(sortedList);
        getMvpView().getTreeNode(skillTree);

    }

    private List<BaseListItem> matchSkillTreeItem() {
        List<BaseListItem> baseListItems = new ArrayList<>();
        int position = 0;
        for (Category category : riderCategories) {
            BaseListItem categoryItem = new BaseListItem(category.getId(), category.getName());
            categoryItem.setDepth(0);
            categoryItem.setCollapsed(false);
            baseListItems.add(position, categoryItem);
            for (int i = 0; i < riderSkills.size(); i++) {
                if (riderSkills.get(i).getCategoryId() == category.getId()) {
                    position++;
                    BaseListItem skillItem = new BaseListItem(riderSkills.get(i).getId(), riderSkills.get(i).getSkillName());
                    skillItem.setParentId(category.getId());
                    skillItem.setCollapsed(true);
                    skillItem.setDepth(1);
                    baseListItems.add(position, skillItem);
                    for (int j = 0; j < riderLevels.size(); j++) {
                        if (riderLevels.get(j).getSkillId() == riderSkills.get(i).getId()) {
                            position++;
                            BaseListItem levelItem = new BaseListItem(riderLevels.get(j).getId(), riderLevels.get(j).getLevelName());
                            levelItem.setParentId(riderLevels.get(j).getSkillId());
                            levelItem.setCollapsed(true);
                            levelItem.setDepth(2);
                            baseListItems.add(position, levelItem);
                        }
                    }
                }
            }
            position++;
        }
        for (Category category : horseCategories) {
            BaseListItem categoryItem = new BaseListItem(category.getId(), category.getName());
            categoryItem.setDepth(0);
            categoryItem.setCollapsed(false);
            baseListItems.add(position, categoryItem);
            for (int i = 0; i < horseSkills.size(); i++) {
                if (horseSkills.get(i).getCategoryId() == category.getId()) {
                    position++;
                    BaseListItem skillItem = new BaseListItem(horseSkills.get(i).getId(), horseSkills.get(i).getSkillName());
                    skillItem.setParentId(category.getId());
                    skillItem.setCollapsed(true);
                    skillItem.setDepth(1);
                    baseListItems.add(position, skillItem);
                    for (int j = 0; j < horseLevels.size(); j++) {
                        if (horseLevels.get(j).getSkillId() == horseSkills.get(i).getId()) {
                            position++;
                            BaseListItem levelItem = new BaseListItem(horseLevels.get(j).getId(), horseLevels.get(j).getLevelName());
                            levelItem.setParentId(riderLevels.get(j).getSkillId());
                            levelItem.setCollapsed(true);
                            levelItem.setDepth(2);
                            baseListItems.add(position, levelItem);
                        }
                    }
                }
            }
            position++;
        }
        Timber.d("BaseListItems size: " + baseListItems.size());
        return baseListItems;
    }

    private TreeNode generateSkillTree(List<BaseListItem> totalSkillTree) {
        int depth = -1;
        TreeNode parent = new TreeNode();
        parent.setDepth(depth);
        traverseLevels(parent, depth, totalSkillTree);
        return parent;
    }

    private void traverseLevels(TreeNode parent, int currentDepth, List<BaseListItem> totalSkillTree) {
        currentDepth++;
        List<BaseListItem> leveledList = getNextLevel(totalSkillTree, currentDepth);

        for (BaseListItem item : leveledList) {
            boolean isChild = parent.isRoot() || parent.getData().getId() == item.getParentId();

            if (isChild) {
                TreeNode child = new TreeNode();
                child.setData(item);
                parent.addChild(child);
            }
        }
        for (TreeNode treeNode : parent.getChildren()) {
            traverseLevels(treeNode, currentDepth, totalSkillTree);
        }
    }

    private List<BaseListItem> getNextLevel(List<BaseListItem> totalSkillTree, int currentDepth) {
        List<BaseListItem> currentLevelList = new ArrayList<>();

        for (BaseListItem item : totalSkillTree) {
            if (item.getDepth() == currentDepth) {
                currentLevelList.add(item);
            }
        }
        return currentLevelList;
    }

    void submitNewResource(Resource resource) {
        getMvpView().setPosting(true);
        new ResourcesApi().createOrUpdateResource(resource);
    }
}
