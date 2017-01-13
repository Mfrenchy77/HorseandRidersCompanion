package com.frenchfriedtechnology.horseandriderscompanion.data.entity;

import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;

/**
 * Model representing a Message
 */
public class Message {

    long id;

    String sender;

    String recipient;

    String title;

    String message;

    @Constants.MessageType
    int messageType;

    @Constants.MessageState
    int messageState = Constants.UNREAD;

}
