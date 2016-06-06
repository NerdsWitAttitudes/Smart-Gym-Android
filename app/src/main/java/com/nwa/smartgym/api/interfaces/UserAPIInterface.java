package com.nwa.smartgym.api.interfaces;

import android.content.Context;

import com.nwa.smartgym.api.AuthAPI;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.api.UserAPI;
import com.nwa.smartgym.api.callbacks.Callback;
import com.nwa.smartgym.lib.SecretsHelper;
import com.nwa.smartgym.models.HTTPResponse;
import com.nwa.smartgym.models.Login;
import com.nwa.smartgym.models.User;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by robin on 6-6-16.
 */
public class UserAPIInterface {
    private Context context;
    private UserAPI userService;
    private AuthAPIInterface authAPIInterface;

    public UserAPIInterface(Context context) {
        this.context = context;

        SecretsHelper secretsHelper = new SecretsHelper(context);
        this.userService = ServiceGenerator.createSmartGymService(UserAPI.class,
                secretsHelper.getAuthToken());
        this.authAPIInterface = new AuthAPIInterface(context);
    }

    public void post(final User user) {
        Call<HTTPResponse> call = this.userService.post(user);

        call.enqueue(new Callback<HTTPResponse>(context) {
            @Override
            public void onResponse(Call<HTTPResponse> call, Response<HTTPResponse> response) {
                super.onResponse(call, response);

                // when the user is successfully created we can log in
                Login loginData = new Login(user.getEmail(), user.getPassword());
                authAPIInterface.login(loginData);
            }
        });
    }
}
