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
import com.nwa.smartgym.api.interfaces.DeviceAPIInterface;
import com.nwa.smartgym.models.Device;

/**
 * Created by robin on 30-5-16.
 */
public class DeviceAdapter extends OrmLiteCursorAdapter<Device, RelativeLayout> {
    private DeviceAPIInterface deviceAPIInterface;

    public DeviceAdapter(Context context, DeviceAPIInterface deviceAPIInterface) {
        super(context);
        this.deviceAPIInterface = deviceAPIInterface;
    }

    @Override
    public void bindView(RelativeLayout relativeLayout, Context context, final Device device) {
        TextView title = (TextView) relativeLayout.findViewById(R.id.title_device_list_item);
        TextView subTitle = (TextView) relativeLayout.findViewById(R.id.subtitle_device_list_item);
        ImageView delete = (ImageView) relativeLayout.findViewById(R.id.delete_device_list_item);

        title.setText(device.getName());
        subTitle.setText(device.getDeviceAddress());
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceAPIInterface.delete(device);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return View.inflate(context, R.layout.device_list_item, null);
    }
}
