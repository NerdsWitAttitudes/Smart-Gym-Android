package com.nwa.smartgym.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TimePicker;

import com.nwa.smartgym.R;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.api.SportScheduleAPI;
import com.nwa.smartgym.api.callbacks.Callback;
import com.nwa.smartgym.lib.SecretsHelper;
import com.nwa.smartgym.models.SportSchedule;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SportScheduleItem extends AppCompatActivity {

    private SportSchedule sportSchedule;
    private SecretsHelper secretsHelper;

    private EditText sportScheduleName;
    private EditText sportScheduleReminder;

    private TimePicker sportScheduleTime;
    private TableLayout tableLayout;

    private SportScheduleAPI smartGymService;

    private Callback<SportSchedule> callback = new Callback<SportSchedule>(this) {
        @Override
        public void onResponse(Call<SportSchedule> call, Response<SportSchedule> response) {
            super.onResponse(call, response);

            if (response.code() == 200) {
                startActivity(new Intent(getBaseContext(), com.nwa.smartgym.activities.SportSchedule.class));
            } else {
                super.onFailure(call, new Throwable(getResources().getString(R.string.server_500_message)));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_schedule_item);

        sportSchedule = (SportSchedule) getIntent().getSerializableExtra("SportSchedule");

        secretsHelper = new SecretsHelper(this);
        smartGymService = ServiceGenerator.createSmartGymService(SportScheduleAPI.class, secretsHelper.getAuthToken());

        sportScheduleName = (EditText) findViewById(R.id.etSportScheduleName);
        sportScheduleReminder = (EditText) findViewById(R.id.etSportScheduleReminder);
        sportScheduleTime = (TimePicker) findViewById(R.id.tpSportScheduleTime);
        tableLayout = (TableLayout) findViewById(R.id.tlSportScheduleWeekdays);

        Button save = (Button) findViewById(R.id.btn_sav_sport_schedule);
        if (save != null) {
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sportSchedule == null) {
                        smartGymService.createSportSchedule(setSportScheduleFields(new SportSchedule()))
                                .enqueue(callback);
                    } else {
                        sportSchedule = setSportScheduleFields(sportSchedule);
                        smartGymService.updateSportSchedule(sportSchedule.getId(), sportSchedule)
                                .enqueue(callback);
                    }
                }
            });
        }

        createWeekDayCheckboxes(sportSchedule);

        if (sportSchedule != null) {
            fillFields(sportSchedule);
        }
    }

    private void fillFields(SportSchedule sportSchedule) {
        sportScheduleTime.setIs24HourView(true);

        sportScheduleName.setText(sportSchedule.getName());
        sportScheduleReminder.setText(String.valueOf(sportSchedule.getReminderMinutes()));
        sportScheduleTime.setCurrentHour(sportSchedule.getTime().getHourOfDay());
        sportScheduleTime.setCurrentMinute(sportSchedule.getTime().getMinuteOfHour());
    }

    private void createWeekDayCheckboxes(SportSchedule sportSchedule) {
        for (int day = 1; day <= DateTimeConstants.DAYS_PER_WEEK; day++) {
            String dayOfTheWeek = new LocalDate().withDayOfWeek(day).dayOfWeek().getAsText();

            TableRow tableRow = new TableRow(this);

            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(dayOfTheWeek);

            if (sportSchedule != null) {
                for (LocalDate.Property property : sportSchedule.getWeekdays()) {
                    if (property.getLocalDate().getDayOfWeek() == day) {
                        checkBox.setChecked(true);
                    }
                }
            }

            tableRow.addView(checkBox);
            tableLayout.addView(tableRow);
        }
    }

    public SportSchedule setSportScheduleFields(SportSchedule sportSchedule) {
        sportSchedule.setName(sportScheduleName.getText().toString());

        String reminderString = sportScheduleReminder.getText().toString();
        if ("".equals(reminderString)) {
            sportSchedule.setReminderMinutes(0);
        } else {
            sportSchedule.setReminderMinutes(Integer.parseInt(reminderString));
        }

        sportSchedule.setTime(new LocalTime(sportScheduleTime.getCurrentHour(), sportScheduleTime.getCurrentMinute()));

        List<LocalDate.Property> weekdays = new ArrayList<>();
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);

            CheckBox checkBox = (CheckBox) row.getChildAt(0);

            if (checkBox.isChecked()) {
                weekdays.add(new LocalDate().withDayOfWeek(i + 1).dayOfWeek());
            }
        }
        sportSchedule.setWeekdays(weekdays);

        return sportSchedule;
    }
}
