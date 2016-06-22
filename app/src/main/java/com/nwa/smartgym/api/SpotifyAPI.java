package com.nwa.smartgym.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by rikvanderwerf on 22-6-16.
 */
public interface SpotifyAPI {

    @GET("/spotify/genres")
    Call<ResponseBody> listGenres();

}
