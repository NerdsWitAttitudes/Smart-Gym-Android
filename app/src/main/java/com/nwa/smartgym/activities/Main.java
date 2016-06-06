package com.nwa.smartgym.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.nwa.smartgym.R;
import com.nwa.smartgym.lib.DefaultPageAdapter;
import com.nwa.smartgym.lib.NonSwipeableViewPager;
import com.nwa.smartgym.lib.adapters.DrawerAdapter;
import com.nwa.smartgym.models.DrawerItem;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity {

    private static NonSwipeableViewPager mViewPager;
    private DefaultPageAdapter mPageAdapter;
    private ListView menuDrawerList;

    private static Context context;

    // UI references.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                getResources().getDrawable(R.drawable.ic_date_range_black_24dp),
                getString(R.string.action_sport_schedule),
                new Intent(this, SportSchedule.class)
        ));

        return drawerItems;
    }
}