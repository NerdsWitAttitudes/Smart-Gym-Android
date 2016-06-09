package com.nwa.smartgym.api.interfaces;

import android.content.Context;
import android.util.Log;
import android.widget.Adapter;

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

import org.json.JSONObject;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public BuddyAPIInterface(Context context, Dao<Buddy, UUID> buddyDao) {
        this.context = context;
        this.buddyDao = buddyDao;
        prepareQueries();

        SecretsHelper secretsHelper = new SecretsHelper(context);
        this.buddyService = ServiceGenerator.createSmartGymService(BuddyAPI.class,
                secretsHelper.getAuthToken());
    }

    public void list() {
        Call<List<Buddy>> call = this.buddyService.list();

        call.enqueue(new Callback<List<Buddy>>(context) {
            @Override
            public void onResponse(Call<List<Buddy>> call, Response<List<Buddy>> response) {
                super.onResponse(call, response);

                if (response.code() == 200) {
                    for (Buddy buddy : response.body()) {
                        try {
                            buddyDao.create(buddy);
                        } catch (SQLException e) {
                            Log.e(context.getClass().getName(), "Unable to create buddy", e);
                        }
                    }
                }
            }
        });
    }

    public void put(UUID newBuddyId) {
        // The api endpoint expects a key value pair specifying the user id of the new buddy
        Map<String, UUID> requestBody = new HashMap<>();
        requestBody.put("user_id", newBuddyId);

        Call<Buddy> call = this.buddyService.put(requestBody);

        call.enqueue(new Callback<Buddy>(context) {
            @Override
            public void onResponse(Call<Buddy> call, Response<Buddy> response) {
                super.onResponse(call, response);

                try {
                    buddyDao.create(response.body());
                } catch (SQLException e) {
                    Log.e(context.getClass().getName(), "Unable to create buddy", e);
                }
            }
        });

    }

    public void delete(final Buddy buddy) {
        Call<HTTPResponse> call = buddyService.delete(buddy.getId());
        call.enqueue(new Callback<HTTPResponse>(context) {

            @Override
            public void onResponse(Call<HTTPResponse> call, Response<HTTPResponse> response) {
                super.onResponse(call, response);
                if (response.code() == 204) {
                    try {
                        buddyDao.deleteById(buddy.getId());
                    } catch (SQLException e) {
                        Log.e(context.getClass().getName(), "Unable to delete buddy", e);
                    }
                }
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
