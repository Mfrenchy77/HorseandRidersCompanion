package com.frenchfriedtechnology.horseandriderscompanion.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.frenchfriedtechnology.horseandriderscompanion.BusProvider;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Message;
import com.frenchfriedtechnology.horseandriderscompanion.events.MessageUpdateEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.ProfileBlockEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.ProfileSelectedEvent;
import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;

import org.parceler.Parcels;

import timber.log.Timber;

/**
 * Message actions
 */

public class DialogMessageAction extends DialogFragment {

    private static final String MESSAGE = "Message";

    public static DialogMessageAction newInstance(Message message) {

        Bundle args = new Bundle();

        DialogMessageAction fragment = new DialogMessageAction();
        args.putParcelable(MESSAGE, Parcels.wrap(message));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Message message = Parcels.unwrap(getArguments().getParcelable(MESSAGE));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_message_actions, null);
        builder.setView(view);

        TextView profileText = (TextView) view.findViewById(R.id.dialog_message_action_sender_profile_text);
        CardView profile = (CardView) view.findViewById(R.id.dialog_message_action_sender_profile);
        TextView blockText = (TextView) view.findViewById(R.id.dialog_message_action_block_text);
        CardView block = (CardView) view.findViewById(R.id.dialog_message_action_block);
        CardView read = (CardView) view.findViewById(R.id.dialog_message_action_mark);

        profileText.setText(message.getSender());
        blockText.setText(String.format("Block %s", message.getSender()));

        profile.setOnClickListener(v -> {
            Timber.d("Show Profile");
            BusProvider.getBusProviderInstance().post(new ProfileSelectedEvent(message.getSender()));
            dismiss();
        });
        block.setOnClickListener(v -> {
            Timber.d("Block Profile");
            BusProvider.getBusProviderInstance().post(new ProfileBlockEvent(message.getSender()));
            dismiss();
        });
        read.setOnClickListener(v -> {
            message.setMessageState(Constants.READ);
            BusProvider.getBusProviderInstance().post(new MessageUpdateEvent(message));
            dismiss();
        });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.getBusProviderInstance().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        BusProvider.getBusProviderInstance().unregister(this);
    }
}
