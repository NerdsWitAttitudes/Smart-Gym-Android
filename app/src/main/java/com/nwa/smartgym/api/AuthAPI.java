package com.nwa.smartgym.api;

import com.nwa.smartgym.models.HTTPResponse;
import com.nwa.smartgym.models.Login;
import com.nwa.smartgym.models.SignUpData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


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
