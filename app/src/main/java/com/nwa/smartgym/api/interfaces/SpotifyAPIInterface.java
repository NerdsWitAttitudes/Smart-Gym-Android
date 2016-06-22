package com.nwa.smartgym.api.interfaces;

import android.content.Context;

import com.nwa.smartgym.activities.MusicPreferenceItem;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.api.SpotifyAPI;
import com.nwa.smartgym.api.callbacks.Callback;
import com.nwa.smartgym.lib.SecretsHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by rikvanderwerf on 22-6-16.
 */
public class SpotifyAPIInterface {

    private MusicPreferenceItem context;
    private SpotifyAPI spotifyService;

    public SpotifyAPIInterface(MusicPreferenceItem context){
        this.context = context;
        SecretsHelper secretsHelper = new SecretsHelper(context);
        this.spotifyService = ServiceGenerator.createSmartGymService(SpotifyAPI.class, secretsHelper.getAuthToken());
    }

    public void getGenres() {
        Call<ResponseBody> call = spotifyService.listGenres();
        call.enqueue(new Callback<ResponseBody>(context) {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                System.out.println(response.code());
                if (response.code() == 200) {
                    try {
                        context.fillGenreSpinner(new JSONObject(response.body().string()));
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
