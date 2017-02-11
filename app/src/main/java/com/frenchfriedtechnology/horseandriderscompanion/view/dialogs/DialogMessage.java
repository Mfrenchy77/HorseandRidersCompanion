package com.frenchfriedtechnology.horseandriderscompanion.view.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.frenchfriedtechnology.horseandriderscompanion.BusProvider;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Message;
import com.frenchfriedtechnology.horseandriderscompanion.util.ViewUtil;

import org.parceler.Parcels;

import timber.log.Timber;


public class DialogMessage extends DialogFragment {

    private static final String MESSAGE = "Message";

    private TextView messageTitle, messageSubject, messageBody, messageActionText;
    private LinearLayout cancelButton, acceptButton, buttonLayout;

    public static DialogMessage newInstance(@Nullable Message message) {

        Bundle args = new Bundle();
        args.putParcelable(MESSAGE, Parcels.wrap(message));

        DialogMessage fragment = new DialogMessage();
        fragment.setArguments(args);
        return fragment;
    }

    private Message message = new Message();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        message = Parcels.unwrap(getArguments().getParcelable(MESSAGE));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final FrameLayout fl = new FrameLayout(getActivity());
        final View view = inflater.inflate(R.layout.dialog_mesage, fl, false);
        fl.addView(view);
        messageTitle = (TextView) fl.findViewById(R.id.dialog_message_title);
        messageSubject = (TextView) fl.findViewById(R.id.dialog_message_subject);
        messageBody = (TextView) fl.findViewById(R.id.dialog_message_body);
        messageActionText = (TextView) fl.findViewById(R.id.dialog_message_action_text);
        Timber.d("MessageType: " + message.getMessageType());
        messageTitle.setText(message.getSender());
        messageSubject.setText(message.getSubject());
        messageBody.setText(message.getMessage());
        buttonLayout = (LinearLayout) fl.findViewById(R.id.dialog_message_buttons_layout);
/*
        messageActionText.setText(message.getMessageType());
*/
        buttonLayout.setVisibility(message.getMessageType() > 3 ? View.GONE : View.VISIBLE);
        acceptButton = (LinearLayout) view.findViewById(R.id.accept_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        cancelButton = (LinearLayout) view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> dismiss());
        builder.setView(fl);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        BusProvider.getBusProviderInstance().register(this);

        dialog.show();

        ViewUtil.hideKeyboard(getActivity());
        return dialog;
    }


}
