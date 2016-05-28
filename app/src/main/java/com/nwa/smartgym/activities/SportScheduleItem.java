package com.nwa.smartgym.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nwa.smartgym.R;
import com.nwa.smartgym.models.SportSchedule;

public class SportScheduleItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_schedule_item);

        SportSchedule sportSchedule = (SportSchedule) getIntent().getSerializableExtra("SportSchedule");
    }


}
