package com.nwa.smartgym.api;

import com.nwa.smartgym.models.MusicPreference;

import java.util.List;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by rikvanderwerf on 11-6-16.
 */
public interface MusicPreferenceAPI {

    @GET("/music_preference/")
    Call<List<MusicPreference>> listMusicPreference();

    @POST("/music_preference")
    Call<ResponseBody> createMusicPreference(@Body MusicPreference musicPreference);

    @DELETE("/music_preference/{id}")
    Call<ResponseBody> deleteMusicPreference(@Path("id") UUID musicPreferenceId);

}
