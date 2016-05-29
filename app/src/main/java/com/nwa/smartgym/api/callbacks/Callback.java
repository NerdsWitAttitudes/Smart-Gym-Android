package com.nwa.smartgym.api.callbacks;

import android.content.Context;
import android.widget.Toast;

import com.nwa.smartgym.R;
import com.nwa.smartgym.api.DeviceAPI;
import com.nwa.smartgym.api.ServiceGenerator;
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
        if (response.code() != 200 && response.code() != 400) {
            raiseGenericError();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        System.out.println("onfailure");
        raiseGenericError();
    }

    public static void raiseGenericError() {
        Toast toast = Toast.makeText(context,
                context.getString(R.string.server_500_message),
                Toast.LENGTH_SHORT);
        toast.show();
    }
}
