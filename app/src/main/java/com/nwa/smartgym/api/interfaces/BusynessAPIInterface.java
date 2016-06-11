package com.nwa.smartgym.api.interfaces;

import android.content.Context;
import android.util.Log;

import com.nwa.smartgym.api.BusynessAPI;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.api.callbacks.Callback;
import com.nwa.smartgym.fragments.main.BusynessFragment;
import com.nwa.smartgym.lib.SecretsHelper;
import com.nwa.smartgym.models.Device;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by rikvanderwerf on 11-6-16.
 */
public class BusynessAPIInterface {

    private BusynessFragment busynessFragment;
    private BusynessAPI busynessService;

    public BusynessAPIInterface(BusynessFragment context) {
        this.busynessFragment = context;

        SecretsHelper secretsHelper = new SecretsHelper(busynessFragment.getContext());
        this.busynessService = ServiceGenerator.createSmartGymService(BusynessAPI.class,
                secretsHelper.getAuthToken());
    }

    public void past(String date) {
        Call<ResponseBody> call = busynessService.past(date);
        call.enqueue(new Callback<ResponseBody>(busynessFragment.getContext()) {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (response.code() == 200) {
                    try {
                        busynessFragment.updateGraph(new JSONObject(response.body().string()));
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void today() {
        Call<ResponseBody> call = busynessService.today();
        call.enqueue(new Callback<ResponseBody>(busynessFragment.getContext()) {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (response.code() == 200) {
                    try {
                        busynessFragment.updateGraph(new JSONObject(response.body().string()));
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void predict(String date) {
        Call<ResponseBody> call = busynessService.predict(date);
        call.enqueue(new Callback<ResponseBody>(busynessFragment.getContext()) {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (response.code() == 200) {
                    try {
                        busynessFragment.updateGraph(new JSONObject(response.body().string()));
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
