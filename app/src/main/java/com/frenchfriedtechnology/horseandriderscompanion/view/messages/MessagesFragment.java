package com.frenchfriedtechnology.horseandriderscompanion.view.messages;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Message;
import com.frenchfriedtechnology.horseandriderscompanion.view.adapters.MessagesAdapter;

import org.parceler.Parcels;

import java.util.List;

import timber.log.Timber;

/**
 * Fragment for Messages ViewPager
 */

public class MessagesFragment extends Fragment {

    private static final String MESSAGES = "Messages";
    private static int EMPTY = 0;
    private static int CONTENT = 1;

    public static MessagesFragment newInstance(List<Message> messages) {

        Bundle args = new Bundle();
        Parcelable messagesParcelable = Parcels.wrap(messages);
        MessagesFragment fragment = new MessagesFragment();
        args.putParcelable(MESSAGES, messagesParcelable);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        initMessages(rootView);

        return rootView;
    }

    private void initMessages(View view) {

        //----setup Messages
        List<Message> messages = Parcels.unwrap(getArguments().getParcelable(MESSAGES));

        Timber.d("initMessages() messages empty: " + messages.isEmpty());
        ViewFlipper viewFlipper = (ViewFlipper) view.findViewById(R.id.messages_view_flipper);
        viewFlipper.setDisplayedChild(messages.isEmpty() ? EMPTY : CONTENT);
        MessagesAdapter messagesAdapter = new MessagesAdapter(messages);
        RecyclerView messagesRecycler = (RecyclerView) view.findViewById(R.id.messages_recycler);
        messagesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        messagesRecycler.setAdapter(messagesAdapter);

    }
}
