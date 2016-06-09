package com.nwa.smartgym.activities;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.nwa.smartgym.R;
import com.nwa.smartgym.api.interfaces.UserAPIInterface;
import com.nwa.smartgym.lib.adapters.UserAdapter;
import com.nwa.smartgym.models.User;

import java.util.ArrayList;
import java.util.List;

public class ListUsersActivity extends ListActivity {

    private UserAPIInterface userAPIInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        UserAdapter userAdapter = new UserAdapter(this);
        userAPIInterface = new UserAPIInterface(this, userAdapter);
        userAPIInterface.list();

        setListAdapter(userAdapter);
    }
}
