package com.nwa.smartgym.api.callbacks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.nwa.smartgym.R;
import com.nwa.smartgym.activities.Welcome;
import com.nwa.smartgym.api.DeviceAPI;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.lib.AuthHelper;
import com.nwa.smartgym.lib.ErrorHelper;
import com.nwa.smartgym.lib.SecretsHelper;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by robin on 29-5-16.
 */
public abstract class Callback<T> implements retrofit2.Callback<T> {
    private static Context context;

    public Callback(Context context) {
        Callback.context = context;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (!response.isSuccessful()) {
            if (response.code() == 401) {
                handleUnauthenticatedUser();
            } else {
                ErrorHelper.raiseGenericError(context);
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (t instanceof SocketTimeoutException) {
            ErrorHelper.showToastError(context, context.getString(R.string.timeout));
        } else {
            Log.e(this.getClass().getName(), t.toString());
            ErrorHelper.raiseGenericError(context);
        }
    }

    private void handleUnauthenticatedUser() {
        AuthHelper authHelper = new AuthHelper(context);
        authHelper.logOut();
    }
}
