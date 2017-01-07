package com.frenchfriedtechnology.horseandriderscompanion.view.riderSkillTree;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.frenchfriedtechnology.horseandriderscompanion.BusProvider;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Category;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Skill;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.events.SkillSelectEvent;
import com.frenchfriedtechnology.horseandriderscompanion.view.adapters.SkillAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


/**
 * Reusable Fragment for view Pager representing a Category
 */

public class CategoryFragment extends Fragment {

    private static final String CATEGORY = "Category";
    private static final String SKILLS = "Skills";
    private static final String LEVELS = "Levels";
    private static final String RESOURCES = "Resources";
    private static final String RIDER = "Rider";

    public CategoryFragment() {
    }

    public static CategoryFragment newInstance(Category category, List<Skill> skills, List<Level> levels, List<Resource> resources, boolean rider) {

        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        Parcelable skillsParcelable = Parcels.wrap(skills);
        Parcelable levelsParcelable = Parcels.wrap(levels);
        Parcelable resourcesParcelable = Parcels.wrap(resources);
        args.putParcelable(SKILLS, skillsParcelable);
        args.putParcelable(LEVELS, levelsParcelable);
        args.putParcelable(CATEGORY, category);
        args.putParcelable(RESOURCES, resourcesParcelable);
        args.putBoolean(RIDER, rider);
        fragment.setArguments(args);
        fragment.setRetainInstance(true);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        initCategory(rootView);

        return rootView;
    }

    private List<Resource> getResourcesForSkill(String skillId, List<Resource> resources) {
        List<Resource> skillResources = new ArrayList<>();

        for (int i = 0; i < resources.size(); i++) {
            if (resources.get(i).getSkillIds().contains(skillId)) {
                skillResources.add(resources.get(i));
            }
        }

        return skillResources;
    }

    private void initCategory(View rootView) {
        final Category category = getArguments().getParcelable(CATEGORY);
        //set Resources

        //editor actions
        ImageButton addSkillButton = (ImageButton) rootView.findViewById(R.id.add_skill_button);
        addSkillButton.setVisibility(new UserPrefs().isEditor() && new UserPrefs().isEditMode() ? View.VISIBLE : View.GONE);
        addSkillButton.setOnClickListener(view -> BusProvider.getBusProviderInstance().post(new SkillSelectEvent(category.getId(), false
                , null)));

        //setup SkillAdapter
        List<Skill> skills = Parcels.unwrap(getArguments().getParcelable(SKILLS));
        List<Resource> allResources = Parcels.unwrap(getArguments().getParcelable(RESOURCES));
        List<Resource> skillResources = new ArrayList<>();
        for (Skill skill : skills) {
            skillResources.addAll(getResourcesForSkill(skill.getId(), allResources));
        }

        List<Level> levels = Parcels.unwrap(getArguments().getParcelable(LEVELS));
        boolean rider = getArguments().getBoolean(RIDER);

        SkillAdapter skillAdapter = new SkillAdapter(getActivity(), skills, levels, skillResources, rider);
        skillAdapter.sortSkills();
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.skill_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(skillAdapter);

        //setup empty skill
        TextView emptySkill = (TextView) rootView.findViewById(R.id.empty_skill);
        if (skills.size() == 0) {
            emptySkill.setVisibility(View.VISIBLE);
            emptySkill.setOnClickListener(view -> BusProvider.getBusProviderInstance().post(new SkillSelectEvent(category.getId(), false, null)));
        } else {
            emptySkill.setVisibility(View.GONE);
        }
    }
}
