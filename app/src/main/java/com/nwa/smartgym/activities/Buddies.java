package com.nwa.smartgym.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.dao.Dao;
import com.nwa.smartgym.R;
import com.nwa.smartgym.api.interfaces.BuddyAPIInterface;
import com.nwa.smartgym.lib.DatabaseHelper;
import com.nwa.smartgym.models.Buddy;

import java.sql.SQLException;
import java.util.UUID;

public class Buddies extends ListFragment {
    private Dao<Buddy, UUID> buddyDao;
    private BuddyAPIInterface buddyAPIInterface;

    public Buddies() {
        // Required empty public constructor
    }

    public static Buddies newInstance() {
        Buddies fragment = new Buddies();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
            buddyDao = databaseHelper.getBuddyDao();
        } catch( SQLException e) {
            Log.e(this.getClass().getName(), "Unable to access database", e);
        }

        buddyAPIInterface = new BuddyAPIInterface(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buddies, container, false);
    }
}
