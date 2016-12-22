package com.frenchfriedtechnology.horseandriderscompanion.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.frenchfriedtechnology.horseandriderscompanion.R;

import java.util.List;

/**
 * Adapter for showing a list of Strings
 */

public class StringAdapter extends ArrayAdapter<String> {
    private final LayoutInflater inflater;

    public StringAdapter(Context context, List<String> objects) {
        super(context, R.layout.item_user_account, objects);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView item;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_user_account, parent, false);

            item = (TextView) convertView.findViewById(R.id.list_item_user_account_field);
            convertView.setTag(item);
        } else {
            item = (TextView) convertView.getTag();
        }

        item.setText(getItem(position));
        return convertView;
    }
}
