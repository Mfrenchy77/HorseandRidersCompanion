package com.frenchfriedtechnology.horseandriderscompanion.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.frenchfriedtechnology.horseandriderscompanion.AccountManager;
import com.frenchfriedtechnology.horseandriderscompanion.BusProvider;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Message;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.events.MessageNewEvent;

import timber.log.Timber;

/**
 * Send a new Message
 */

public class DialogNewMessage extends DialogFragment implements AdapterView.OnItemSelectedListener {

    public static DialogNewMessage newInstance() {

        Bundle args = new Bundle();

        DialogNewMessage fragment = new DialogNewMessage();
        fragment.setArguments(args);
        return fragment;
    }

    private TextInputEditText recipient, message;
    private String subject;
    private AppCompatSpinner messageTypeSpinner;
    private ArrayAdapter<CharSequence> spinnerAdapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final FrameLayout fl = new FrameLayout(getActivity());
        final View view = inflater.inflate(R.layout.dialog_new_message, fl, false);
        fl.addView(view);
        recipient = (TextInputEditText) view.findViewById(R.id.dialog_new_message_recipient);
        message = (TextInputEditText) view.findViewById(R.id.dialog_new_message_message);
        messageTypeSpinner = (AppCompatSpinner) view.findViewById(R.id.dialog_new_message_type_spinner);
        spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.message_types, android.R.layout.simple_spinner_dropdown_item);

        messageTypeSpinner.setAdapter(spinnerAdapter);
        messageTypeSpinner.setOnItemSelectedListener(this);
        messageTypeSpinner.setSelection(5);

        LinearLayout sendButton = (LinearLayout) view.findViewById(R.id.accept_button);
        sendButton.setOnClickListener(v -> {
            String recipientText = recipient.getText().toString().toLowerCase().trim();
            String messageText = message.getText().toString().trim();
            if (!TextUtils.isEmpty(recipientText) || !TextUtils.isEmpty(messageText)) {
                if (messageTypeSpinner.getSelectedItemPosition() == 5) {

                    Toast.makeText(getActivity(), "Please Select a Message Type", Toast.LENGTH_SHORT).show();
                    messageTypeSpinner.requestFocus();
                } else {
                    Message message = new Message();
                    message.setId(System.currentTimeMillis());
                    message.setMessage(messageText);
                    message.setRecipient(recipientText);
                    message.setMessageType(messageTypeSpinner.getSelectedItemPosition());
                    message.setSender(new UserPrefs().getUserEmail());
                    message.setSubject(messageTypeSpinner.getSelectedItem().toString());
                    BusProvider.getBusProviderInstance().post(new MessageNewEvent(message));
                    dismiss();
                }
            } else {
                Toast.makeText(getActivity(), "Fields must not be left blank", Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout cancelButton = (LinearLayout) view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> dismiss());
        builder.setView(fl);
        AlertDialog dialog = builder.create();
        BusProvider.getBusProviderInstance().register(this);

        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();

        return dialog;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Toast.makeText(getActivity(), "Selected " + position, Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(getActivity(), "Selected " + position, Toast.LENGTH_SHORT).show();

                break;
            case 2:
                Toast.makeText(getActivity(), "Selected " + position, Toast.LENGTH_SHORT).show();

                break;
            case 3:
                Toast.makeText(getActivity(), "Selected " + position, Toast.LENGTH_SHORT).show();

                break;
            case 4:
                Toast.makeText(getActivity(), "Selected " + position, Toast.LENGTH_SHORT).show();

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.removeViewAt(5);
    }
}
