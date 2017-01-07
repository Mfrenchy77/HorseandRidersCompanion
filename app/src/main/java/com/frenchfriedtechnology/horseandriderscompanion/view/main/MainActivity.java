package com.frenchfriedtechnology.horseandriderscompanion.view.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.BaseListItem;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.HorseProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.entity.RiderProfile;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.realm.ListItem;
import com.frenchfriedtechnology.horseandriderscompanion.events.HorseProfileCreateEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.HorseProfileDeleteEvent;
import com.frenchfriedtechnology.horseandriderscompanion.events.ThemeChangedEvent;
import com.frenchfriedtechnology.horseandriderscompanion.view.base.BaseActivity;
import com.frenchfriedtechnology.horseandriderscompanion.view.SettingsActivity;
import com.frenchfriedtechnology.horseandriderscompanion.view.adapters.StringAdapter;
import com.frenchfriedtechnology.horseandriderscompanion.view.dialogs.DialogHorseProfile;
import com.frenchfriedtechnology.horseandriderscompanion.view.horseSkillTree.HorseSkillTreeActivity;
import com.frenchfriedtechnology.horseandriderscompanion.view.riderSkillTree.RiderSkillTreeActivity;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

import static com.frenchfriedtechnology.horseandriderscompanion.view.dialogs.DialogHorseProfile.EDIT_HORSE;
import static com.frenchfriedtechnology.horseandriderscompanion.view.dialogs.DialogHorseProfile.NEW_HORSE;


/**
 * This is the Main Landing page of the Horse and Riders Companion
 * Show a feed a new activity on profile and recommendations to resources
 */
public class MainActivity extends BaseActivity implements MainMvpView, NavigationView.OnNavigationItemSelectedListener {
    // TODO: 22/12/16 this needs to be redesigned to add the suggestions and what's new feed

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.frenchfriedtechnology.horseandriderscompanion.view.main.MainActivity.EXTRA_TRIGGER_SYNC_FLAG";

    private static final String EMAIL = "Email";
    private ViewFlipper viewFlipperHorse;
    private ListView horseList;

    private StringAdapter adapter;

    //----Navigation Menu
    private static final int VIEW_EMPTY = 0;
    private static final int VIEW_CONTENT = 1;
    private static final String KEY_MENU_STATE = "KEY_MENU_STATE";

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private boolean showAccountOptions;
    private ImageView expandChevron;
    private NavigationView navigationView;
    private RiderProfile userRiderProfile = new RiderProfile();
    private List<HorseProfile> horseProfiles = new ArrayList<>();

    @Inject
    MainPresenter mPresenter;


    @Override
    protected int getResourceLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);

        mPresenter.attachView(this);
        mPresenter.getRiderProfile(getIntent().getStringExtra(EMAIL));

        horseList = (ListView) findViewById(R.id.horses_list);
        horseList.setOnItemClickListener((adapterView, view, i, l) -> onHorseSelected(i));
        horseList.setOnItemLongClickListener((adapterView, view, i, l) -> onHorseLongClicked(i));
        TextView emptyList = (TextView) findViewById(R.id.empty_horse_list);
        emptyList.setOnClickListener(view -> emptyHorseListClicked());
        AppCompatButton skillTreeButton = (AppCompatButton) findViewById(R.id.button_skill_tree);
        skillTreeButton.setOnClickListener(view -> openSkillTree());
        AppCompatButton addHorseButton = (AppCompatButton) findViewById(R.id.add_horse_button);
        addHorseButton.setOnClickListener(view -> addHorseClicked());
        viewFlipperHorse = (ViewFlipper) findViewById(R.id.owned_horses_view_flipper);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hnr);
        setSupportActionBar(toolbar);
        setupToolbar(toolbar);

        setUpNavigationDrawer();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.addAuthListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.removeAuthListener();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.removeAuthListener();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(KEY_MENU_STATE, showAccountOptions);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @SuppressWarnings("Convert2streamapi")
    @Override
    public void getUserProfile(RiderProfile riderProfile) {
        List<String> horseIds = new ArrayList<>();
        for (BaseListItem ownedHorse : riderProfile.getOwnedHorses()) {
            horseIds.add(ownedHorse.getId());
        }
        this.userRiderProfile = riderProfile;
        toolbar.setTitle(riderProfile.getName());
        if (riderProfile.getOwnedHorses() != null) {
            mPresenter.getHorseProfiles(horseIds);
        }
        Timber.d("Profile gotten for: " + userRiderProfile.getName());
    }


    // TODO: 30/12/16 move the adapter initilize out of here and uses getProfile for setup
    @SuppressWarnings("Convert2streamapi")
    @Override
    public void getHorseProfiles(List<HorseProfile> horseProfiles) {
        this.horseProfiles = horseProfiles;
        List<String> horseNames = new ArrayList<>();
        for (HorseProfile horseProfile : horseProfiles) {
            if (!horseNames.contains(horseProfile.getName())) {
                horseNames.add(horseProfile.getName());
            }
        }
        adapter = new StringAdapter(this, horseNames);
        horseList.setAdapter(adapter);
        horseList.setDivider(null);
        Collections.sort(horseNames);
        adapter.notifyDataSetChanged();
        viewFlipperHorse.setDisplayedChild(horseNames.isEmpty() ? VIEW_EMPTY : VIEW_CONTENT);
        if (horseProfiles.size() != 0) {
            Timber.d("Horse Profiles size: " + horseProfiles.size());
        }
    }

    /**
     * Opens the Horse Skill Tree
     */
    public void onHorseSelected(int position) {
        String horse = adapter.getItem(position);
        String horseId = getHorseIdFromName(horse);
        if (horseId != null) {
            HorseSkillTreeActivity.start(this, horseId);
        } else {
            Timber.d("Horse Id is Null");
        }
        showToast("Open Horse Skill Tree for " + horse);
    }

    private String getHorseIdFromName(String horse) {
        String horseId = null;
        for (int i = 0; i < horseProfiles.size(); i++) {
            if (horseProfiles.get(i).getName().equals(horse)) {
                horseId = horseProfiles.get(i).getId();
                break;
            }
        }
        return horseId;
    }

    public boolean onHorseLongClicked(int position) {
        for (int i = 0; i < horseProfiles.size(); i++) {
            if (horseProfiles.get(i).getName().equals(adapter.getItem(position))){
                DialogHorseProfile.newInstance(EDIT_HORSE, horseProfiles.get(i)).show(getFragmentManager(), null);
            }
        }
        horseList.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        showToast("Long Clicked: " + adapter.getItem(position));
        return true;
    }

    void emptyHorseListClicked() {
        DialogHorseProfile.newInstance(NEW_HORSE, null).show(getFragmentManager(), null);

    }


    /**
     * Open SkillTree Activity
     */
    public void openSkillTree() {
        if (userRiderProfile != null) {
            RiderSkillTreeActivity.start(this, userRiderProfile.getEmail());
        }
    }

    public void addHorseClicked() {
        //Dialog form to create a horse profile
        DialogHorseProfile.newInstance(NEW_HORSE, null).show(getFragmentManager(), null);
    }

    @Subscribe
    public void onCreateOrEditHorseProfile(HorseProfileCreateEvent event) {
        mPresenter.createOrUpdateHorseProfile(event.getHorseProfile());
        showToast("Horse: " + event.getHorseProfile().getName());
    }

    @Subscribe
    public void onDeleteHorseEvent(HorseProfileDeleteEvent event) {
        mPresenter.deleteHorseProfile(event.getId());
        showToast("Delete Horse: " + event.getId());
    }

    @Subscribe
    public void onThemeChangedEvent(ThemeChangedEvent event) {
        Timber.d("Theme Changed");
        recreate();
    }

    public static void start(Context context, String email, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        intent.putExtra(EMAIL, email);
        context.startActivity(intent);
    }

    //----Navigation Menu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void setUpNavigationDrawer() {

        navigationView = (NavigationView) findViewById(R.id.navigation);
        View view = navigationView.inflateHeaderView(R.layout.drawer_header);
        navigationView.setNavigationItemSelectedListener(this);
        expandChevron = (ImageView) view.findViewById(R.id.expand_menu_chevron);

        TextView textUser = (TextView) view.findViewById(R.id.profile_username);
        textUser.setText(userRiderProfile.getName());
        LinearLayout accountNameLayout = (LinearLayout) view.findViewById(R.id.account_name_layout);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                null, R.string.nav_open, R.string.nav_closed) {

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // disable animation
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        navigationView.setNavigationItemSelectedListener(this);

        accountNameLayout.setOnClickListener(v -> {
            showAccountOptions = !showAccountOptions;
            updateNavigationMenu();
        });
        updateNavigationMenu();
    }

    public void closeDrawerIfOpen() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    /**
     * Handles toggle of the navigation chevron, which determines whether account options or the
     * regular menu should be shown.
     */
    private void updateNavigationMenu() {
        expandChevron.setImageResource(showAccountOptions ?
                R.drawable.vector_expand_less : R.drawable.vector_expand_more);

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.menu_heading_app).setVisible(!showAccountOptions);
        menu.findItem(R.id.menu_heading_horse_and_rider).setVisible(!showAccountOptions);
        menu.findItem(R.id.menu_heading_account).setVisible(showAccountOptions);
    }

    /**
     * Navigation Drawer click handling
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.action_menu_item_switch_account:
                // logout user and open switch account dialog
                Toast.makeText(this, "Switch Account", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_menu_item_sign_out:
                //clear auth and go to login screen
                mPresenter.logoutUser();
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_menu_item_profile:
                //open edit profile dialog
                Toast.makeText(this, "Edit Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_menu_glossary:
                //open dialog to glossary
                Toast.makeText(this, "Open Glossary", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_menu_item_settings:
                //open settings activity
                SettingsActivity.start(this);
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_menu_item_support:
                //send email
                Toast.makeText(this, "Email Horse and Riders Companion", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_menu_item_rate:
                //rate app
                Toast.makeText(this, "goto play store page", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;

        }
        closeDrawerIfOpen();
        return true;
    }

}
