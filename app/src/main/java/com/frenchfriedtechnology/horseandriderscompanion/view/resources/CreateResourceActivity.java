package com.frenchfriedtechnology.horseandriderscompanion.view.resources;

import android.content.Intent;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.ThumbnailRequestCoordinator;
import com.frenchfriedtechnology.horseandriderscompanion.AccountManager;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.BaseListItem;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.TreeNode;
import com.frenchfriedtechnology.horseandriderscompanion.util.ViewUtil;
import com.frenchfriedtechnology.horseandriderscompanion.view.adapters.SkillTreeSelectAdapter;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.OnClick;
import timber.log.Timber;

/**
 * Activity for Creating a resource and allows share intercepts from sources outside of application
 */

public class CreateResourceActivity extends BaseActivity implements CreateResourceMvpView {

    public static final String ACTION_CREATE_RESOURCE = "com.frenchfriedtechnology.horseandriderscompanion.ACTION_CREATE_RESOURCE";
    public static final String ACTION_EDIT_RESOURCE = "com.frenchfriedtechnology.horseandriderscompanion.ACTION_EDIT_RESOURCE";

    private static final String EXTRA_SHARE_SCREENSHOT_AS_STREAM = "share_screenshot_as_stream";
    public static final String EXTRA_KEY_TITLE = "EXTRA_KEY_TITLE";
    public static final String EXTRA_KEY_URL = "EXTRA_KEY_URL";
    public static final String EXTRA_KEY_RESOURCE = "EXTRA_KEY_RESOURCE";
    public static final String EXTRA_KEY_ID = "EXTRA_KEY_ID";

    private TextInputLayout createResourceTitleWrapper, createResourceDescriptionWrapper;
    private TextInputEditText resourceTitle, resourceLink, resourceDescription;
    private ImageView createResourceThumbnail;
    private RecyclerView skillTreeRecycler;
    private SkillTreeSelectAdapter skillTreeSelectAdapter;
    private Button createResourceButton;
    private CardView root;

    @Inject
    CreateResourcePresenter presenter;

    private boolean externalShare;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_create_resource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        presenter.attachView(this);
        presenter.getSkillTreeItems();
        Intent intent = getIntent();
        externalShare = Intent.ACTION_SEND.equals(intent.getAction());

        root = (CardView) findViewById(R.id.create_resource_root);
        resourceTitle = (TextInputEditText) findViewById(R.id.create_resource_title);
        resourceLink = (TextInputEditText) findViewById(R.id.create_resource_link);
        resourceDescription = (TextInputEditText) findViewById(R.id.create_resource_description);
        createResourceTitleWrapper = (TextInputLayout) findViewById(R.id.create_resource_title_wrapper);
        createResourceDescriptionWrapper = (TextInputLayout) findViewById(R.id.create_resource_description_wrapper);
        createResourceThumbnail = (ImageView) findViewById(R.id.create_resource_thumbnail);
        createResourceButton = (Button) findViewById(R.id.create_resource_button);
        createResourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitClicked();
            }
        });
        if (externalShare) {
            handleExternalShare(intent);
        } else {
            //handle EDITs
            Timber.d("Intent.ActionSend = Null");
        }

    }


    /**
     * Handles a share intent from a 3rd party
     *
     * @param intent an intent from a 3rd party
     */
    private void handleExternalShare(Intent intent) {
        if (intent.getType().equals("text/plain")) {
            Bundle extras = intent.getExtras();

            String text = extras.getString(Intent.EXTRA_TEXT);
            String title = extras.getString(Intent.EXTRA_TITLE);
            String subject = extras.getString(Intent.EXTRA_SUBJECT);
            try {

                // imageUri is always null
                Uri imageUri = intent.getParcelableExtra(EXTRA_SHARE_SCREENSHOT_AS_STREAM);
                // Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (imageUri != null) {
                    Timber.d("image exists");
                    Glide.with(this).load(imageUri).into(createResourceThumbnail);
                    createResourceThumbnail.setVisibility(View.VISIBLE);
                    createResourceThumbnail.setImageURI(null);
                    createResourceThumbnail.setImageURI(imageUri);
                } else {
                    createResourceThumbnail.setVisibility(View.GONE);
                }
            } catch (Throwable t) {
                Timber.d("no image available" + t.getMessage());
            }
            Timber.d("EXTRA: " + extras);
            Timber.d("EXTRA_STREAM: " + intent.getParcelableArrayExtra(EXTRA_SHARE_SCREENSHOT_AS_STREAM));
            Timber.d("EXTRA_TEXT: " + text);
            Timber.d("EXTRA_TITLE: " + title);
            Timber.d("EXTRA_SUBJECT: " + subject);

            if (subject != null) {
                resourceTitle.setText(subject);
            }
            if (title != null) {
                resourceTitle.setText(title); // override subject
            }

            if (text != null) {
                boolean isLink = URLUtil.isValidUrl(text);

                if (isLink) {
                    resourceLink.setText(text);
                } else {
                    resourceDescription.setText(text);
                }
            }
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private void onSubmitClicked() {
        Resource resource = validatePost();
        if (resource != null) {
            presenter.submitNewResource(resource);
            finish();
        }
 /*       ApiBodyUserSubmission body = validatePost();

        if (new AccountManager().isGuest()) { //Check if User is guest to prevent crashes
            SnackbarUtils.displayLongDismissibleSnackbar(root, Voater.getContext().getString(R.string.err_message_security));
            finish();
        } else if (body != null) {
            if (ACTION_CREATE_RESOURCE.equals(getIntent().getAction())) {
                BusProvider.Instance.getBus().post(new Content.Ui
                        .ContentPostEvent(body, contentSubverse.getText().toString(), false, id));
                finish();
            } else if (ACTION_EDIT_RESOURCE.equals(getIntent().getAction())) {
                BusProvider.Instance.getBus().post(new Content.Ui
                        .ContentPostEvent(body, contentSubverse.getText().toString(), true, id));
                finish();
            } else { // creating new post
                BusProvider.Instance.getBus().post(new Content.Ui
                        .ContentPostEvent(body, contentSubverse.getText().toString(), false, id));
                finish();
            }
            SnackbarUtils.displayLongDismissibleSnackbar(root, getString(R.string.posting_to_voat));
        }*/
    }

    /**
     * Update the view state depending on whether content is being posted or not.
     *
     * @param posting true if posting, otherwise false
     */
    @Override
    public void setPosting(boolean posting) {
        resourceTitle.setEnabled(!posting);
        resourceLink.setEnabled(!posting);
        resourceDescription.setEnabled(!posting);

        createResourceButton.setEnabled(!posting);
        createResourceButton.setText(posting ? R.string.sending : R.string.submit);
    }


    @Override
    public void getTreeNode(TreeNode treeNode) {
/*
        skillTreeSelectAdapter.setSkillTree(skillTreeList);
*/
    }

    @Override
    public void getSkillTreeList(List<BaseListItem> skillTreeList) {

        skillTreeRecycler = (RecyclerView) findViewById(R.id.create_resource_skill_tree_selector);
        skillTreeSelectAdapter = new SkillTreeSelectAdapter(this, skillTreeList);
        skillTreeRecycler.setLayoutManager(new LinearLayoutManager(this));
        skillTreeRecycler.setAdapter(skillTreeSelectAdapter);
    }

    /**
     * Validates the input fields and constructs a request body if they are valid.
     *
     * @return a request body if valid, otherwise null
     */
    private Resource validatePost() {
        Resource resource = null;
        boolean valid = true;

        String title = resourceTitle.getText().toString();
        String url = resourceLink.getText().toString();
        String description = resourceDescription.getText().toString();

        List<BaseListItem> selectedSkillTreeItems = new ArrayList<>();
        for (int i = 0; i < skillTreeSelectAdapter.getSkillTree().size(); i++) {
            if (skillTreeSelectAdapter.getSkillTree().get(i).isSelected()) {
                selectedSkillTreeItems.add(skillTreeSelectAdapter.getSkillTree().get(i));
            }
        }
        if (TextUtils.isEmpty(title)) {
            createResourceTitleWrapper.setError(getString(R.string.empty_field
            ));
            valid = false;
        } else if (title.length() < 5) {
            createResourceTitleWrapper.setError(getString(R.string.title_too_short));
            valid = false;
        }
        if (selectedSkillTreeItems.size() < 1) {
            Toast.makeText(this, "Please choose at least one Category for the new Resource", Toast.LENGTH_LONG).show();
            valid = false;
        }
        url = (url.equals("")) ? null : url;
        description = (description.equals("")) ? null : description;

        if (url == null && description == null) {
            createResourceDescriptionWrapper.setError(getString(R.string.empty_field));
            valid = false;
        }
        Timber.d("selectedSkillTreeItems size: " + selectedSkillTreeItems.size());
        if (valid) {
            resource = new Resource();
            resource.setId(ViewUtil.createLongId());
            resource.setName(resourceTitle.getText().toString());
            resource.setDescription(resourceDescription.getText().toString());
            resource.setUrl(resourceLink.getText().toString());
            resource.setLastEditBy(AccountManager.currentUser());
            resource.setLastEditDate(System.currentTimeMillis());
            resource.setSkillTreeIds(selectedSkillTreeItems);

        }
        return resource;
    }

}
