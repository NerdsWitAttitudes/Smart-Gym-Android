package com.nwa.smartgym.lib;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by robin on 2-5-16.
 */
public class DefaultPageAdapter extends FragmentPagerAdapter {
    public static final String TAB_NAME = "tab_name";

    private List<Fragment> fragments;

    public DefaultPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Bundle arguments = fragments.get(position).getArguments();
        String string = arguments.getString(TAB_NAME);
        return string;
    }
}
