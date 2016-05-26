package com.nwa.smartgym.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.nwa.smartgym.R;
import com.nwa.smartgym.adapter.SportScheduleAdapter;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.api.SportScheduleAPI;

import java.util.List;
import java.util.UUID;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SportSchedule extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        SportScheduleTask sportScheduleTask = new SportScheduleTask(UUID.fromString("6159c216-a92f-4fdb-b043-baefa82009f1"));
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
                    List<com.nwa.smartgym.models.SportSchedule> sportSchedules = response.body();

                    SportScheduleAdapter sportScheduleAdapter = new SportScheduleAdapter(getBaseContext(), sportSchedules);
                    listView.setAdapter(sportScheduleAdapter);
                }

                @Override
                public void onFailure(Call<List<com.nwa.smartgym.models.SportSchedule>> call, Throwable t) {
                    HttpUrl url = call.request().url();

                    System.out.println("url : " + url.toString() );
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
