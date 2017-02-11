package com.frenchfriedtechnology.horseandriderscompanion.events;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Message;

public class MessageSelectedEvent {
    private Message message;

    public MessageSelectedEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
