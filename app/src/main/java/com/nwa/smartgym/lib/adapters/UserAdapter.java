package com.nwa.smartgym.lib.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nwa.smartgym.R;
import com.nwa.smartgym.models.User;

import java.util.List;


/**
 * Created by robin on 9-6-16.
 */
public class UserAdapter extends ArrayAdapter {
    public UserAdapter(Context context, List<User> users) {
        super(context, 0, users);
    }

    public UserAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = (User) getItem(position);

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.user_list_item, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name_user_list_item);
        name.setText(user.getFullName());

        return convertView;
    }
}
