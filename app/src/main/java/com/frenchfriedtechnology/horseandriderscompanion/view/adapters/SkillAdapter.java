package com.frenchfriedtechnology.horseandriderscompanion.view.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frenchfriedtechnology.horseandriderscompanion.BusProvider;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Skill;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.events.LevelSelectEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.SkillSelectEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.SkillUpdateEvent;
import com.frenchfriedtechnology.horseandriderscompanion.util.ViewUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

import static com.frenchfriedtechnology.horseandriderscompanion.events.LevelSelectEvent.NEW_LEVEL;


/**
 * Adapter for showing a List of SkillTree Skills
 */

public class SkillAdapter extends BaseQuickAdapter<Skill> {

    private Context context;
    private List<Skill> skills;
    private List<Level> allLevels;
    private List<Resource> resources;
    private boolean rider;

    public SkillAdapter(Context context, List<Skill> skills, List<Level> levels, List<Resource> resources, boolean rider) {
        super(R.layout.item_skill, skills);
        this.resources = resources;
        this.context = context;
        this.skills = skills;
        this.allLevels = levels;
        this.rider = rider;
    }

    public boolean isRider() {
        return rider;
    }


    public void sortSkills() {
        Collections.sort(skills);
    }


    private List<Level> getLevelsForSkill(Skill skill) {
        List<Level> levels = new ArrayList<>();
        for (int i = 0; i < allLevels.size(); i++) {
            if (allLevels.get(i).getSkillId().equals(skill.getId())) {
                levels.add(allLevels.get(i));
            }
        }
        return levels;
    }

    private List<Resource> getResourcesForLevel(String levelId) {
        List<Resource> levelResources = new ArrayList<>();
        for (int i = 0; i < resources.size(); i++) {
            if (resources.get(i).getLevelIds().contains(levelId)) {
                levelResources.add(resources.get(i));
            }
        }
        return levelResources;
    }

    private void onItemMove(int fromPosition, int toPosition) {
        Timber.d("ItemMove() fromPosition: " + fromPosition + " to: " + toPosition);
        Collections.swap(skills, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public long getItemId(int position) {
        if (!skills.isEmpty()) {
            String id = skills.get(position).getId();
            return ViewUtil.convertIdToNumber(id);
        } else {
            return super.getItemId(position);
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, Skill skill) {
        int position = holder.getAdapterPosition();

        //----set skill values and click events
        holder.setText(R.id.skill_title, skill.getSkillName());
        holder.itemView.setOnLongClickListener(view -> {
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            if (new UserPrefs().isEditor() && new UserPrefs().isEditMode()) {
                BusProvider.getBusProviderInstance().post(new SkillSelectEvent(skill.getCategoryId(), true, skill));
            }
            return true;
        });

        //----set up levels for skill
        List<Resource> levelResource = new ArrayList<>();
        List<Level> levels = getLevelsForSkill(skill);
        for (Level level : levels) {
            levelResource.addAll(getResourcesForLevel(level.getId()));
        }
        RecyclerView levelsRecycler = holder.getView(R.id.level_recycler);
        LevelAdapter levelAdapter = new LevelAdapter(levels, levelResource,isRider());
        levelAdapter.setHasStableIds(true);
        levelAdapter.sortLevels();
        levelsRecycler.setLayoutManager(new LinearLayoutManager(context));
        levelsRecycler.setAdapter(levelAdapter);

        //----setup edit layout
        holder.getView(R.id.skill_edit_layout).setVisibility(new UserPrefs().isEditor() && new UserPrefs().isEditMode() ? View.VISIBLE : View.GONE);
        holder.getView(R.id.skill_up).setOnClickListener(view -> {
            if (position > 0) {
                Timber.d("move position from " + position + " to: " + (position - 1));
                skill.setPosition(position - 1);
                onItemMove(position, (position - 1));
                BusProvider.getBusProviderInstance().post(new SkillUpdateEvent(skill));
            }
        });

        holder.getView(R.id.skill_down).setOnClickListener(view -> {
            if (position < skills.size() - 1) {
                Timber.d("List Size: " + skills.size());
                Timber.d("move position from " + position + " to: " + (position + 1));
                onItemMove(position, position + 1);
                skill.setPosition(position + 1);
                BusProvider.getBusProviderInstance().post(new SkillUpdateEvent(skill));
            }
        });
        holder.getView(R.id.add_level_button).setOnClickListener(view -> BusProvider.getBusProviderInstance().post(new LevelSelectEvent(NEW_LEVEL, null, skill.getId())));

        //----setup empty level
        TextView emptyLevel = holder.getView(R.id.empty_level);
        emptyLevel.setVisibility(levels.isEmpty() && new UserPrefs().isEditor() ? View.VISIBLE : View.GONE);
        levelsRecycler.setVisibility(levels.isEmpty() ? View.GONE : View.VISIBLE);
        emptyLevel.setOnClickListener(view -> BusProvider.getBusProviderInstance().post(new LevelSelectEvent(NEW_LEVEL, null, skill.getId())));


    }
}
