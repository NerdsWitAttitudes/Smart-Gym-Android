package com.nwa.smartgym.api;

import com.nwa.smartgym.models.Device;
import com.nwa.smartgym.models.HTTPResponse;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by robin on 24-5-16.
 */
public interface DeviceAPI {
    @GET("device")
    Call<List<Device>> listDevices();

    @DELETE("device/{id}")
    Call<HTTPResponse> deleteDevice(@Path("id") UUID deviceID);
}
