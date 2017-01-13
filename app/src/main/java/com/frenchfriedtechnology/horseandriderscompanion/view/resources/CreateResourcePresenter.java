package com.frenchfriedtechnology.horseandriderscompanion.view.resources;

import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.ResourcesApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.BaseListItem;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Category;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Skill;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.realmServices.RealmSkillTreeService;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Submit new Resource
 */

public class CreateResourcePresenter extends BasePresenter<CreateResourceMvpView> {

    private final RealmSkillTreeService realmSkillTreeService;
    private List<Category> riderCategories = new ArrayList<>();
    private List<Skill> riderSkills = new ArrayList<>();
    private List<Level> riderLevels = new ArrayList<>();
    private List<Category> horseCategories = new ArrayList<>();
    private List<Skill> horseSkills = new ArrayList<>();
    private List<Level> horseLevels = new ArrayList<>();


    @Inject
    public CreateResourcePresenter(RealmSkillTreeService realmSkillTreeService) {
        this.realmSkillTreeService = realmSkillTreeService;
    }

    void getSkillTreeItems() {
        riderCategories = realmSkillTreeService.getCategories(true);
        riderSkills = realmSkillTreeService.getSkills(true);
        riderLevels = realmSkillTreeService.getLevels(true);

        horseCategories = realmSkillTreeService.getCategories(false);
        horseSkills = realmSkillTreeService.getSkills(false);
        horseLevels = realmSkillTreeService.getLevels(false);
        if (riderCategories.isEmpty()) {
            Timber.d("riderCategories are Null");
        } else {

            Timber.d("RiderCategories size: " + riderCategories.size());
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getMvpView().getSkillTreeList(matchSkillTreeItem());
            }
        };
        runnable.run();
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
                if (riderSkills.get(i).getCategoryId().equals(category.getId())) {
                    position++;
                    BaseListItem skillItem = new BaseListItem(riderSkills.get(i).getId(), riderSkills.get(i).getSkillName());
                    skillItem.setParentId(category.getId());
                    skillItem.setCollapsed(true);
                    skillItem.setDepth(1);
                    baseListItems.add(position, skillItem);
                    for (int j = 0; j < riderLevels.size(); j++) {
                        if (riderLevels.get(j).getSkillId().equals(riderSkills.get(i).getId())) {
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
            baseListItems.add(position, categoryItem);
            for (int i = 0; i < horseSkills.size(); i++) {
                if (horseSkills.get(i).getCategoryId().equals(category.getId())) {
                    position++;
                    BaseListItem skillItem = new BaseListItem(horseSkills.get(i).getId(), horseSkills.get(i).getSkillName());
                    baseListItems.add(position, skillItem);
                    for (int j = 0; j < horseLevels.size(); j++) {
                        if (horseLevels.get(j).getSkillId().equals(horseSkills.get(i).getId())) {
                            position++;
                            BaseListItem levelItem = new BaseListItem(horseLevels.get(j).getId(), horseLevels.get(j).getLevelName());
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

    void submitNewResource(Resource resource) {
        getMvpView().setPosting(true);
        new ResourcesApi().createOrUpdateResource(resource, new ResourcesApi.ResourceCreatedCallback() {
            @Override
            public void onSuccess() {
                getMvpView().setPosting(false);
            }

            @Override
            public void onError(String error) {
                getMvpView().setPosting(false);
                Timber.e(error);
            }
        });
    }
}
