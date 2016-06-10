package com.nwa.smartgym.activities;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.dao.Dao;
import com.nwa.smartgym.R;
import com.nwa.smartgym.api.interfaces.BuddyAPIInterface;
import com.nwa.smartgym.api.interfaces.UserAPIInterface;
import com.nwa.smartgym.lib.DatabaseHelper;
import com.nwa.smartgym.lib.adapters.UserAdapter;
import com.nwa.smartgym.models.Buddy;
import com.nwa.smartgym.models.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListUsersActivity extends OrmLiteBaseListActivity<DatabaseHelper> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        UserAdapter userAdapter = new UserAdapter(this);
        UserAPIInterface userAPIInterface = new UserAPIInterface(this, userAdapter);

        //get recommended buddies first to show in the top list
        userAPIInterface.listRecommendedBuddies();

        setListAdapter(userAdapter);
    }
}
