package com.frenchfriedtechnology.horseandriderscompanion.data.endpoints;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Message;
import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;
import com.frenchfriedtechnology.horseandriderscompanion.util.ViewUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.DataSnapshotMapper;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;

import java.util.List;

import timber.log.Timber;

/**
 * Interface for Firebase and Message
 */

public class MessagesApi {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    public void createOrUpdate(Message message) {
        //send Message
        Timber.d("sendMessage() called");
        String path = new ViewUtil().convertEmailToPath(message.getRecipient());
        databaseReference.child(Constants.MESSAGES)
                .child(path)
                .child(String.valueOf(message.getId()))
                .setValue(message);
    }

    public void getMessages(String userId, MessagesCallback callback) {
        //retrieve Messages for User
        Timber.d("getMessages() called");
        RxFirebaseDatabase.observeValueEvent(databaseReference
                .child(Constants.MESSAGES)
                .child(userId), DataSnapshotMapper.listOf(Message.class))
                .subscribe(callback::onSuccess, callback::onError);
    }

    public void deleteMessage(Message messageToDelete) {
        //delete message

    }

    public interface MessagesCallback {

        void onSuccess(List<Message> messages);

        void onError(final Throwable throwable);
    }
}

