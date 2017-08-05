package com.frenchfriedtechnology.horseandriderscompanion.view.adapters;

import android.content.Context;
import android.view.View;
import android.widget.CheckedTextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.BaseListItem;
import com.frenchfriedtechnology.horseandriderscompanion.util.ViewUtil;

import java.util.Collections;
import java.util.List;

/**
 * Adapter for multiple select of skill tree items
 */

public class SkillTreeSelectAdapter extends BaseQuickAdapter<BaseListItem> {
    private static final int UNIT_PADDING = (int) ViewUtil.dpToPx(5);

    private List<BaseListItem> skillTree = Collections.emptyList();
    private Context context;

    public SkillTreeSelectAdapter(Context context, List<BaseListItem> skillTree) {
        super(R.layout.item_select_skilltree, skillTree);
        this.context = context;
        this.skillTree = skillTree;
    }

    @Override
    public long getItemId(int position) {
        if (!skillTree.isEmpty()) {
            return skillTree.get(position).getId();
        } else {
            return super.getItemId(position);
        }
    }

    @Override
    public int getItemCount() {
        return skillTree.size();
    }

    public List<BaseListItem> getSkillTree() {
        return skillTree;
    }

    public void setSkillTree(List<BaseListItem> skillTree) {
        this.skillTree = skillTree;
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseListItem baseListItem) {
// TODO: 04/08/17 Checked Item Visibility system is not working well, please fix
        //----initialize check item
        holder.itemView.setVisibility(baseListItem.isCollapsed() ? View.GONE : View.VISIBLE);
        holder.setText(R.id.select_skill_tree_item, baseListItem.getName());
        CheckedTextView checkedTextView = holder.getView(R.id.select_skill_tree_item);
        checkedTextView.setText(baseListItem.getName());
        int depth = baseListItem.getDepth() * 5;
        checkedTextView.setPadding(UNIT_PADDING * depth, 0, 0, 0);
        checkedTextView.setChecked(baseListItem.isSelected());
        checkedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedTextView.isChecked()) {
                    baseListItem.setSelected(false);
                    toggleChildren(baseListItem.getId());
                    notifyItemChanged(holder.getAdapterPosition());
                } else {
                    baseListItem.setSelected(true);
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }
        });

    }

    private void toggleChildren(long id) {
        for (int i = 0; i < skillTree.size(); i++) {
            if (skillTree.get(i).getParentId() != 0 && skillTree.get(i).getParentId() == id) {
                skillTree.get(i).toggle();
                notifyDataSetChanged();
            }
        }
    }
}
