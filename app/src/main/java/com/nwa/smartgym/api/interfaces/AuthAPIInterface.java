package com.nwa.smartgym.api.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;

import com.nwa.smartgym.R;
import com.nwa.smartgym.activities.Main;
import com.nwa.smartgym.activities.Welcome;
import com.nwa.smartgym.api.AuthAPI;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.api.callbacks.Callback;
import com.nwa.smartgym.lib.ErrorHelper;
import com.nwa.smartgym.lib.SecretsHelper;
import com.nwa.smartgym.models.HTTPResponse;
import com.nwa.smartgym.models.Login;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by robin on 6-6-16.
 */
public class AuthAPIInterface {
    private Context context;
    private AuthAPI authService;


    public AuthAPIInterface(Context context) {
        this.context = context;

        this.authService = ServiceGenerator.createSmartGymService(AuthAPI.class);
    }

    public void login(Login login) {
        login(login, null);
    }

    public void login(Login login, final EditText passwordView) {
        Call<HTTPResponse> call = this.authService.logIn(login);

        call.enqueue(new Callback<HTTPResponse>(context) {

            @Override
            public void onResponse(Call<HTTPResponse> call, Response<HTTPResponse> response) {
                if (response.code() == 200) {
                    Headers headers = response.headers();
                    String token = headers.get("Set-Cookie");
                    storeSecurityHeader(token);
                    launchMain();
                } else if (response.code() == 400) {
                    passwordView.setError(context.getString(R.string.log_in_failed));
                    passwordView.requestFocus();
                } else {
                    ErrorHelper.raiseGenericError(context);
                }
            }

            private void storeSecurityHeader(String auth_tkt){
                SharedPreferences secrets = context.getSharedPreferences(
                        context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
                );
                SharedPreferences.Editor editor = secrets.edit();
                editor.putString(context.getString(R.string.auth_tkt), auth_tkt);
                editor.apply();
            }

            private void launchMain() {
                Intent intent = new Intent(context, Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

        });
    }

    public void logout() {
        Call<HTTPResponse> call = this.authService.logOut();
        call.enqueue(new Callback<HTTPResponse>(context){
            @Override
            public void onResponse(Call<HTTPResponse> call, Response<HTTPResponse> response) {
                super.onResponse(call, response);
                removeSecurityHeader();
                launchWelcome();
            }

            private void launchWelcome() {
                Intent intent = new Intent(context, Welcome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            private void removeSecurityHeader() {
                SharedPreferences secrets = context.getSharedPreferences(
                        context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
                );
                SharedPreferences.Editor editor = secrets.edit();
                editor.remove(context.getString(R.string.auth_tkt));
                editor.apply();
            }
        });
    }
}
