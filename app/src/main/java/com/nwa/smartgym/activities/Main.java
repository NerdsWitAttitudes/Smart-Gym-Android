package com.nwa.smartgym.activities;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.nwa.smartgym.R;

import java.util.ArrayList;
import java.util.List;
import com.nwa.smartgym.fragments.main.BusynessFragment;
import  com.nwa.smartgym.lib.DefaultPageAdapter;
import  com.nwa.smartgym.lib.NonSwipeableViewPager;

public class Main extends FragmentActivity {

    private static NonSwipeableViewPager mViewPager;
    private DefaultPageAdapter mPageAdapter;

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
    }

    private List<Fragment> listFragments() {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(BusynessFragment.newInstance());
        return fragmentList;
    }
}
