package com.nwa.smartgym.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nwa.smartgym.R;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.api.SportScheduleAPI;
import com.nwa.smartgym.lib.SportScheduleAdapter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SportSchedule extends AppCompatActivity {

    UUID userId = UUID.fromString("6159c216-a92f-4fdb-b043-baefa82009f1");

    private ListView listView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getBaseContext(), SportScheduleItem.class).putExtra("user_id", userId));
                }
            });
        }

        progressDialog = ProgressDialog.show(this, "Loading", "Loading Sport Schedules");
        SportScheduleTask sportScheduleTask = new SportScheduleTask(userId);
        sportScheduleTask.execute((Void) null);
    }

    public class SportScheduleTask extends AsyncTask<Void, Void, Boolean> {

        private UUID userId;

        public SportScheduleTask(UUID userId) {
            this.userId = userId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SportScheduleAPI sportScheduleService = ServiceGenerator.createSmartGymService(SportScheduleAPI.class);
            Call<List<com.nwa.smartgym.models.SportSchedule>> call = sportScheduleService.getSchedules(userId);

            call.enqueue(new Callback<List<com.nwa.smartgym.models.SportSchedule>>() {
                @Override
                public void onResponse(Call<List<com.nwa.smartgym.models.SportSchedule>> call, Response<List<com.nwa.smartgym.models.SportSchedule>> response) {
                    progressDialog.dismiss();
                    List<com.nwa.smartgym.models.SportSchedule> sportSchedules = response.body();

                    SportScheduleAdapter sportScheduleAdapter = new SportScheduleAdapter(getBaseContext(), sportSchedules);
                    listView.setAdapter(sportScheduleAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            com.nwa.smartgym.models.SportSchedule sportSchedule = (com.nwa.smartgym.models.SportSchedule) parent.getItemAtPosition(position);
                            startActivity(new Intent(getBaseContext(), SportScheduleItem.class).putExtra("SportSchedule", (Serializable) sportSchedule));
                        }
                    });

                }

                @Override
                public void onFailure(Call<List<com.nwa.smartgym.models.SportSchedule>> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.server_500_message),
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

            return false;
        }
    }
}
