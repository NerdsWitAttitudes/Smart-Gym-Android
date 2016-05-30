package com.nwa.smartgym.lib.adapters;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nwa.smartgym.R;
import com.nwa.smartgym.models.Device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by robin on 29-5-16.
 */
public class AddDeviceAdapter extends ArrayAdapter<Device> {
    private Map<String, Device> devicesToBePersisted;

    public AddDeviceAdapter(Context context, List<Device> objects) {
        super(context, R.layout.add_device_list_item, objects);
        this.devicesToBePersisted = new HashMap<>();
    }

    public View getView(int position, View view, ViewGroup parent) {
        final Device device = (Device) getItem(position);

        LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View listItem = layoutInflater.inflate(R.layout.add_device_list_item, null);
        TextView title = (TextView) listItem.findViewById(R.id.title_device_list_item);
        TextView subTitle = (TextView) listItem.findViewById(R.id.subtitle_device_list_item);
        CheckBox checkbox = (CheckBox) listItem.findViewById(R.id.add_device_checkbox);

        title.setText(device.getName());
        subTitle.setText(device.getDeviceAddress());

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    devicesToBePersisted.put(device.getDeviceAddress(), device);
                } else {
                    devicesToBePersisted.remove(device.getDeviceAddress());
                }
            }
        });

        return listItem;
    }

    public Map<String, Device> getDevicesToBePersisted() {
        return this.devicesToBePersisted;
    }
}
