package com.nwa.smartgym.api.interfaces;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.nwa.smartgym.api.AuthAPI;
import com.nwa.smartgym.api.BuddyAPI;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.api.UserAPI;
import com.nwa.smartgym.api.callbacks.Callback;
import com.nwa.smartgym.lib.ErrorHelper;
import com.nwa.smartgym.lib.SecretsHelper;
import com.nwa.smartgym.models.Buddy;
import com.nwa.smartgym.models.HTTPResponse;
import com.nwa.smartgym.models.Login;
import com.nwa.smartgym.models.User;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by robin on 6-6-16.
 */
public class BuddyAPIInterface {
    private Context context;
    private BuddyAPI buddyService;
    private PreparedQuery<Buddy> preparedListQuery;
    private Dao<Buddy, UUID> buddyDao;
    private AuthAPIInterface authAPIInterface;

    public BuddyAPIInterface(Context context, Dao<Buddy, UUID> buddyDao) {
        this.context = context;
        this.buddyDao = buddyDao;
        prepareQueries();

        SecretsHelper secretsHelper = new SecretsHelper(context);
        this.buddyService = ServiceGenerator.createSmartGymService(BuddyAPI.class,
                secretsHelper.getAuthToken());
        this.authAPIInterface = new AuthAPIInterface(context);
    }

    public void list() {
        Call<List<Buddy>> call = this.buddyService.list();

        call.enqueue(new Callback<List<Buddy>>(context) {
            @Override
            public void onResponse(Call<List<Buddy>> call, Response<List<Buddy>> response) {
                super.onResponse(call, response);

                List<Buddy> buddies = response.body();
            }
        });
    }

    private void prepareQueries() {
        try {
            preparedListQuery = buddyDao.queryBuilder().prepare();
        } catch (SQLException e) {
            Log.e(context.getPackageName(), e.getMessage());
            ErrorHelper.raiseGenericError(context);
        }
    }

    public PreparedQuery<Buddy> getListQuery() {
        return preparedListQuery;
    }
}
