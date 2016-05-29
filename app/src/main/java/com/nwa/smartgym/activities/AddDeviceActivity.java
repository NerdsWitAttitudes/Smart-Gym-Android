package com.nwa.smartgym.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nwa.smartgym.R;
import com.nwa.smartgym.lib.adapters.AddDeviceAdapter;
import com.nwa.smartgym.models.Device;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AddDeviceActivity extends AppCompatActivity {

    private static final int REQUEST_BLUETOOTH = 1;

    private BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter broadcastFilter;

    private List<Device> deviceList;
    private ArrayAdapter<Device> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        ListView listView = (ListView) findViewById(android.R.id.list);

        bluetoothAdapter = getBluetoothAdapter();
        findDevices();
        arrayAdapter = new AddDeviceAdapter(this, deviceList, bluetoothAdapter);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.registerReceiver(broadcastReceiver, broadcastFilter);
        bluetoothAdapter.startDiscovery();
    }

    private BluetoothAdapter getBluetoothAdapter() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // no bluetooth possible
        } else if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_BLUETOOTH);
        }
        return bluetoothAdapter;
    }

    private void findDevices() {
        deviceList = new ArrayList<Device>();
        findPairedDevices();
        scanForDevices();
    }

    private void findPairedDevices() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        for (BluetoothDevice bluetoothDevice : pairedDevices) {
            Device device = new Device(bluetoothDevice.getAddress(), bluetoothDevice.getName());
            deviceList.add(device);
        }
    }

    private void scanForDevices() {
        broadcastFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                    BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Device device = new Device(bluetoothDevice.getAddress(), bluetoothDevice.getName());
                    arrayAdapter.add(device);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        };

        this.registerReceiver(broadcastReceiver, broadcastFilter);
        bluetoothAdapter.startDiscovery();
    }



}
