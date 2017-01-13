package com.frenchfriedtechnology.horseandriderscompanion.view.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Category;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.HorseProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Skill;
import com.frenchfriedtechnology.horseandriderscompanion.view.riderSkillTree.CategoryFragment;
import com.frenchfriedtechnology.horseandriderscompanion.view.riderSkillTree.ProfileFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.frenchfriedtechnology.horseandriderscompanion.view.riderSkillTree.ProfileFragment.HORSE_PROFILE;
import static com.frenchfriedtechnology.horseandriderscompanion.view.riderSkillTree.ProfileFragment.RIDER_PROFILE;

/**
 * View pager adapter that shows a profile and Horse/Rider Skill Tree
 */

public class SkillTreePagerAdapter extends FragmentStatePagerAdapter {

    private List<Category> categories = new ArrayList<>();
    private List<Skill> allSkills = new ArrayList<>();
    private List<Level> allLevels = new ArrayList<>();
    private List<Resource> resources = new ArrayList<>();
    private HorseProfile horseProfile = new HorseProfile();
    private RiderProfile riderProfile = new RiderProfile();
    private boolean rider;


    private final FragmentManager mFragmentManager;
    private SparseArray<Fragment> mFragments;
    private FragmentTransaction mCurTransaction;

    public SkillTreePagerAdapter(FragmentManager fm,
                                 List<Level> allLevels,
                                 List<Skill> allSkills,
                                 List<Category> categories,
                                 List<Resource> resources,
                                 HorseProfile horseProfile,
                                 RiderProfile riderProfile,
                                 boolean rider) {
        super(fm);
        this.resources = resources;
        this.allLevels = allLevels;
        this.allSkills = allSkills;
        this.categories = categories;
        this.horseProfile = horseProfile;
        this.rider = rider;
        this.riderProfile = riderProfile;
        mFragmentManager = fm;
        mFragments = new SparseArray<>();
    }

    public HorseProfile getHorseProfile() {
        return horseProfile;
    }

    public void setHorseProfile(HorseProfile horseProfile) {
        this.horseProfile = horseProfile;
    }

    public RiderProfile getRiderProfile() {
        return riderProfile;
    }

    public void setRiderProfile(RiderProfile riderProfile) {
        this.riderProfile = riderProfile;
    }

    public void setAllLevels(List<Level> allLevels) {
        this.allLevels = allLevels;
        notifyDataSetChanged();
    }

    public void setAllSkills(List<Skill> allSkills) {
        this.allSkills = allSkills;
        notifyDataSetChanged();
    }

    private List<Skill> getAllSkills() {
        return allSkills;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        sortCategories();
        notifyDataSetChanged();
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
        notifyDataSetChanged();
    }

    public List<Resource> getResources() {
        return resources;
    }

    private List<Skill> getSkillsForCategory(Category category) {
        List<Skill> categorySkills = new ArrayList<>();
        List<Skill> skills = getAllSkills();
        for (int i = 0; i < skills.size(); i++) {
            if (skills.get(i).getCategoryId().equals(category.getId())) {
                categorySkills.add(skills.get(i));
            }
        }
        return categorySkills;
    }

    private void sortCategories() {
        Collections.sort(categories);
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = getItem(position);
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        mCurTransaction.add(container.getId(), fragment, "fragment:" + position);
        mFragments.put(position, fragment);

        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        mCurTransaction.detach(mFragments.get(position));
        mFragments.remove(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object fragment) {
        return ((Fragment) fragment).getView() == view;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ProfileFragment.newInstance(rider ?
                    RIDER_PROFILE : HORSE_PROFILE, getHorseProfile(), getRiderProfile());
        } else {
            Category category = categories.get(position - 1);
            return CategoryFragment.newInstance(category,
                    getSkillsForCategory(category),
                    allLevels, resources, rider);
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }

    @Override
    public int getCount() {
        return categories.size() + 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Profile";
        } else {
            return categories.get(position - 1).getName();
        }
    }

}
