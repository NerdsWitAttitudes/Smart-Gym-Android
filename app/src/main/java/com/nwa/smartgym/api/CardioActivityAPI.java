package com.nwa.smartgym.api;

import com.nwa.smartgym.models.CardioActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CardioActivityAPI {

    @GET("/cardio_activity")
    Call<List<CardioActivity>> getSessions();

    @POST("/cardio_activity")
    Call<CardioActivity> createSession(@Body CardioActivity cardioActivity);

}