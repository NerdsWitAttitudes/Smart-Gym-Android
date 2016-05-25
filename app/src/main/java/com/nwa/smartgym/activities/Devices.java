package com.nwa.smartgym.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.nwa.smartgym.R;
import com.nwa.smartgym.api.DeviceAPI;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.lib.SecretsHelper;
import com.nwa.smartgym.models.Device;
import com.nwa.smartgym.models.HTTPResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Devices extends ListActivity{

    private ArrayAdapter<Device> viewAdapter;
    private List<Device> devices;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        DeviceListTask deviceListTask = new DeviceListTask();
        deviceListTask.execute((Void) null);

        viewAdapter = new ArrayAdapter<>(this, R.layout.device_list_item, devices);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_device);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        });
    }

    public class DeviceListTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        public Boolean doInBackground(Void... Params) {
            SecretsHelper secretsHelper = new SecretsHelper(getApplicationContext());
            DeviceAPI deviceService = ServiceGenerator.createSmartGymService(DeviceAPI.class,
                    secretsHelper.getAuthToken());
            Call<List<Device>> call = deviceService.listDevices();
            call.enqueue(new Callback<List<Device>>() {

                @Override
                public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
                    System.out.println(response.code());
                    if (response.code() == 200) {
                        System.out.println("stupidshit");
                        devices.addAll(response.body());
                        System.out.println(devices.size());
                    } else if (response.code() == 400) {
                        System.out.println("this happened");
                        return;
                    } else {
                        System.out.println("generic");
                        System.out.println(response.code());
                        raiseGenericError();
                    }
                }

                @Override
                public void onFailure(Call<List<Device>> call, Throwable t) {
                    System.out.println("sowrong");
                    System.out.println(t);
                    raiseGenericError();
                }

                private void raiseGenericError() {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.server_500_message),
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

            return true;
        }

    }
}
