package com.frenchfriedtechnology.horseandriderscompanion.view.adapters;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frenchfriedtechnology.horseandriderscompanion.BusProvider;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Message;
import com.frenchfriedtechnology.horseandriderscompanion.events.MessageShowActionsEvent;

import java.util.List;

import timber.log.Timber;

/**
 * Adapter for list of Messages
 */

public class MessagesAdapter extends BaseQuickAdapter<Message> {


    public MessagesAdapter(List<Message> data) {
        super(R.layout.item_message, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Message message) {
        //----setup message
        holder.setText(R.id.message_from, "From: " + message.getSender());
        holder.setText(R.id.message_subject, message.getSubject());
        holder.setText(R.id.message_content, message.getMessage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.d("Message clicked");
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                BusProvider.getBusProviderInstance().post(new MessageShowActionsEvent(message));
                return true;
            }
        });
    }
}
