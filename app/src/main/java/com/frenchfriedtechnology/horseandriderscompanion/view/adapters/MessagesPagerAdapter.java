package com.frenchfriedtechnology.horseandriderscompanion.view.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Message;
import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;
import com.frenchfriedtechnology.horseandriderscompanion.view.messages.MessagesFragment;

import java.util.ArrayList;
import java.util.List;

public class MessagesPagerAdapter extends FragmentStatePagerAdapter {
    private List<Message> messages;

    private final FragmentManager mFragmentManager;
    private SparseArray<Fragment> mFragments;
    private FragmentTransaction mCurTransaction;

    public MessagesPagerAdapter(FragmentManager fm, List<Message> messages) {
        super(fm);
        this.messages = messages;
        mFragmentManager = fm;
        mFragments = new SparseArray<>();
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = getItem(position);
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        mCurTransaction.add(container.getId(), fragment, "fragment:" + position);
        mFragments.put(position, fragment);

        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        mCurTransaction.detach(mFragments.get(position));
        mFragments.remove(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object fragment) {
        return ((Fragment) fragment).getView() == view;
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }
    private List<Message> getSortedMessages(@Constants.MessageState int messageState) {
        List<Message> sortedMessages = new ArrayList<>();
        for (int i = 0; i < getMessages().size(); i++) {
            if (getMessages().get(i).getMessageState() == messageState) {
                sortedMessages.add(getMessages().get(i));
            }
        }
        return sortedMessages;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MessagesFragment.newInstance(getSortedMessages(Constants.UNREAD));
            case 1:
                return MessagesFragment.newInstance(messages);
            case 2:
                return MessagesFragment.newInstance(getSortedMessages(Constants.READ));
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Unread";
            case 1:
                return "All";
            case 2:
                return "Read";
            default:
                return null;
        }
    }
}
