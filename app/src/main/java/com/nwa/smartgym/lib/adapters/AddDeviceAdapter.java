package com.nwa.smartgym.lib.adapters;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nwa.smartgym.R;
import com.nwa.smartgym.models.Device;

import java.util.List;

/**
 * Created by robin on 29-5-16.
 */
public class AddDeviceAdapter extends ArrayAdapter<Device> {
    private BluetoothAdapter bluetoothAdapter;

    public AddDeviceAdapter(Context context, List<Device> objects, BluetoothAdapter bluetoothAdapter) {
        super(context, R.layout.device_list_item, objects);
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public View getView(int position, View view, ViewGroup parent) {
        Device device = (Device) getItem(position);

        LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View listItem = layoutInflater.inflate(R.layout.device_list_item, null);
        TextView title = (TextView) listItem.findViewById(R.id.title_device_list_item);
        TextView subTitle = (TextView) listItem.findViewById(R.id.subtitle_device_list_item);

        title.setText(device.getName());
        subTitle.setText(device.getDeviceAddress());

        return listItem;
    }
}
