package smartgym.smartgym;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nwa.smartgym.R;

import java.util.List;
import java.util.UUID;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smartgym.api.ServiceGenerator;
import smartgym.api.SportScheduleAPI;

public class SportSchedule extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.textView);

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
            SportScheduleAPI sportScheduleService = ServiceGenerator.createPartyPeakService(SportScheduleAPI.class);
            Call<List<smartgym.models.SportSchedule>> call = sportScheduleService.getSchedules(userId);

            call.enqueue(new Callback<List<smartgym.models.SportSchedule>>() {
                @Override
                public void onResponse(Call<List<smartgym.models.SportSchedule>> call, Response<List<smartgym.models.SportSchedule>> response) {
                    List<smartgym.models.SportSchedule> sportSchedules = response.body();
                    textView.setText(sportSchedules.toString());
                }

                @Override
                public void onFailure(Call<List<smartgym.models.SportSchedule>> call, Throwable t) {
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
