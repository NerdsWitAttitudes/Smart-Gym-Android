package com.nwa.smartgym.api;

import com.nwa.smartgym.models.CardioActivity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CardioActivityAPI {

    @POST("/cardio_activity")
    Call<CardioActivity> createSession(@Body CardioActivity cardioActivity);

}
