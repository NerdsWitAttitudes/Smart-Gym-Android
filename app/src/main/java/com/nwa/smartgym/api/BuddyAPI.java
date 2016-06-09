package com.nwa.smartgym.api;

import com.nwa.smartgym.models.Buddy;
import com.nwa.smartgym.models.HTTPResponse;
import com.nwa.smartgym.models.User;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by robin on 6-6-16.
 */
public interface BuddyAPI {
    @GET("user/buddies")
    Call<List<Buddy>> list();

    @PUT("user/buddies")
    Call<Buddy> put(
            @Body Map<String, UUID> requestBody
    );

    @DELETE("user/buddies/{id}")
    Call<HTTPResponse> delete(@Path("id") UUID buddyID);
}
