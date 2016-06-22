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
import com.nwa.smartgym.api.callbacks.Callback;
import com.nwa.smartgym.lib.MessageHelper;
import com.nwa.smartgym.lib.SecretsHelper;
import com.nwa.smartgym.models.CardioActivity;
import com.nwa.smartgym.models.UserActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SportActivity extends AppCompatActivity {

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
        setButtonOnClick(startTreadmill, stopTreadmill, FitnessActivities.RUNNING_TREADMILL);

        Button startCycling = (Button) findViewById(R.id.btn_start_cycling);
        Button stopCycling = (Button) findViewById(R.id.btn_stop_cycling);
        setButtonOnClick(startCycling, stopCycling, FitnessActivities.BIKING_SPINNING);
    }

    private void setButtonOnClick(Button btnStart, Button btnStop, final String activityType) {
        if (btnStart != null && btnStop != null) {
            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentCardioActivity == null) {
                        createNewCardioActivity(activityType);
                    } else {
                        MessageHelper.raiseGenericError(getBaseContext());
                    }
                }
            });

            btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    googleFitService.stopSession();
                    currentCardioActivity = null;
                }
            });
        }
    }

    private void createNewCardioActivity(final String activityType) {
        final Call<List<UserActivity>> userActivities = smartGymService.getUserActivities();
        userActivities.enqueue(new Callback<List<UserActivity>>(this) {
            @Override
            public void onResponse(Call<List<UserActivity>> call, Response<List<UserActivity>> response) {
                super.onResponse(call, response);

                if (response.body().size() > 0) {
                    for (UserActivity userActivity : response.body()) {
                        if (userActivity.getEndDate() == null) {
                            currentCardioActivity = new CardioActivity(userActivity.getId(), activityType);
                            googleFitService.addCardioActivity(currentCardioActivity);
                        }
                    }

                } else {
                    super.onFailure(call, new Throwable("No active activity found"));
                }
            }
        });
    }
}