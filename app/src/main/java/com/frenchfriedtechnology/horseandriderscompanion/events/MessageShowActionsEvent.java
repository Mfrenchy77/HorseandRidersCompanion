package com.frenchfriedtechnology.horseandriderscompanion.events;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Message;

public class MessageShowActionsEvent {
    private Message message;

    public MessageShowActionsEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
