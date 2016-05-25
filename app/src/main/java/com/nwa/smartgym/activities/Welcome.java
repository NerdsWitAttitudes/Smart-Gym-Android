package com.nwa.smartgym.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.nwa.smartgym.R;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void launchLogin(View view) {
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
    }

    public void launchSignUp(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
}
