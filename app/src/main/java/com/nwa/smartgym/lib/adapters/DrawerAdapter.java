package com.nwa.smartgym.lib.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nwa.smartgym.R;
import com.nwa.smartgym.models.DrawerItem;

import java.util.List;

/**
 * Created by robin on 30-5-16.
 */
public class DrawerAdapter extends ArrayAdapter<DrawerItem> {

    public DrawerAdapter(Context context, List<DrawerItem> objects) {
        super(context, R.layout.add_device_list_item, objects);
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final DrawerItem drawerItem = (DrawerItem) getItem(position);

        View listItem = inflateDrawerItem(drawerItem);

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItem(position).executeDrawerAction(getContext());
            }
        });

        return listItem;
    }

    public View inflateDrawerItem(DrawerItem drawerItem) {
        LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View listItem = layoutInflater.inflate(R.layout.drawer_list_item, null);

        TextView name = (TextView) listItem.findViewById(R.id.menu_drawer_name);
        ImageView image = (ImageView) listItem.findViewById(R.id.menu_drawer_icon);

        name.setText(drawerItem.getName());
        image.setImageDrawable(drawerItem.getIcon());

        return listItem;
    }
}
