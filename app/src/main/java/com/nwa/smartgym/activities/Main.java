package com.nwa.smartgym.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.nwa.smartgym.R;
import com.nwa.smartgym.api.interfaces.AuthAPIInterface;
import com.nwa.smartgym.fragments.main.BusynessFragment;
import com.nwa.smartgym.fragments.main.CardioActivity;
import com.nwa.smartgym.lib.DefaultPageAdapter;
import com.nwa.smartgym.lib.NonSwipeableViewPager;
import com.nwa.smartgym.lib.SecretsHelper;
import com.nwa.smartgym.lib.adapters.DrawerAdapter;
import com.nwa.smartgym.models.DrawerItem;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity {

    private static Context context;

    // UI references.
    ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkAuthCookieExists();

        setContentView(R.layout.activity_main);
        List<Fragment> fragments = listFragments();
        DefaultPageAdapter mPageAdapter = new DefaultPageAdapter(getSupportFragmentManager(), fragments);
        Main.context = getApplicationContext();

        NonSwipeableViewPager mViewPager = (NonSwipeableViewPager) findViewById(R.id.main_container);
        if (mViewPager != null) {
            mViewPager.setAdapter(mPageAdapter);
        }

        setupDrawer();
    }

    private List<Fragment> listFragments() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(BusynessFragment.newInstance());
        fragmentList.add(CardioActivity.newInstance());
        return fragmentList;
    }

    private void setupDrawer() {
        ListView menuDrawerList = (ListView) findViewById(R.id.menu_drawer_list);
        DrawerAdapter drawerAdapter = new DrawerAdapter(this, getDrawerItems());
        if (menuDrawerList != null) {
            menuDrawerList.setAdapter(drawerAdapter);
        }

        // Make sure we got a drawer toggle for usability purposes.
        drawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.app_name, R.string.app_name);

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
    }

    private List<DrawerItem> getDrawerItems() {
        List<DrawerItem> drawerItems = new ArrayList<>();
        drawerItems.add(new DrawerItem(
                getResources().getDrawable(R.drawable.bluetooth),
                getString(R.string.action_persist_device),
                new Intent(this, Devices.class)));

        drawerItems.add(new DrawerItem(
                getResources().getDrawable(R.drawable.ic_date_range_black_24dp),
                getString(R.string.action_sport_schedule),
                new Intent(this, SportSchedule.class)
        ));
        drawerItems.add(new DrawerItem(
                getResources().getDrawable(R.drawable.buddies),
                getString(R.string.buddies),
                new Intent(this, Buddies.class)
        ));

        drawerItems.add(new DrawerItem(
                getResources().getDrawable(R.mipmap.ic_smartgym),
                getString(R.string.activity_activities_name),
                new Intent(this, SportActivity.class)
        ));

        // Logout should, logically, be the bottom item
        drawerItems.add(getLoginDrawerItem());

        return drawerItems;
    }

    private DrawerItem getLoginDrawerItem() {
        return new DrawerItem(
                 getResources().getDrawable(R.drawable.logout),
                 getString(R.string.logout)) {
             @Override
             public void executeDrawerAction(Context context1) {
                 AuthAPIInterface authAPIInterface = new AuthAPIInterface(context1);
                 authAPIInterface.logout();
             }
         };
    }

    private void checkAuthCookieExists() {
        SecretsHelper secretsHelper = new SecretsHelper(this);
        if (secretsHelper.getAuthToken() == null) {
            Intent intent = new Intent(this, Welcome.class);
            startActivity(intent);
            finish(); // Finish to prevent the user simply backing into this activity again.
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Activate the navigation drawer toggle
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}