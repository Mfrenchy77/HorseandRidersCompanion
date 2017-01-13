package com.frenchfriedtechnology.horseandriderscompanion.view.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frenchfriedtechnology.horseandriderscompanion.BusProvider;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.events.ResourceSelectedEvent;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.List;

import timber.log.Timber;


/**
 * Adapter for showing a list of Resources
 */

public class ResourceAdapter extends BaseQuickAdapter<Resource> {

    public ResourceAdapter(List<Resource> data) {
        super(R.layout.item_resource, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Resource resource) {
        int position = holder.getAdapterPosition();

        //----setup resource values and click events
        holder.setText(R.id.resource_title, resource.getName());
        TextView ratingCount = holder.getView(R.id.resource_rating_count);
        ratingCount.setText(String.valueOf(resource.getRating()));
        holder.setText(R.id.resource_description, resource.getDescription());
        SimpleRatingBar ratingBar = holder.getView(R.id.resource_rating_bar);
        ratingBar.setRating(resource.getRating());
        ratingBar.setOnRatingBarChangeListener(new SimpleRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(SimpleRatingBar simpleRatingBar, float rating, boolean fromUser) {
                //update server and rating bar
                resource.setRating((long) (resource.getRating() + rating));
                ratingCount.setText(String.valueOf(resource.getRating()));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resource.getUrl()!=null){
                    BusProvider.getBusProviderInstance().post(new ResourceSelectedEvent(resource.getUrl()));
                }
                Timber.d("Resource Item Clicked");
            }
        });
    }
}
