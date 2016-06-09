package com.nwa.smartgym.lib.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.nwa.smartgym.R;
import com.nwa.smartgym.api.interfaces.BuddyAPIInterface;
import com.nwa.smartgym.lib.DatabaseHelper;
import com.nwa.smartgym.lib.ErrorHelper;
import com.nwa.smartgym.models.User;

import java.sql.SQLException;
import java.util.List;


/**
 * Created by robin on 9-6-16.
 */
public class UserAdapter extends ArrayAdapter {
    private BuddyAPIInterface buddyAPIInterface;
    private OrmLiteBaseListActivity<DatabaseHelper> ormLiteBaseListActivity;

    public UserAdapter(OrmLiteBaseListActivity<DatabaseHelper> ormLiteBaseListActivity, List<User> users) {
        super(ormLiteBaseListActivity, 0, users);

        this.ormLiteBaseListActivity = ormLiteBaseListActivity;
        setBuddyAPIInterface();
    }

    public UserAdapter(OrmLiteBaseListActivity<DatabaseHelper> ormLiteBaseListActivity) {
        super(ormLiteBaseListActivity, 0);

        this.ormLiteBaseListActivity = ormLiteBaseListActivity;
        setBuddyAPIInterface();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User user = (User) getItem(position);

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.user_list_item, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name_user_list_item);
        final ImageView addBuddy = (ImageView) convertView.findViewById(R.id.add_buddy_list_item);

        name.setText(user.getFullName());

        addBuddy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buddyAPIInterface == null) {
                    // If there's no interface we can not put to the API. Retry setting the
                    // interface and if that fails we show a generic error to the user.
                    setBuddyAPIInterface();

                    if (buddyAPIInterface == null) {
                        ErrorHelper.raiseGenericError(ormLiteBaseListActivity);
                        return;
                    }
                }

                buddyAPIInterface.put(user.getId());

                // Make it visually obvious that the user is added as a buddy and prevent the user
                // from making the request again.
                addBuddy.setImageDrawable(getContext().getResources().getDrawable(R.drawable.checkmark));

                addBuddy.setOnClickListener(null);
            }
        });

        return convertView;
    }

    private void setBuddyAPIInterface() {
        try {
            buddyAPIInterface = new BuddyAPIInterface(
                    ormLiteBaseListActivity,
                    ormLiteBaseListActivity.getHelper().getBuddyDao());
        } catch (SQLException e) {
            Log.e(getClass().getName(), "Unable to access database");
        }
    }
}
