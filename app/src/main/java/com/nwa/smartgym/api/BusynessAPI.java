package com.nwa.smartgym.api;

import com.nwa.smartgym.models.HTTPResponse;
import com.nwa.smartgym.models.Login;
import com.nwa.smartgym.models.SignUpData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Created by robin on 19-4-16.
 */
public interface BusynessAPI {
    @GET("busyness/past?date={date}")
    Call<ResponseBody> past(
            @Path("date") String busynessDateText
    );

    @GET("busyness/today")
    Call<ResponseBody> today(

    );

    @GET("busyness/predict?date={date}")
    Call<ResponseBody> predict(
            @Path("date") String busynessDateText
    );
}
