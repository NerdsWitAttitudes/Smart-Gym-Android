package com.nwa.smartgym.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nwa.smartgym.R;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.api.SportScheduleAPI;
import com.nwa.smartgym.api.callbacks.Callback;
import com.nwa.smartgym.lib.SecretsHelper;
import com.nwa.smartgym.lib.adapters.SportScheduleAdapter;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SportSchedule extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_schedule);

        listView = (ListView) findViewById(R.id.listView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_sport_schedule);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(SportSchedule.this, SportScheduleItem.class));
                }
            });
        }

        showSportSchedules();
    }

    private void showSportSchedules() {
        ProgressDialog progressDialog = ProgressDialog.show(this, "Loading", "Loading Sport Schedules");

        SecretsHelper secretsHelper = new SecretsHelper(this);
        SportScheduleAPI sportScheduleService = ServiceGenerator.createSmartGymService(SportScheduleAPI.class, secretsHelper.getAuthToken());
        Call<List<com.nwa.smartgym.models.SportSchedule>> call = sportScheduleService.getSchedules();

        call.enqueue(new Callback<List<com.nwa.smartgym.models.SportSchedule>>(this) {
            @Override
            public void onResponse(Call<List<com.nwa.smartgym.models.SportSchedule>> call, Response<List<com.nwa.smartgym.models.SportSchedule>> response) {
                super.onResponse(call, response);

                if (response.code() == 200) {
                    List<com.nwa.smartgym.models.SportSchedule> sportSchedules = response.body();

                    if (sportSchedules.isEmpty()) {
                        Snackbar.make(listView, R.string.sport_schedule_make_one, Snackbar.LENGTH_INDEFINITE).show();
                    }

                    SportScheduleAdapter sportScheduleAdapter = new SportScheduleAdapter(SportSchedule.this, sportSchedules);
                    listView.setAdapter(sportScheduleAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            com.nwa.smartgym.models.SportSchedule sportSchedule = (com.nwa.smartgym.models.SportSchedule) parent.getItemAtPosition(position);
                            startActivity(new Intent(SportSchedule.this, SportScheduleItem.class).putExtra("SportSchedule", (Serializable) sportSchedule));
                        }
                    });
                }
            }
        });

        progressDialog.dismiss();
    }
}
