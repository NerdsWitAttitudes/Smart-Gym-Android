package com.nwa.smartgym.api.interfaces;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.api.UserAPI;
import com.nwa.smartgym.api.callbacks.Callback;
import com.nwa.smartgym.lib.SecretsHelper;
import com.nwa.smartgym.lib.adapters.UserAdapter;
import com.nwa.smartgym.models.HTTPResponse;
import com.nwa.smartgym.models.Login;
import com.nwa.smartgym.models.User;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by robin on 6-6-16.
 */
public class UserAPIInterface {
    private Context context;
    private UserAdapter userAdapter;
    private UserAPI userService;
    private AuthAPIInterface authAPIInterface;

    public UserAPIInterface(Context context, UserAdapter userAdapter) {
        this(context);
        this.userAdapter = userAdapter;
    }

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

    public void list() {
        if (userAdapter == null) {
            return;
        }

        Call<List<User>> call = this.userService.list();

        call.enqueue(new Callback<List<User>>(context) {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                super.onResponse(call, response);
                userAdapter.addAll(response.body());
                userAdapter.notifyDataSetChanged();
            }
        });
    }

    public void persistCurrentUserLocally() {
        Call<User> call = this.userService.getMe();

        call.enqueue(new Callback<User>(context) {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                super.onResponse(call, response);
                SecretsHelper secretsHelper = new SecretsHelper(context);
                secretsHelper.setCurrentUserID(response.body().getId());
            }
        });
    }
}
