package com.frenchfriedtechnology.horseandriderscompanion.view.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frenchfriedtechnology.horseandriderscompanion.AccountManager;
import com.frenchfriedtechnology.horseandriderscompanion.BusProvider;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.BaseListItem;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.events.ResourceSelectedEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.ResourceUpdateEvent;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


/**
 * Adapter for showing a list of Resources
 */

public class ResourceAdapter extends BaseQuickAdapter<Resource> {

    public ResourceAdapter(List<Resource> data) {
        super(R.layout.item_resource, data);
    }

    private boolean alreadyRecommended(Resource resource, long id) {
        for (int i = 0; i < resource.getUsersWhoRated().size(); i++) {
            if (resource.getUsersWhoRated().get(i).getId() == id) {
                return true;
            }
        }
        return false;
    }

    private int positionInList(List<BaseListItem> list, BaseListItem item) {
        Timber.d("List size: " + list.size());
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == item.getId()) {
                Timber.d("Found position: " + i);
                return i;
            }
        }
        Timber.d("position not Found");
        return -1;
    }

    @Override
    protected void convert(BaseViewHolder holder, Resource resource) {
        int position = holder.getAdapterPosition();
        BaseListItem userListItem = new BaseListItem(new UserPrefs().getUserId(), AccountManager.currentUser());


        //----setup resource values and click events
        holder.setText(R.id.resource_title, resource.getName());
        TextView ratingCount = holder.getView(R.id.resource_rating_count);
        ratingCount.setText(String.valueOf(resource.getRating()));
        holder.setText(R.id.resource_description, resource.getDescription());
        ImageView recommendButton = holder.getView(R.id.resource_recommend_button);
        recommendButton.setPressed(alreadyRecommended(resource, userListItem.getId()));
        if (resource.getUsersWhoRated() != null) {
            if (!alreadyRecommended(resource, userListItem.getId())) {

                holder.setText(R.id.resource_recommend_text, "Recommend this?");
            } else {
                holder.setText(R.id.resource_recommend_text, "Remove Recommendation");
                recommendButton.setRotation(180);
            }
        }

        LinearLayout recommendLayout = holder.getView(R.id.resource_recommend_layout);
        recommendLayout.setOnClickListener(v -> {
            List<BaseListItem> usersWhoRatedList = resource.getUsersWhoRated();

            if (!alreadyRecommended(resource, userListItem.getId())) {
                if (usersWhoRatedList == null) {
                    usersWhoRatedList = new ArrayList<>();
                    Timber.d("usersWhoRatedAre Null...adding");
                }
                usersWhoRatedList.add(userListItem);
                resource.setUsersWhoRated(usersWhoRatedList);
                resource.setRating(resource.getRating() + 1);
            } else {

                usersWhoRatedList.remove(positionInList(usersWhoRatedList, userListItem));
                resource.setUsersWhoRated(usersWhoRatedList);
                resource.setRating(resource.getRating() - 1);
            }
            notifyItemChanged(position);
            BusProvider.getBusProviderInstance().post(new ResourceUpdateEvent(resource));
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resource.getUrl() != null) {
                    BusProvider.getBusProviderInstance().post(new ResourceSelectedEvent(resource.getUrl()));
                }
                Timber.d("Resource Item Clicked");
            }
        });
    }
}
