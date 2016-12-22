package com.frenchfriedtechnology.horseandriderscompanion.view.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.frenchfriedtechnology.horseandriderscompanion.BusProvider;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.AppPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.events.SwitchAccountEvent;
import com.frenchfriedtechnology.horseandriderscompanion.view.adapters.StringAdapter;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * A dialog which allows the user to select from a list of accounts which have been added to the app.
 */
public class DialogSwitchAccount extends DialogFragment {

    private static final int VIEW_EMPTY = 0;
    private static final int VIEW_CONTENT = 1;

    @Bind(R.id.dialog_switch_account_flipper)
    ViewFlipper flipper;
    @Bind(R.id.dialog_switch_account_listview)
    ListView accountListView;

    @OnItemClick(R.id.dialog_switch_account_listview)
    void onAccountSelected(int position) {
        String user = adapter.getItem(position);
        AppPrefs.setActiveUser(user);
        BusProvider.getBusProviderInstance().post(new SwitchAccountEvent());
        Toast.makeText(getActivity(), "Switch to " + user, Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private StringAdapter adapter;

    public
    @NonNull
    Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.dialog_title_select_account);
        dialog.setNegativeButton(android.R.string.cancel, null);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_switch_accounts, null);
        ButterKnife.bind(this, view);
        dialog.setView(view);

        List<String> userList = new ArrayList<>();
        userList.addAll(AppPrefs.getUserAccounts());
        userList.remove(AppPrefs.getActiveUser());
        Collections.sort(userList);

        adapter = new StringAdapter(getActivity(), userList);
        accountListView.setAdapter(adapter);
        accountListView.setDivider(null);

        flipper.setDisplayedChild(userList.isEmpty() ? VIEW_EMPTY : VIEW_CONTENT);

        return dialog.create();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
