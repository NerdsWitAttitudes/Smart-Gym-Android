package com.nwa.smartgym.api;

import com.nwa.smartgym.models.Buddy;
import com.nwa.smartgym.models.HTTPResponse;
import com.nwa.smartgym.models.User;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by robin on 6-6-16.
 */
public interface UserAPI {
    @POST("user")
    Call<HTTPResponse> post(
            @Body User user
    );

    @GET("user/me")
    Call<User> getMe();

    @GET("user")
    Call<List<User>> list(@Query("exclude") List<UUID> exclude);
}
