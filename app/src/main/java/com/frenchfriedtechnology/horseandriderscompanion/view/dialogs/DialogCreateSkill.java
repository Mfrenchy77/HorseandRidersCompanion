package com.frenchfriedtechnology.horseandriderscompanion.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputEditText;
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
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Skill;
import com.frenchfriedtechnology.horseandriderscompanion.events.SkillCreateEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.SkillDeleteEvent;

import org.parceler.Parcels;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import timber.log.Timber;

/**
 * Create or edit Skill
 */

public class DialogCreateSkill extends DialogFragment {

    @StringDef({NEW_SKILL, EDIT_SKILL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Tags {
    }

    public static final String TAG = "TAG";
    private static final String SKILL = "SKILL";
    public static final String NEW_SKILL = "NEW_SKILL";
    public static final String EDIT_SKILL = "EDIT_SKILL";
    private static final String CATEGORY_ID = "Category_Id";

    private TextInputEditText skillName, skillDescription;

    public static DialogCreateSkill newInstance(@StringRes String tag, @Nullable Skill skill, String categoryId) {

        Bundle args = new Bundle();
        args.putParcelable(SKILL, Parcels.wrap(skill));
        args.putString(TAG, tag);
        args.putString(CATEGORY_ID, categoryId);

        DialogCreateSkill fragment = new DialogCreateSkill();
        fragment.setArguments(args);
        return fragment;
    }

    private String tag, categoryId;
    private Skill skill = new Skill();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = getArguments().getString(TAG);
        categoryId = getArguments().getString(CATEGORY_ID);
        skill = Parcels.unwrap(getArguments().getParcelable(SKILL));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final FrameLayout fl = new FrameLayout(getActivity());
        final View view = inflater.inflate(R.layout.dialog_create_skill_tree_item, fl, false);
        fl.addView(view);

        skillName = (TextInputEditText) view.findViewById(R.id.item_name);
        skillDescription = (TextInputEditText) view.findViewById(R.id.item_description);
        TextView dialogTitle = (TextView) view.findViewById(R.id.dialog_title);

        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.delete_item_button);
        deleteButton.setVisibility(tag.equals(EDIT_SKILL) ? View.VISIBLE : View.GONE);
        deleteButton.setOnClickListener(view1 -> {
            if (skill.getId() != null) {
                BusProvider.getBusProviderInstance().post(new SkillDeleteEvent(skill.getId()));
                dismiss();
            } else {
                Timber.d("DeleteSkill Error:  name is null");
            }
        });
        dialogTitle.setText(tag.equals(EDIT_SKILL) ? "Edit Skill" : "Create New Skill");

        //Edit SKILL
        if (tag.equals(EDIT_SKILL)) {
            skillName.setText(skill.getSkillName());
            skillDescription.setText(skill.getDescription());
        }

        LinearLayout createButton = (LinearLayout) view.findViewById(R.id.accept_button);
        createButton.setOnClickListener(v -> {
            String name = skillName.getText().toString().trim();
            String description = skillDescription.getText().toString().trim();
            if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(description)) {

                BusProvider.getBusProviderInstance().post(new SkillCreateEvent(categoryId, description, name, tag.equals(EDIT_SKILL), tag.equals(EDIT_SKILL) ? skill.getId() : null))
                ;
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
