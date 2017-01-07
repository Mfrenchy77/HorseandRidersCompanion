package com.frenchfriedtechnology.horseandriderscompanion.view.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.frenchfriedtechnology.horseandriderscompanion.BusProvider;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Category;
import com.frenchfriedtechnology.horseandriderscompanion.events.CategoryCreateEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.CategoryDeleteEvent;

import org.parceler.Parcels;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import timber.log.Timber;

/**
 * Dialog for creating and editing Category
 */

public class DialogCreateCategory extends DialogFragment {

    @StringDef({NEW_CATEGORY, EDIT_CATEGORY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Tags {
    }

    public static final String EDIT_CATEGORY = "EDIT_CATEGORY";
    public static final String NEW_CATEGORY = "NEW_CATEGORY";
    private TextInputEditText categoryName, categoryDescription;
    public static final String TAG = "TAG";
    private static final String CATEGORY = "Category";

    public static DialogCreateCategory newInstance(@StringRes String tag, @Nullable Category category) {

        Bundle args = new Bundle();
        args.putParcelable(CATEGORY, Parcels.wrap(category));
        args.putString(TAG, tag);

        DialogCreateCategory fragment = new DialogCreateCategory();
        fragment.setArguments(args);
        return fragment;
    }

    private String tag;
    private Category category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = getArguments().getString(TAG);
        category = Parcels.unwrap(getArguments().getParcelable(CATEGORY));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final FrameLayout fl = new FrameLayout(getActivity());
        final View view = inflater.inflate(R.layout.dialog_create_skill_tree_item, fl, false);
        fl.addView(view);

        categoryName = (TextInputEditText) view.findViewById(R.id.item_name);
        categoryDescription = (TextInputEditText) view.findViewById(R.id.item_description);

        TextView dialogTitle = (TextView) view.findViewById(R.id.dialog_title);
        dialogTitle.setText(tag.equals(EDIT_CATEGORY) ? "Edit Category" : "Create New Category");

        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.delete_item_button);
        deleteButton.setVisibility(tag.equals(EDIT_CATEGORY) ? View.VISIBLE : View.GONE);
        deleteButton.setOnClickListener(view1 -> {
            if (category.getName() != null) {
                BusProvider.getBusProviderInstance().post(new CategoryDeleteEvent(category.getName()));
                dismiss();
            } else {
                Timber.d("DeleteCategory Error:  name is null");
            }
        });

        //Edit Category
        if (tag.equals(EDIT_CATEGORY)) {
            categoryName.setText(category.getName());
            categoryDescription.setText(category.getDescription());
        }

        LinearLayout createButton = (LinearLayout) view.findViewById(R.id.accept_button);
        createButton.setOnClickListener(v -> {
            String name = categoryName.getText().toString().trim();
            String description = categoryDescription.getText().toString().trim();

            if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(description)) {
                BusProvider.getBusProviderInstance().post(new CategoryCreateEvent(description, name));
                dismiss();
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
}
