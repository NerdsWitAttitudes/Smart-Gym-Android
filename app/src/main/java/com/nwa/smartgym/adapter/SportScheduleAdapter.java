package com.nwa.smartgym.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nwa.smartgym.R;
import com.nwa.smartgym.models.SportSchedule;

import java.util.List;

public class SportScheduleAdapter extends ArrayAdapter<SportSchedule> {

    public SportScheduleAdapter(Context context, List<SportSchedule> sportSchedules) {
        super(context, 0, sportSchedules);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SportSchedule sportSchedule = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_sport_schedule, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tvSportScheduleTitle);
        TextView tvReminder = (TextView) convertView.findViewById(R.id.tvSportScheduleReminder);

        tvName.setText(sportSchedule.getName());
        tvReminder.setText(String.valueOf(sportSchedule.getReminderMinutes()));

        return convertView;
    }
}
