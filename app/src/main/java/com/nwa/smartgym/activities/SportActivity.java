package com.nwa.smartgym.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.fitness.FitnessActivities;
import com.nwa.smartgym.R;
import com.nwa.smartgym.api.CardioActivityAPI;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.models.CardioActivity;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SportActivity extends Activity {

    UUID activityId = UUID.fromString("3c6e46c0-3894-42af-bff9-9b4caf3a8674");

    private GoogleFitService googleFitService;
    private ProgressDialog progressGoogleFit;
    private CardioActivityAPI smartGymService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);

        smartGymService = ServiceGenerator.createSmartGymService(CardioActivityAPI.class);

        Button startTreadmill = (Button) findViewById(R.id.btn_start_treadmill);
        Button stopTreadmill = (Button) findViewById(R.id.btn_stop_treadmill);

        if (startTreadmill != null && stopTreadmill != null) {
            startTreadmill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startSession(new CardioActivity(activityId));
                }
            });
        }

//        googleFitService = new GoogleFitService(this);
    }

    private void startSession(CardioActivity cardioActivity) {
        Call<CardioActivity> session = smartGymService.createSession(cardioActivity);
        session.enqueue(new Callback<CardioActivity>() {
            @Override
            public void onResponse(Call<CardioActivity> call, Response<CardioActivity> response) {
                Log.i("NWA", " added cardio session to db code: " + response.code());

//                googleFitService.startSession(FitnessActivities.RUNNING_TREADMILL);
            }

            @Override
            public void onFailure(Call<CardioActivity> call, Throwable t) {
                Log.i("NWA", " failed!!! to add session to db");
            }
        });
    }
}
