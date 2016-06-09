package com.nwa.smartgym.api;

import com.nwa.smartgym.models.Buddy;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
/**
 * Created by robin on 6-6-16.
 */
public interface BuddyAPI {
    @GET("user/buddies")
    Call<List<Buddy>> list();
}
