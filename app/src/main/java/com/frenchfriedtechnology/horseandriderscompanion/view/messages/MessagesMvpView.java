package com.frenchfriedtechnology.horseandriderscompanion.view.messages;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Message;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.MvpView;

import java.util.List;


public interface MessagesMvpView extends MvpView {
    void getMessages(List<Message> messages);
    void showMessageActions(Message message);
}
