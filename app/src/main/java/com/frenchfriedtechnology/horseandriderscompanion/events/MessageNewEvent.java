package com.frenchfriedtechnology.horseandriderscompanion.events;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Message;

public class MessageNewEvent {

    private Message message;

    public MessageNewEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
