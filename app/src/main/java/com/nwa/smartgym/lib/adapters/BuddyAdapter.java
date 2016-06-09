package com.nwa.smartgym.lib.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OrmLiteCursorAdapter;
import com.nwa.smartgym.R;
import com.nwa.smartgym.api.interfaces.BuddyAPIInterface;
import com.nwa.smartgym.models.Buddy;

/**
 * Created by robin on 8-6-16.
 */
public class BuddyAdapter extends OrmLiteCursorAdapter<Buddy, RelativeLayout> {
    private BuddyAPIInterface buddyAPIInterface;

    public BuddyAdapter(Context context, BuddyAPIInterface buddyAPIInterface) {
        super(context);
        this.buddyAPIInterface = buddyAPIInterface;
    }


    @Override
    public void bindView(RelativeLayout relativeLayout, Context context, final Buddy buddy) {
        TextView name = (TextView) relativeLayout.findViewById(R.id.name_buddy_list_item);
        ImageView delete = (ImageView) relativeLayout.findViewById(R.id.delete_buddy_list_item);

        name.setText(buddy.getFullName());
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return View.inflate(context, R.layout.buddy_list_item, null);
    }
}
