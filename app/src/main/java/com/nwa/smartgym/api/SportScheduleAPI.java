package com.nwa.smartgym.api;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import com.nwa.smartgym.models.SportSchedule;

public interface SportScheduleAPI {
    @GET("/sport_schedule/{id}")
    Call<SportSchedule> getSchedule(@Path("id") UUID userID);

    @GET("/sport_schedule")
    Call<List<SportSchedule>> getSchedules();

    @POST("/sport_schedule")
    Call<SportSchedule> createSportSchedule(@Body SportSchedule sportSchedule);

    @PUT("/sport_schedule/{id}")
    Call<SportSchedule> updateSportSchedule(@Path("id") UUID sportScheduleId, @Body SportSchedule sportSchedule);
}
