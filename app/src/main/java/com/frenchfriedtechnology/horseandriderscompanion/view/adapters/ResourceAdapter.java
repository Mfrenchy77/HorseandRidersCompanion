package com.frenchfriedtechnology.horseandriderscompanion.view.adapters;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


/**
 * Adapter for showing a list of Resources
 */

public class ResourceAdapter extends BaseQuickAdapter<Resource> {
    private List<Resource> resources = new ArrayList<>();

    public ResourceAdapter(List<Resource> data) {
        super(R.layout.item_resource, data);
        resources = data;
    }

    @Override
    protected void convert(BaseViewHolder holder, Resource resource) {
        int position = holder.getAdapterPosition();

        //----setup resource values and click events
        holder.getView(R.id.resource_layout).setVisibility(resources.isEmpty() ? View.GONE : View.VISIBLE);
        holder.setText(R.id.resource_title, resource.getTitle());
        holder.setText(R.id.resource_rating_count, (int) resource.getRating());
        holder.setText(R.id.resource_description, resource.getDescription());
        SimpleRatingBar ratingBar = holder.getView(R.id.resource_rating_bar);
        ratingBar.setRating(resource.getRating());
        ratingBar.setOnRatingBarChangeListener(new SimpleRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(SimpleRatingBar simpleRatingBar, float rating, boolean fromUser) {
                //update server and rating bar
                resource.setRating((long) (resource.getRating() + rating));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timber.d("Resource Item Clicked");
            }
        });
        Timber.d("Resources Empty? " + resources.isEmpty());
        //----setup empty list
        TextView emptyResource = holder.getView(R.id.empty_resource);
        emptyResource.setVisibility(resources.isEmpty() ? View.VISIBLE : View.GONE);
        emptyResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timber.d("Empty Resource clicked");
                //open add resource dialog
            }
        });
    }
}
