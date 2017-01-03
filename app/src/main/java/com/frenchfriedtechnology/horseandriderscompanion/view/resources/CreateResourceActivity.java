package com.frenchfriedtechnology.horseandriderscompanion.view.resources;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.TextView;

import com.frenchfriedtechnology.horseandriderscompanion.AccountManager;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.Resource;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import timber.log.Timber;

/**
 * Activity for Creating a resource and allows share intercepts from sources outside of application
 */

public class CreateResourceActivity extends BaseActivity {
    @Bind(R.id.post_content_title)
    TextView contentTitle;
    @Bind(R.id.post_content_subverse)
    TextView contentSubverse;
    @Bind(R.id.post_content_link)
    TextView contentLink;
    @Bind(R.id.post_content_message)
    TextView contentMessage;
    @Bind(R.id.post_content_title_wrapper)
    TextInputLayout contentTitleWrapper;
    @Bind(R.id.post_content_subverse_wrapper)
    TextInputLayout contentSubverseWrapper;
    @Bind(R.id.post_content_message_wrapper)
    TextInputLayout contentMessageWrapper;

    @Bind(R.id.post_content_button)
    Button contentButton;
    @Bind(R.id.post_content_root)
    View root;

    private boolean externalShare;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_create_resource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        externalShare = Intent.ACTION_SEND.equals(intent.getAction());

        if (externalShare) {
            handleExternalShare(intent);
        } else {
            //only external shares
            Timber.d("Intent.ActionSend = Null");
        }

        FragmentManager fm = getSupportFragmentManager();
        PostContentPresenter postContentPresenter = (PostContentPresenter) fm.findFragmentByTag(PRESENTER_TAG);

        if (postContentPresenter == null) {
            postContentPresenter = new PostContentPresenter();
            fm.beginTransaction().add(postContentPresenter, PRESENTER_TAG).commit();
        }
    }


    /**
     * Handles a share intent from a 3rd party app
     *
     * @param intent an intent from a 3rd party
     */
    private void handleExternalShare(Intent intent) {
        if (intent.getType().equals("text/plain")) {
            Bundle extras = intent.getExtras();

            String text = extras.getString(Intent.EXTRA_TEXT);
            String title = extras.getString(Intent.EXTRA_TITLE);
            String subject = extras.getString(Intent.EXTRA_SUBJECT);

            if (subject != null) {
                contentTitle.setText(subject);
            }
            if (title != null) {
                contentTitle.setText(title); // override subject
            }

            if (text != null) {
                boolean isLink = URLUtil.isValidUrl(text);

                if (isLink) {
                    contentLink.setText(text);
                } else {
                    contentMessage.setText(text);
                }
            }
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @OnTextChanged(R.id.post_content_link)
    void onUrlChanged() {
        contentMessage.setVisibility(TextUtils.isEmpty(contentLink.getText()) ? View.VISIBLE : View.GONE);
    }

    @OnTextChanged(R.id.post_content_message)
    void onMessageChanged() {
        contentLink.setVisibility(TextUtils.isEmpty(contentMessage.getText()) ? View.VISIBLE : View.GONE);
        contentMessageWrapper.setError(null);
    }

    @OnTextChanged(R.id.post_content_title)
    void onPostTitleChanged() {
        contentTitleWrapper.setError(null);
    }

    @OnTextChanged(R.id.post_content_subverse)
    void onSubverseChanged() {
        contentSubverseWrapper.setError(null);
    }

    @OnClick(R.id.post_content_button)
    void onSubmitClicked() {
        setPosting();
        Resource resource = new Resource();
        resource.setTitle(contentTitle.getText().toString());
        resource.setDescription(contentMessage.getText().toString());
        resource.setUrl(contentLink.getText().toString());
        resource.setAddedBy(AccountManager.currentUser());
        resource.setAddedDate(System.currentTimeMillis());

        //presenter.submitNewResource(resource)
 /*       ApiBodyUserSubmission body = validatePost();

        if (new AccountManager().isGuest()) { //Check if User is guest to prevent crashes
            SnackbarUtils.displayLongDismissibleSnackbar(root, Voater.getContext().getString(R.string.err_message_security));
            finish();
        } else if (body != null) {
            if (ACTION_CREATE_CONTENT.equals(getIntent().getAction())) {
                BusProvider.Instance.getBus().post(new Content.Ui
                        .ContentPostEvent(body, contentSubverse.getText().toString(), false, id));
                finish();
            } else if (ACTION_EDIT_CONTENT.equals(getIntent().getAction())) {
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
    private void setPosting(boolean posting) {
        contentTitle.setEnabled(!posting);
        contentLink.setEnabled(!posting);
        contentMessage.setEnabled(!posting);

        contentButton.setEnabled(!posting);
        contentButton.setText(posting ? R.string.sending : R.string.submit);
    }

}
