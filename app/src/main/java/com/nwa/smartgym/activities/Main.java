package com.nwa.smartgym.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nwa.smartgym.R;

import java.util.ArrayList;
import java.util.List;
import com.nwa.smartgym.fragments.main.BusynessFragment;

import com.nwa.smartgym.api.interfaces.AuthAPIInterface;
import  com.nwa.smartgym.lib.DefaultPageAdapter;
import  com.nwa.smartgym.lib.NonSwipeableViewPager;
import com.nwa.smartgym.lib.SecretsHelper;
import com.nwa.smartgym.lib.adapters.DrawerAdapter;
import com.nwa.smartgym.models.*;

public class Main extends AppCompatActivity {

    private static NonSwipeableViewPager mViewPager;
    private DefaultPageAdapter mPageAdapter;
    private ListView menuDrawerList;

    private static Context context;

    // UI references.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkAuthCookieExists();

        setContentView(R.layout.activity_main);
        List<Fragment> fragments = listFragments();
        mPageAdapter = new DefaultPageAdapter(getSupportFragmentManager(), fragments);
        Main.context = getApplicationContext();

        mViewPager = (NonSwipeableViewPager) findViewById(R.id.main_container);
        mViewPager.setAdapter(mPageAdapter);

        setupDrawer();
    }

    private List<Fragment> listFragments() {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(BusynessFragment.newInstance());
        return fragmentList;
    }

    private void setupDrawer() {
        menuDrawerList = (ListView) findViewById(R.id.menu_drawer_list);
        DrawerAdapter drawerAdapter = new DrawerAdapter(this, getDrawerItems());
        menuDrawerList.setAdapter(drawerAdapter);
    }

    private List<DrawerItem> getDrawerItems() {
        List<DrawerItem> drawerItems = new ArrayList<>();
        drawerItems.add(new DrawerItem(
                getResources().getDrawable(R.drawable.bluetooth),
                getString(R.string.action_persist_device),
                new Intent(this, Devices.class)));

        drawerItems.add(new DrawerItem(
                getResources().getDrawable(R.drawable.music),
                getString(R.string.action_music_preference),
                new Intent(this, MusicPreference.class)));

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
        drawerItems.add(getLoginDrawerItem());


        return drawerItems;
    }

    private DrawerItem getLoginDrawerItem() {
       DrawerItem loginDrawerItem = new DrawerItem(
                getResources().getDrawable(R.drawable.logout),
                getString(R.string.logout)) {
            @Override
            public void executeDrawerAction(Context context) {
                AuthAPIInterface authAPIInterface = new AuthAPIInterface(context);
                authAPIInterface.logout();
            }
        };
        return loginDrawerItem;
    }

    private void checkAuthCookieExists() {
        SecretsHelper secretsHelper = new SecretsHelper(this);
        if (secretsHelper.getAuthToken() == null) {
            Intent intent = new Intent(this, Welcome.class);
            startActivity(intent);
            finish(); // Finish to prevent the user simply backing into this activity again.
        }
    }
}