package com.nwa.smartgym.api;

import com.nwa.smartgym.models.HTTPResponse;
import com.nwa.smartgym.models.Login;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import smartgym.models.SignUpData;

/**
 * Created by robin on 19-4-16.
 */
public interface AuthAPI {
    @POST("auth/login")
    Call<HTTPResponse> logIn(
            @Body Login login
    );

    @POST("user")
    Call<HTTPResponse> signUp(
            @Body SignUpData signUpData
    );
}
