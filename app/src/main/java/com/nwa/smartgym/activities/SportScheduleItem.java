package com.nwa.smartgym.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
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
import com.nwa.smartgym.models.SportSchedule;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SportScheduleItem extends AppCompatActivity {

    private SportSchedule sportSchedule;

    private EditText sportScheduleName;
    private EditText sportScheduleReminder;
    private TimePicker sportScheduleTime;

    private TableLayout tableLayout;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_schedule_item);
        sportSchedule = (SportSchedule) getIntent().getSerializableExtra("SportSchedule");

        sportScheduleName = (EditText) findViewById(R.id.etSportScheduleName);
        sportScheduleReminder = (EditText) findViewById(R.id.etSportScheduleReminder);
        sportScheduleTime = (TimePicker) findViewById(R.id.tpSportScheduleTime);
        tableLayout = (TableLayout) findViewById(R.id.tlSportScheduleWeekdays);

        save = (Button) findViewById(R.id.btn_sav_sport_schedule);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sportSchedule = setSportScheduleFields(sportSchedule);
                saveSportSchedule(sportSchedule);
            }
        });

        setWeekdays(sportSchedule);
        setFields(sportSchedule);
    }

    private void saveSportSchedule(SportSchedule sportSchedule) {
        SportScheduleAPI smartGymService = ServiceGenerator.createSmartGymService(SportScheduleAPI.class);
        Call<SportSchedule> sportScheduleCall = smartGymService.updateSportSchedule(sportSchedule.getId(), sportSchedule);

        sportScheduleCall.enqueue(new Callback<SportSchedule>() {
            @Override
            public void onResponse(Call<SportSchedule> call, Response<SportSchedule> response) {
                finish();
            }

            @Override
            public void onFailure(Call<SportSchedule> call, Throwable t) {

            }
        });
    }

    private void setFields(SportSchedule sportSchedule) {
        sportScheduleTime.setIs24HourView(true);

        if (sportSchedule != null) {
            sportScheduleName.setText(sportSchedule.getName());
            sportScheduleReminder.setText(String.valueOf(sportSchedule.getReminderMinutes()));
            sportScheduleTime.setCurrentHour(sportSchedule.getTime().getHourOfDay());
            sportScheduleTime.setCurrentMinute(sportSchedule.getTime().getMinuteOfHour());
        }
    }

    private void setWeekdays(SportSchedule sportSchedule) {
        for (int day = 1; day <= DateTimeConstants.DAYS_PER_WEEK; day++) {
            String dayString = new LocalDate().withDayOfWeek(day).dayOfWeek().getAsText();

            TableRow tableRow = new TableRow(this);

            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(dayString);

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
        Editable text = sportScheduleName.getText();
        sportSchedule.setName(text.toString());
        String string = sportScheduleReminder.getText().toString();
        Integer integer = Integer.parseInt(string);
        sportSchedule.setReminderMinutes(integer);
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
