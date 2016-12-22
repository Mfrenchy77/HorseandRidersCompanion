package com.frenchfriedtechnology.horseandriderscompanion.view.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.frenchfriedtechnology.horseandriderscompanion.BusProvider;
import com.frenchfriedtechnology.horseandriderscompanion.HorseAndRidersCompanion;
import com.frenchfriedtechnology.horseandriderscompanion.R;
import com.frenchfriedtechnology.horseandriderscompanion.data.local.UserPrefs;
import com.frenchfriedtechnology.horseandriderscompanion.di.component.ActivityComponent;
import com.frenchfriedtechnology.horseandriderscompanion.di.component.ConfigPersistentComponent;
import com.frenchfriedtechnology.horseandriderscompanion.di.component.DaggerConfigPersistentComponent;
import com.frenchfriedtechnology.horseandriderscompanion.di.module.ActivityModule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import timber.log.Timber;

/**
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private ActivityComponent mComponent;
    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new HashMap<>();

    private long mActivityId;
    protected Context context = this;
    protected Toolbar toolbar;
    protected LayoutInflater layoutInflater;
    protected ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Sets the Global Theme: Night Mode, Day Mode, Auto Day/Night

        AppCompatDelegate.setDefaultNightMode(new UserPrefs().isDayNightMode() ?
                AppCompatDelegate.MODE_NIGHT_AUTO : new UserPrefs().isNightMode() ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);


        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mActivityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent;
        if (!sComponentsMap.containsKey(mActivityId)) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(HorseAndRidersCompanion.get(this).getComponent())
                    .build();
            sComponentsMap.put(mActivityId, configPersistentComponent);
        } else {
            Timber.i("Reusing ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = sComponentsMap.get(mActivityId);
        }
        mComponent = configPersistentComponent.activityComponent(new ActivityModule(this));

        setContentView(getResourceLayout());
        Timber.tag(getClass().getSimpleName());
        layoutInflater = LayoutInflater.from(context);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hnr);
        setupToolbar(toolbar);
        setSupportActionBar(toolbar);
        BusProvider.getBusProviderInstance().register(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, mActivityId);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", mActivityId);
            sComponentsMap.remove(mActivityId);
        }
        BusProvider.getBusProviderInstance().unregister(this);
        super.onDestroy();
    }


    public FragmentManager getBaseFragmentManager() {
        return super.getSupportFragmentManager();
    }

    protected void setupToolbar(final Toolbar toolbar) {
        setupToolbar(toolbar, null);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void setupToolbar(final Toolbar toolbar, final View.OnClickListener onClickListener) {

        this.toolbar = toolbar;
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        if (onClickListener != null) {
            toolbar.setNavigationOnClickListener(onClickListener);
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setTitle(int title) {
        super.setTitle(title);
        if (actionBar != null) {
            actionBar.setTitle(getString(title));

        }
    }

    public ActionBar getBaseActionBar() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        return actionBar;
    }

    @Override
    public void onBackPressed() {
        if (getBaseFragmentManager().getBackStackEntryCount() > 0) {
            getBaseFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    protected ActivityComponent activityComponent() {
        return mComponent;
    }

    protected void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    protected abstract int getResourceLayout();
}
