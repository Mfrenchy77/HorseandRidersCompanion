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

import com.frenchfriedtechnology.horseandriderscompanion.AccountManager;
import com.frenchfriedtechnology.horseandriderscompanion.BusProvider;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Level;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.events.LevelCreateEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.LevelDeleteEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.LevelAdjustedEvent;
import com.frenchfriedtechnology.horseandriderscompanion.util.Constants;
import com.frenchfriedtechnology.horseandriderscompanion.util.ViewUtil;

import org.parceler.Parcels;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import timber.log.Timber;

/**
 * Dialog for create/editing a level, also used for updating Horse/Rider Profile SkillLevel
 */
// TODO: 10/12/16 add Resources at the bottom
public class DialogCreateAdjustLevel extends DialogFragment {


    @StringDef({NEW_LEVEL, EDIT_LEVEL, RIDER_ADJUST, HORSE_ADJUST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Tags {
    }

    public static final String TAG = "TAG";
    private static final String LEVEL = "LEVEL";
    private static final String SKILL_ID = "Skill_Id";
    public static final String NEW_LEVEL = "NEW_LEVEL";
    public static final String EDIT_LEVEL = "EDIT_LEVEL";
    public static final String RIDER_ADJUST = "RIDER_ADJUST";
    public static final String HORSE_ADJUST = "HORSE_ADJUST";

    private TextInputEditText levelName;
    private TextInputEditText learningLevelDescription;
    private TextInputEditText completeLevelDescription;


    public static DialogCreateAdjustLevel newInstance(@StringRes String tag, @Nullable Level level, String skillId) {

        Bundle args = new Bundle();
        args.putParcelable(LEVEL, Parcels.wrap(level));
        args.putString(TAG, tag);
        args.putString(SKILL_ID, skillId);

        DialogCreateAdjustLevel fragment = new DialogCreateAdjustLevel();
        fragment.setArguments(args);
        return fragment;
    }

    private String tag, skillId;
    private Level level = new Level();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = getArguments().getString(TAG);
        level = Parcels.unwrap(getArguments().getParcelable(LEVEL));
        skillId = getArguments().getString(SKILL_ID);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final FrameLayout fl = new FrameLayout(getActivity());

        final View view = inflater.inflate(
                tag.equals(RIDER_ADJUST) ? R.layout.dialog_adjust_level :
                        tag.equals(HORSE_ADJUST) ? R.layout.dialog_adjust_level :
                                R.layout.dialog_create_skill_tree_item, fl, false);
        fl.addView(view);

        switch (tag) {
            case RIDER_ADJUST:
                initializeAdjust(view);
                break;
            case HORSE_ADJUST:
                initializeAdjust(view);
                break;
            default:
                initializeCreate(view);
                break;
        }

        builder.setView(fl);
        AlertDialog dialog = builder.create();
        BusProvider.getBusProviderInstance().register(this);

        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();

        return dialog;
    }

    private void initializeCreate(View view) {
        levelName = (TextInputEditText) view.findViewById(R.id.item_name);
        learningLevelDescription = (TextInputEditText) view.findViewById(R.id.level_create_description_learning);
        completeLevelDescription = (TextInputEditText) view.findViewById(R.id.level_create_description_complete);
        learningLevelDescription.setVisibility(View.VISIBLE);
        completeLevelDescription.setVisibility(View.VISIBLE);
        TextInputEditText description1 = (TextInputEditText) view.findViewById(R.id.item_description);
        description1.setVisibility(View.GONE);
        TextView dialogTitle = (TextView) view.findViewById(R.id.dialog_title);
        dialogTitle.setText(tag.equals(EDIT_LEVEL) ? "Edit Level" : "Create New Level");

        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.delete_item_button);
        deleteButton.setVisibility(tag.equals(EDIT_LEVEL) ? View.VISIBLE : View.GONE);
        deleteButton.setOnClickListener(view1 -> {
            if (level.getId() != null) {
                BusProvider.getBusProviderInstance().post(new LevelDeleteEvent(level.getId()));
                dismiss();
            } else {
                Timber.d("Delete Level Error:  name is null");
            }
        });

        //setup Edit Level text
        if (tag.equals(EDIT_LEVEL)) {
            levelName.setText(level.getLevelName());
            learningLevelDescription.setText(level.getLearningDescription());
            completeLevelDescription.setText(level.getCompleteDescription());
        }

        LinearLayout createButton = (LinearLayout) view.findViewById(R.id.accept_button);


        //----Create/Edit Level if fields are not empty
        createButton.setOnClickListener(v -> {
            String name = levelName.getText().toString().trim();
            String learningDescription = learningLevelDescription.getText().toString().trim();
            String completeDescription = completeLevelDescription.getText().toString().trim();

            Level newLevel = new Level();
            newLevel.setLevelName(name);
            newLevel.setId(tag.equals(EDIT_LEVEL) ? level.getId() : ViewUtil.createId());
            newLevel.setLearningDescription(learningLevelDescription.getText().toString().trim());
            newLevel.setCompleteDescription(completeLevelDescription.getText().toString().trim());
            newLevel.setSkillId(skillId);
            newLevel.setLastEditBy(AccountManager.currentUser());
            newLevel.setLastEditDate(System.currentTimeMillis());

            if (!TextUtils.isEmpty(name) ||
                    !TextUtils.isEmpty(learningDescription) ||
                    !TextUtils.isEmpty(completeDescription)) {
                BusProvider.getBusProviderInstance().post(new LevelCreateEvent(newLevel, tag.equals(EDIT_LEVEL)));
                dismiss();
            } else {
                Toast.makeText(getActivity(), "Fields must not be left blank", Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout cancelButton = (LinearLayout) view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> dismiss());
    }

    /**
     * This is the main interface for a rider to update their Skill Tree, this will add
     * an attribute to their profile with the skill Id and their Level associated with it.
     * Also, Provides resources(url's to videos articles on training/learning content)
     */
    private void initializeAdjust(View view) {
        TextView lName = (TextView) view.findViewById(R.id.adjust_level_name);
        TextView completeDescription = (TextView) view.findViewById(R.id.adjust_level_description_complete);
        TextView learningDescription = (TextView) view.findViewById(R.id.adjust_level_description_learning);
        TextView verified = (TextView) view.findViewById(R.id.adjust_verify);
        TextView noProgress = (TextView) view.findViewById(R.id.adjust_no_progress);
        TextView learning = (TextView) view.findViewById(R.id.adjust_learning);
        TextView complete = (TextView) view.findViewById(R.id.adjust_complete);

        lName.setText(level.getLevelName());
        learningDescription.setText(String.format("To be considered Learning:\n\n%s", level.getLearningDescription()));
        completeDescription.setText(String.format("To be considered Complete:\n\n%s", level.getCompleteDescription()));

        // FIXME: 12/12/16 This needs to be more that just if instructor or not,
        // needs to know if their own level is high enough to verify and check if horse or rider is a student
        verified.setVisibility(new UserPrefs().isInstructor() ? View.VISIBLE : View.GONE);

        complete.setOnClickListener(view14 -> {
            level.setLevel(Constants.COMPLETE);
            BusProvider.getBusProviderInstance().post(
                    new LevelAdjustedEvent(level.getId(), level.getLevel(), tag.equals(RIDER_ADJUST)));
            dismiss();
        });
        noProgress.setOnClickListener(view13 -> {
            level.setLevel(Constants.NO_PROGRESS);
            BusProvider.getBusProviderInstance().post(
                    new LevelAdjustedEvent(level.getId(), level.getLevel(), tag.equals(RIDER_ADJUST)));
            dismiss();
        });
        learning.setOnClickListener(view12 -> {
            level.setLevel(Constants.LEARNING);
            BusProvider.getBusProviderInstance().post(
                    new LevelAdjustedEvent(level.getId(), level.getLevel(), tag.equals(RIDER_ADJUST)));
            dismiss();
        });
        verified.setOnClickListener(view1 -> {
            level.setLevel(Constants.VERIFIED);
            BusProvider.getBusProviderInstance().post(
                    new LevelAdjustedEvent(level.getId(), level.getLevel(), tag.equals(RIDER_ADJUST)));
            dismiss();
        });
    }
}
