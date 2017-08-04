package com.frenchfriedtechnology.horseandriderscompanion.view.messages;

import com.frenchfriedtechnology.horseandriderscompanion.data.endpoints.MessagesApi;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Message;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.events.MessageNewEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.MessageSelectedEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.MessageShowActionsEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.MessageUpdateEvent;
import com.frenchfriedtechnology.horseandriderscompanion.util.ViewUtil;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BasePresenter;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class MessagesPresenter extends BasePresenter<MessagesMvpView> {


    @Inject
    public MessagesPresenter() {
    }

    void getMessages() {
        Timber.d("getMessages() called");
        new MessagesApi().getMessages(new ViewUtil().convertEmailToPath(new UserPrefs().getUserEmail()), new MessagesApi.MessagesCallback() {
            @Override
            public void onSuccess(List<Message> messages) {
                getMvpView().getMessages(messages);
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e("Error retrieving Messages: " + throwable);
            }
        });
    }

    @Subscribe
    public void newMessageEvent(MessageNewEvent event) {
        Timber.d("Send Message to: " + event.getMessage().getRecipient());
        if (isViewAttached()) {
            new MessagesApi().createOrUpdate(event.getMessage());
        }
    }

    @Subscribe
    public void updateMessageEvent(MessageUpdateEvent event) {
        Timber.d("updateMessageEvent()");
        if (isViewAttached()) {
            new MessagesApi().createOrUpdate(event.getUpdatedMessage());
        }
    }

    @Subscribe
    public void showMessageActionEvent(MessageShowActionsEvent event) {
        if (isViewAttached()) {
            getMvpView().showMessageActions(event.getMessage());
        }
    }

    @Subscribe
    public void messageSelectedEvent(MessageSelectedEvent event) {
        if (isViewAttached()) {
            getMvpView().showMessage(event.getMessage());
        }
    }
}
