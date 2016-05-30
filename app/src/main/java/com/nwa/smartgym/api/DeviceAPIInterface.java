package com.nwa.smartgym.api;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;
import android.widget.BaseAdapter;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.nwa.smartgym.api.callbacks.Callback;
import com.nwa.smartgym.lib.SecretsHelper;
import com.nwa.smartgym.models.Device;
import com.nwa.smartgym.models.HTTPResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by robin on 29-5-16.
 */
public class DeviceAPIInterface {
    private Context context;
    private DeviceAPI deviceService;
    private Dao<Device, UUID> deviceDao;

    private static final String DEVICE_ADDRESS_FIELD = "device_address";

    public DeviceAPIInterface(Context context, Dao<Device, UUID> dao) {
        this.context = context;
        this.deviceDao = dao;

        SecretsHelper secretsHelper = new SecretsHelper(context);
        this.deviceService = ServiceGenerator.createSmartGymService(DeviceAPI.class,
                secretsHelper.getAuthToken());
    }

    public void list() {
        Call<List<Device>> call = deviceService.listDevices();
        call.enqueue(new Callback<List<Device>>(context) {
            @Override
            public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
                if (response.code() == 200) {
                    for (Device device : response.body()) {
                        try {
                            deviceDao.create(device);
                        } catch (SQLiteConstraintException e) {
                            // Meaning the device already exists and does not have to be added
                            continue;
                        } catch (SQLException e) {
                            Log.e(context.getClass().getName(), "Unable to persist device", e);
                        }
                    }
                } else {
                    raiseGenericError();
                }
            }
        });
    }

    public void delete(final Device device) {
        Call<HTTPResponse> call = deviceService.deleteDevice(device.getId());
        call.enqueue(new Callback<HTTPResponse>(context) {

            @Override
            public void onResponse(Call<HTTPResponse> call, Response<HTTPResponse> response) {
                System.out.println(response.code());
                if (response.code() == 200) {
                    try {
                        deviceDao.deleteById(device.getId());
                    } catch (SQLException e) {
                        Log.e(context.getClass().getName(), "Unable to delete device", e);
                    }
                } else {
                    raiseGenericError();
                }
            }
        });
    }

    public void persist(final Device device) {
        Call<Device> call = deviceService.postDevice(device);
        call.enqueue(new Callback<Device>(context) {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                if (response.code() == 201) {
                    try {
                        System.out.println(response.body().getName());
                        deviceDao.create(response.body());
                    } catch (SQLException e) {
                        Log.e(context.getClass().getName(), "Unable to create device", e);
                    }
                } else {
                    raiseGenericError();
                }
            }
        });
    }

    public boolean deviceExists(String deviceAddress) {
        try {
            QueryBuilder queryBuilder = deviceDao.queryBuilder();
            Where whereClause = queryBuilder.where();
            whereClause.eq(DEVICE_ADDRESS_FIELD, deviceAddress);
            if (queryBuilder.queryForFirst() != null) {
                return true;
            }
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "Failed to get device by device address");
        }
        return false;
    }
}
