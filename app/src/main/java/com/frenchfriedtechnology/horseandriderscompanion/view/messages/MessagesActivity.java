package com.frenchfriedtechnology.horseandriderscompanion.view.messages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Message;
import com.frenchfriedtechnology.horseandriderscompanion.view.adapters.MessagesPagerAdapter;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BaseActivity;
import com.frenchfriedtechnology.horseandriderscompanion.view.dialogs.DialogMessage;
import com.frenchfriedtechnology.horseandriderscompanion.view.dialogs.DialogMessageAction;
import com.frenchfriedtechnology.horseandriderscompanion.view.dialogs.DialogNewMessage;

import java.util.List;

import javax.inject.Inject;

public class MessagesActivity extends BaseActivity implements MessagesMvpView {

    @Inject
    MessagesPresenter presenter;
    private MessagesPagerAdapter messagesPagerAdapter;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_tab_layout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        presenter.attachView(this);
        presenter.getMessages();
        FloatingActionButton newMessageButton = (FloatingActionButton) findViewById(R.id.add_fab);
        newMessageButton.setOnClickListener(v -> newMessage());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!presenter.isViewAttached()) {
            presenter.attachView(this);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void getMessages(List<Message> messages) {
        messagesPagerAdapter = new MessagesPagerAdapter(getSupportFragmentManager(), messages);
        ViewPager messagesViewPager = (ViewPager) findViewById(R.id.view_pager);
        messagesViewPager.setAdapter(messagesPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(messagesViewPager);
        messagesPagerAdapter.notifyDataSetChanged();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MessagesActivity.class);
        context.startActivity(intent);
    }

    private void newMessage() {
        //open new Message Dialog
        DialogNewMessage.newInstance().show(getFragmentManager(), null);
    }

    @Override
    public void showMessageActions(Message message) {
        DialogMessageAction.newInstance(message).show(getFragmentManager(), null);
    }

    @Override
    public void showMessage(Message message) {
        DialogMessage.newInstance(message).show(getFragmentManager(), null);
    }
}
