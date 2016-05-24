package com.nwa.smartgym.api;

import com.nwa.smartgym.models.Device;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by robin on 24-5-16.
 */
public interface DeviceAPI {
    @GET("device")
    Call<List<Device>> listDevices();
}
