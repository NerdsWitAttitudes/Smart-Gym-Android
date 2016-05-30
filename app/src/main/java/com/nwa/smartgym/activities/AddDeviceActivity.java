package com.nwa.smartgym.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.dao.Dao;
import com.nwa.smartgym.R;
import com.nwa.smartgym.api.DeviceAPIInterface;
import com.nwa.smartgym.lib.DatabaseHelper;
import com.nwa.smartgym.lib.adapters.AddDeviceAdapter;
import com.nwa.smartgym.models.Device;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AddDeviceActivity extends OrmLiteBaseListActivity<DatabaseHelper> {

    private static final int REQUEST_BLUETOOTH = 1;

    private Dao<Device, Integer> deviceDao;
    private DeviceAPIInterface deviceAPIInterface;

    private BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter broadcastFilter;

    private List<Device> deviceList;
    private AddDeviceAdapter deviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        ListView listView = (ListView) findViewById(android.R.id.list);

        try {
            deviceDao = getHelper().getDeviceDao();
        } catch( SQLException e) {
            Log.e(this.getLocalClassName(), "Unable to access database", e);
        }
        deviceAPIInterface = new DeviceAPIInterface(this, deviceDao);
        bluetoothAdapter = getBluetoothAdapter();

        findDevices();

        Button createButton = (Button) findViewById(R.id.persist_devices_button);
        deviceAdapter = new AddDeviceAdapter(this, deviceList, createButton);
        assignButton(createButton);

        listView.setAdapter(deviceAdapter);
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
        getCurrentDevice();
        findPairedDevices();
        scanForDevices();
    }

    private void getCurrentDevice() {
        if (deviceAPIInterface.deviceExists(bluetoothAdapter.getAddress())) {
            return;
        }
        Device device = new Device(bluetoothAdapter.getAddress(), bluetoothAdapter.getName());
        deviceList.add(device);
    }

    private void findPairedDevices() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        for (BluetoothDevice bluetoothDevice : pairedDevices) {
            if (deviceAPIInterface.deviceExists(bluetoothDevice.getAddress())) {
                continue;
            }
            Device device = new Device(
                    bluetoothDevice.getAddress(),
                    bluetoothDevice.getName(),
                    bluetoothDevice.hashCode());
            deviceList.add(device);
        }
    }

    private void scanForDevices() {
        final List<String> foundAddresses = new ArrayList<>();
        broadcastFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                    BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (deviceAPIInterface.deviceExists(bluetoothDevice.getAddress())) {
                        return;
                    }
                    Device device = new Device(
                            bluetoothDevice.getAddress(),
                            bluetoothDevice.getName(),
                            bluetoothDevice.getBluetoothClass().hashCode());
                    if (foundAddresses.contains(device.getDeviceAddress())) {
                        // devices sometimes show up as duplicate during scanning.
                        return;
                    }
                    foundAddresses.add(device.getDeviceAddress());
                    deviceAdapter.add(device);
                    deviceAdapter.notifyDataSetChanged();
                }
            }
        };

        this.registerReceiver(broadcastReceiver, broadcastFilter);
        bluetoothAdapter.startDiscovery();
    }

    private void assignButton(Button button) {
        final Map<String, Device> devicesToBePersisted = deviceAdapter.getDevicesToBePersisted();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Map.Entry<String, Device> deviceEntry : devicesToBePersisted.entrySet()) {
                    deviceAPIInterface.persist(deviceEntry.getValue());
                }
                finish();
            }
        });
    }
}
