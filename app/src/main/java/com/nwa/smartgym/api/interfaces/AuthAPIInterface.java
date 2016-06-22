package com.nwa.smartgym.api.interfaces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;

import com.nwa.smartgym.R;
import com.nwa.smartgym.activities.Main;
import com.nwa.smartgym.api.AuthAPI;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.api.callbacks.Callback;
import com.nwa.smartgym.lib.AuthHelper;
import com.nwa.smartgym.lib.MessageHelper;
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
    private AuthHelper authHelper;


    public AuthAPIInterface(Context context) {
        this.context = context;

        this.authService = ServiceGenerator.createSmartGymService(AuthAPI.class);
        this.authHelper = new AuthHelper(context);
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

                    // UserAPIInterface for locally storing the logged in user ID
                    UserAPIInterface userAPIInterface = new UserAPIInterface(context);

                    try {
                        userAPIInterface.persistCurrentUserLocally();
                    } catch (NullPointerException e) {
                        Log.e(getClass().getName(), "Unable to store current logged in user ID", e);

                        // We can't continue without the user stored so we should logout on remote
                        authHelper.logOut();
                        return;
                    }

                    launchMain();
                } else if (response.code() == 400 && passwordView != null) {
                    passwordView.setError(context.getString(R.string.log_in_failed));
                    passwordView.requestFocus();
                } else {
                    MessageHelper.raiseGenericError(context);
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
                authHelper.logOut();
            }
        });
    }
}
