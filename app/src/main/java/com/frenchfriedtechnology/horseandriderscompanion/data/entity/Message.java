package com.frenchfriedtechnology.horseandriderscompanion.data.entity;

import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;

import org.parceler.Parcel;

/**
 * Model representing a Message
 */
@Parcel
public class Message {

    long id;

    String sender;

    String recipient;

    String subject;

    String message;

    @Constants.MessageType
    int messageType;

    @Constants.MessageState
    int messageState = Constants.UNREAD;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Constants.MessageState
    public int getMessageState() {
        return messageState;
    }

    public void setMessageState(@Constants.MessageState int messageState) {
        this.messageState = messageState;
    }
@Constants.MessageType
    public int getMessageType() {
        return messageType;
    }
@Constants.MessageType
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
