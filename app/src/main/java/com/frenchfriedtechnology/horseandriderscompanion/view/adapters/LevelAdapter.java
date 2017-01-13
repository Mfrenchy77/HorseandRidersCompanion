package com.frenchfriedtechnology.horseandriderscompanion.view.adapters;

import android.view.HapticFeedbackConstants;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frenchfriedtechnology.horseandriderscompanion.BusProvider;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.events.LevelEditEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.LevelSelectEvent;
import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

import static com.frenchfriedtechnology.horseandriderscompanion.events.LevelSelectEvent.EDIT_LEVEL;
import static com.frenchfriedtechnology.horseandriderscompanion.events.LevelSelectEvent.HORSE_ADJUST;
import static com.frenchfriedtechnology.horseandriderscompanion.events.LevelSelectEvent.RIDER_ADJUST;

/**
 * Adapter for displaying a list of SkillTree Levels and the profiles SkillLevel.
 */

class LevelAdapter extends BaseQuickAdapter<Level> {

    private List<Level> levels = new ArrayList<>();
    private List<Resource> resources = new ArrayList<>();
    private boolean rider;

    LevelAdapter(List<Level> levels, List<Resource> resources, boolean rider) {
        super(R.layout.item_level, levels);
        this.resources = resources;
        this.rider = rider;
        this.levels = levels;
    }

    private boolean isRider() {
        Timber.d("isRider(): " + rider);
        return rider;
    }

    void sortLevels() {
        Collections.sort(levels);
    }

    private void onItemMove(int fromPosition, int toPosition) {
        Timber.d("ItemMove() fromPosition: " + fromPosition + " to: " + toPosition);
        Collections.swap(levels, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        notifyItemChanged(toPosition);
    }

    @Override
    public long getItemId(int position) {
        if (!levels.isEmpty()) {
            return levels.get(position).getId();
        } else {
            return super.getItemId(position);
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, Level level) {
        int position = holder.getAdapterPosition();
        if (level.getPosition() == -1) {
            level.setPosition(position);
            Timber.d("setSkillPosition: " + position);
            BusProvider.getBusProviderInstance().post(new LevelEditEvent(level));
        }
        if (!resources.isEmpty()) {
            Timber.d("Resources size: " + resources.size());
        }
        //----set level values and click events
        holder.setText(R.id.level_title, level.getLevelName());
        View levelIndicator = holder.getView(R.id.level_indicator);
        if (level.getLevel() == Constants.NO_PROGRESS) {
            levelIndicator.setBackgroundResource(R.drawable.level_indicator_background_no_progress);
        } else if (level.getLevel() == Constants.LEARNING) {
            levelIndicator.setBackgroundResource(R.drawable.level_indicator_background_learning);
        } else if (level.getLevel() == Constants.COMPLETE) {
            levelIndicator.setBackgroundResource(R.drawable.level_indicator_background_complete);
        } else if (level.getLevel() == Constants.VERIFIED) {
            levelIndicator.setBackgroundResource(R.drawable.level_indicator_background_verified);
        }
        holder.itemView.setOnClickListener(
                view -> BusProvider.getBusProviderInstance().post(
                        new LevelSelectEvent(
                                isRider() ? RIDER_ADJUST :
                                        HORSE_ADJUST, level, level.getSkillId())));
        holder.itemView.setOnLongClickListener(view -> {
            if (new UserPrefs().isEditor() && new UserPrefs().isEditMode()) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                BusProvider.getBusProviderInstance().post(
                        new LevelSelectEvent(EDIT_LEVEL, level, level.getSkillId()));
                return true;
            } else {
                //do nothing
                Timber.d("Not in Editor/Edit Mode");
                return false;
            }
        });

        //---- setup level editor
        holder.getView(R.id.editor_level_layout).setVisibility(new UserPrefs().isEditor() && new UserPrefs().isEditMode() ? View.VISIBLE : View.GONE);
        holder.getView(R.id.level_up).setOnClickListener(view -> {
            if (position > 0) {
                level.setPosition(position - 1);
                onItemMove(position, (position - 1));
                BusProvider.getBusProviderInstance().post(new LevelEditEvent(level));
            }
        });
        holder.getView(R.id.level_down).setOnClickListener(view -> {
            if (position < levels.size() - 1) {
                onItemMove(position, position + 1);
                level.setPosition(position + 1);
                BusProvider.getBusProviderInstance().post(new LevelEditEvent(level));
            }
        });
    }
}
