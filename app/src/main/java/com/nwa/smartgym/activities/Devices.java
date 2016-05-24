package com.nwa.smartgym.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
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

public class Devices extends ListActivity {

    private ArrayAdapter<Device> viewAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("woohoo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        System.out.println("CALLED");
        requestSavedDevices();

        /**iewAdapter = new ArrayAdapter<Device>(this, R.layout.device_list_item, requestSavedDevices());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_device);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        });**/
    }

    private List<Device> requestSavedDevices() {
        System.out.println("request called");
        SecretsHelper secretsHelper = new SecretsHelper(this);
        DeviceAPI deviceService = ServiceGenerator.createSmartGymService(DeviceAPI.class,
                                                                         secretsHelper.getAuthToken());
        System.out.println("made it");
        Call<List<Device>> call = deviceService.listDevices();
        call.enqueue(new Callback<List<Device>>() {

            @Override
            public void onResponse(Call<List<Device>> call, Response<List<Device>> response){
                System.out.println("response code");
                System.out.println(response.code());
                if (response.code() == 200){
                    System.out.println("fuck yea");
                   return;
                } else if (response.code() == 400){
                    System.out.println("fuck no");
                    return;
                } else {
                    System.out.println(response.code());
                    raiseGenericError();
                }
            }

            @Override
            public void onFailure(Call<List<Device>> call, Throwable t){
                System.out.println(t);
                raiseGenericError();
            }

            private void raiseGenericError(){
                Toast toast = Toast.makeText(getApplicationContext(),
                        getString(R.string.server_500_message),
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        return new ArrayList<Device>();
    }


}
