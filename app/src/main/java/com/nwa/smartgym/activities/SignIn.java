package com.nwa.smartgym.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nwa.smartgym.R;

import okhttp3.Headers;
import com.nwa.smartgym.api.AuthAPI;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.models.HTTPResponse;
import com.nwa.smartgym.models.Login;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignIn extends AppCompatActivity  {

    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            AuthAPI loginService = ServiceGenerator.createSmartGymService(AuthAPI.class);
            Call<HTTPResponse> call = loginService.logIn(new Login(mEmail, mPassword));


            call.enqueue(new Callback<HTTPResponse>() {

                @Override
                public void onResponse(Call<HTTPResponse> call, Response<HTTPResponse> response){
                    System.out.println(response.code());
                    if (response.code() == 200){
                        Headers headers = response.headers();
                        String token = headers.get("Set-Cookie");
                        storeSecurityHeader(token);
                    } else if (response.code() == 400){
                        mPasswordView.setError(getString(R.string.log_in_failed));
                        mPasswordView.requestFocus();
                    } else {
                        System.out.println(response.code());
                        raiseGenericError();
                    }
                }

                @Override
                public void onFailure(Call<HTTPResponse> call, Throwable t){
                    raiseGenericError();
                }

                private void raiseGenericError(){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.server_500_message),
                            Toast.LENGTH_SHORT);
                    toast.show();
                }

                private void storeSecurityHeader(String auth_tkt){
                    SharedPreferences secrets = getApplicationContext().getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE
                    );
                    SharedPreferences.Editor editor = secrets.edit();
                    editor.putString(getString(R.string.auth_tkt), auth_tkt);
                    editor.apply();
                }
            });

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}

