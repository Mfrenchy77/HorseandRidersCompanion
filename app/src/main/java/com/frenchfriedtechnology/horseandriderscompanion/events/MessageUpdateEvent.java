package com.frenchfriedtechnology.horseandriderscompanion.events;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Message;

public class MessageUpdateEvent {
    private Message updatedMessage;

    public MessageUpdateEvent(Message updatedMessage) {
        this.updatedMessage = updatedMessage;
    }

    public Message getUpdatedMessage() {
        return updatedMessage;
    }
}
