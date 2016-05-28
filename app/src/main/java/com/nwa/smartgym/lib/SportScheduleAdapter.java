package com.nwa.smartgym.lib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nwa.smartgym.R;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.api.SportScheduleAPI;
import com.nwa.smartgym.models.SportSchedule;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SportScheduleAdapter extends ArrayAdapter<SportSchedule> {

    public SportScheduleAdapter(Context context, List<SportSchedule> sportSchedules) {
        super(context, 0, sportSchedules);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SportSchedule sportSchedule = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_sport_schedule, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tvSportScheduleTitle);
        TextView tvDateTime = (TextView) convertView.findViewById(R.id.tvSportScheduleDateTime);
        final Switch switchActive = (Switch) convertView.findViewById(R.id.switchActive);

        tvName.setText(sportSchedule.getName());

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm dd-MM-yyyy");
        tvDateTime.setText(dateTimeFormatter.print(sportSchedule.getDateTime()));

        switchActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if (sportSchedule.isActive() != isChecked) {
                    sportSchedule.setIsActive(isChecked);

                    SportScheduleAPI sportScheduleService = ServiceGenerator.createSmartGymService(SportScheduleAPI.class);
                    Call<SportSchedule> sportScheduleCall = sportScheduleService.updateSportSchedule(sportSchedule.getId(), sportSchedule);

                    sportScheduleCall.enqueue(new Callback<SportSchedule>() {
                        @Override
                        public void onResponse(Call<SportSchedule> call, Response<SportSchedule> response) {
                            Toast toast = Toast.makeText(getContext(),
                                    "Sport schema notifications changed",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }

                        @Override
                        public void onFailure(Call<SportSchedule> call, Throwable t) {
                            sportSchedule.setIsActive(!isChecked);
                            switchActive.setChecked(!isChecked);

                            Toast toast = Toast.makeText(getContext(),
                                    getContext().getString(R.string.server_500_message),
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            }
        });

        switchActive.setChecked(sportSchedule.isActive());

        return convertView;
    }
}
