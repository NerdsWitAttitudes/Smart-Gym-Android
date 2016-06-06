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
import java.util.UUID;

import  com.nwa.smartgym.lib.DefaultPageAdapter;
import  com.nwa.smartgym.lib.NonSwipeableViewPager;
import com.nwa.smartgym.lib.NotificationService;
import com.nwa.smartgym.lib.adapters.DrawerAdapter;
import com.nwa.smartgym.models.DrawerItem;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

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

        List<LocalDate.Property> properties = new ArrayList<>();
        properties.add(new LocalDate().withDayOfWeek(1).dayOfWeek());
        properties.add(new LocalDate().withDayOfWeek(2).dayOfWeek());
        com.nwa.smartgym.models.SportSchedule hardgaan = new com.nwa.smartgym.models.SportSchedule(UUID.randomUUID(), UUID.randomUUID(), "Hardgaan", 0, LocalTime.now().plusSeconds(5), properties, true);
        NotificationService notificationService = new NotificationService(this);
        notificationService.scheduleNotifications(hardgaan);
        notificationService.cancelScheduledNotifications();
        hardgaan.setTime(LocalTime.now().plusSeconds(120));
        notificationService.scheduleNotifications(hardgaan);
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
                "Manage Sport Schedules",
                new Intent(this, SportSchedule.class)
        ));

        return drawerItems;
    }
}