package com.nwa.smartgym.api.interfaces;

import android.content.Context;

import com.nwa.smartgym.api.AuthAPI;
import com.nwa.smartgym.api.BuddyAPI;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.api.UserAPI;
import com.nwa.smartgym.api.callbacks.Callback;
import com.nwa.smartgym.lib.SecretsHelper;
import com.nwa.smartgym.models.Buddy;
import com.nwa.smartgym.models.HTTPResponse;
import com.nwa.smartgym.models.Login;
import com.nwa.smartgym.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by robin on 6-6-16.
 */
public class BuddyAPIInterface {
    private Context context;
    private BuddyAPI buddyService;
    private AuthAPIInterface authAPIInterface;

    public BuddyAPIInterface(Context context) {
        this.context = context;

        SecretsHelper secretsHelper = new SecretsHelper(context);
        this.buddyService = ServiceGenerator.createSmartGymService(BuddyAPI.class,
                secretsHelper.getAuthToken());
        this.authAPIInterface = new AuthAPIInterface(context);
    }

    public void getBuddies() {
        Call<List<Buddy>> call = this.buddyService.getBuddies();

        call.enqueue(new Callback<List<Buddy>>(context) {
            @Override
            public void onResponse(Call<List<Buddy>> call, Response<List<Buddy>> response) {
                super.onResponse(call, response);

                List<Buddy> buddies = response.body();
            }
        });
    }
}
