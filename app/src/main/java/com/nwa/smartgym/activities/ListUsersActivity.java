package com.nwa.smartgym.activities;

import android.os.Bundle;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.nwa.smartgym.R;
import com.nwa.smartgym.api.interfaces.UserAPIInterface;
import com.nwa.smartgym.lib.DatabaseHelper;
import com.nwa.smartgym.lib.adapters.UserAdapter;

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
