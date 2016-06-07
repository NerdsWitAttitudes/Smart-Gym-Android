package com.nwa.smartgym.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.fitness.FitnessActivities;
import com.nwa.smartgym.R;
import com.nwa.smartgym.api.CardioActivityAPI;
import com.nwa.smartgym.api.GoogleFitService;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.lib.ErrorHelper;
import com.nwa.smartgym.lib.SecretsHelper;
import com.nwa.smartgym.models.CardioActivity;

import java.util.UUID;

public class SportActivity extends AppCompatActivity {

    UUID activityId = UUID.fromString("a920093a-7fa5-440a-a257-93add88cf8f9");

    private GoogleFitService googleFitService;
    private CardioActivityAPI smartGymService;
    private CardioActivity currentCardioActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_activity);

        smartGymService = ServiceGenerator.createSmartGymService(CardioActivityAPI.class, new SecretsHelper(this).getAuthToken());
        googleFitService = new GoogleFitService(this, smartGymService);

        Button startTreadmill = (Button) findViewById(R.id.btn_start_treadmill);
        Button stopTreadmill = (Button) findViewById(R.id.btn_stop_treadmill);

        if (startTreadmill != null && stopTreadmill != null) {
            startTreadmill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentCardioActivity == null) {
                        currentCardioActivity = new CardioActivity(activityId, FitnessActivities.RUNNING_TREADMILL);
                        googleFitService.addCardioActivity(currentCardioActivity);
                    } else {
                        ErrorHelper.raiseGenericError(getBaseContext());
                    }
                }
            });

            stopTreadmill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    googleFitService.stopSession();
                    currentCardioActivity = null;
                }
            });
        }

        Button startCycling = (Button) findViewById(R.id.btn_start_cycling);
        Button stopCycling = (Button) findViewById(R.id.btn_stop_cycling);

        if (startCycling != null && stopCycling != null) {
            startCycling.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentCardioActivity == null) {
                        currentCardioActivity = new CardioActivity(activityId, FitnessActivities.BIKING_SPINNING);
                        googleFitService.addCardioActivity(currentCardioActivity);
                    } else {
                        ErrorHelper.raiseGenericError(getBaseContext());
                    }
                }
            });

            stopCycling.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    googleFitService.stopSession();
                    currentCardioActivity = null;
                }
            });
        }
     }
}