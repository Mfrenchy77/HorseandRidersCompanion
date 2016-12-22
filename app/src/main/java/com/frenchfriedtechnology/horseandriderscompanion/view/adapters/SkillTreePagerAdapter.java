package com.frenchfriedtechnology.horseandriderscompanion.view.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Category;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.HorseProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Skill;
import com.frenchfriedtechnology.horseandriderscompanion.view.riderSkillTree.CategoryFragment;
import com.frenchfriedtechnology.horseandriderscompanion.view.riderSkillTree.ProfileFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static com.frenchfriedtechnology.horseandriderscompanion.view.riderSkillTree.ProfileFragment.HORSE_PROFILE;
import static com.frenchfriedtechnology.horseandriderscompanion.view.riderSkillTree.ProfileFragment.RIDER_PROFILE;

/**
 * Created by matteo on 16/12/16 for HorseandRidersCompanion.
 */

public class SkillTreePagerAdapter extends FragmentStatePagerAdapter {
    List<Category> categories = new ArrayList<>();

    private List<Skill> allSkills = new ArrayList<>();
    private List<Level> allLevels = new ArrayList<>();
    HorseProfile horseProfile = new HorseProfile();
    RiderProfile riderProfile = new RiderProfile();
    boolean rider;


    private final FragmentManager mFragmentManager;
    private SparseArray<Fragment> mFragments;
    private FragmentTransaction mCurTransaction;

    public SkillTreePagerAdapter(FragmentManager fm,
                                 List<Level> allLevels,
                                 List<Skill> allSkills,
                                 List<Category> categories,
                                 HorseProfile horseProfile,
                                 RiderProfile riderProfile,
                                 boolean rider) {
        super(fm);
        this.allLevels = allLevels;
        this.allSkills = allSkills;
        this.categories = categories;
        this.horseProfile = horseProfile;
        this.rider = rider;
        this.riderProfile = riderProfile;
        mFragmentManager = fm;
        mFragments=new SparseArray<>();
    }


    public void setAllLevels(List<Level> allLevels) {
        this.allLevels = allLevels;
        notifyDataSetChanged();
    }

    public void setAllSkills(List<Skill> allSkills) {
        Timber.d("setAllSkills()  called");
        this.allSkills = allSkills;
        notifyDataSetChanged();
    }

    public List<Skill> getAllSkills() {
        Timber.d("getAllSkills() size: " + allSkills.size());
        return allSkills;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        sortCategories();
        notifyDataSetChanged();
    }

    private List<Skill> getSkillsForCategory(Category category) {
        Timber.d("Levels size: " + allLevels.size());
        List<Skill> categorySkills = new ArrayList<>();
        List<Skill> skills = getAllSkills();
        Timber.d("Skills size: " + skills.size());
        for (int i = 0; i < skills.size(); i++) {
            if (skills.get(i).getCategoryId().equals(category.getId())) {
                Timber.d("added skill to category: " + category.getName());
                categorySkills.add(skills.get(i));
            }
        }
        return categorySkills;
    }

    private void sortCategories() {
        Collections.sort(categories);
    }

    public int getItemPosition(Object object){
        return POSITION_NONE;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = getItem(position);
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        mCurTransaction.add(container.getId(),fragment,"fragment:"+position);
        mFragments.put(position,fragment);

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
        Timber.d("position= " + position);
        if (position == 0) {
            Timber.d("Rider? " + rider);
            return ProfileFragment.newInstance(rider ?
                    RIDER_PROFILE : HORSE_PROFILE, horseProfile, riderProfile);
        } else {
            Category category = categories.get(position - 1);
            Timber.d("Category: " + category.getName());
            return CategoryFragment.newInstance(category,
                    getSkillsForCategory(category),
                    allLevels,
                    rider);
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
