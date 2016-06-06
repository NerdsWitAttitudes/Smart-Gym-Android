package com.nwa.smartgym.api.callbacks;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.nwa.smartgym.R;
import com.nwa.smartgym.activities.Welcome;
import com.nwa.smartgym.api.DeviceAPI;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.lib.ErrorHelper;
import com.nwa.smartgym.lib.SecretsHelper;

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
        ErrorHelper.raiseGenericError(context);
    }

    private void handleUnauthenticatedUser() {
        Intent intent = new Intent(context, Welcome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
