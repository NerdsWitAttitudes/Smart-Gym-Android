package com.nwa.smartgym.api;

import com.nwa.smartgym.models.CardioActivity;
import com.nwa.smartgym.models.UserActivity;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CardioActivityAPI {

    String baseUrl = "/cardio_activity";

    @GET(baseUrl)
    Call<List<CardioActivity>> getSessions();

    @POST(baseUrl)
    Call<CardioActivity> createSession(@Body CardioActivity cardioActivity);

    @PUT(baseUrl + "/{id}")
    Call<CardioActivity> endSession(@Path("id") UUID cardioActivityId, @Body CardioActivity cardioActivity);

    @GET("/user_activity")
    Call<List<UserActivity>> getUserActivities();

}