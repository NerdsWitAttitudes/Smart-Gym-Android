package com.nwa.smartgym.lib.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nwa.smartgym.R;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.api.SportScheduleAPI;
import com.nwa.smartgym.api.callbacks.Callback;
import com.nwa.smartgym.lib.MessageHelper;
import com.nwa.smartgym.lib.SecretsHelper;
import com.nwa.smartgym.models.SportSchedule;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
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
        TextView tvDaysOfWeek = (TextView) convertView.findViewById(R.id.tvDaysOfWeek);
        final Switch switchActive = (Switch) convertView.findViewById(R.id.switchActive);
        ImageView delete = (ImageView) convertView.findViewById(R.id.delete_sport_schedule);

        final SportScheduleAPI smartGymService = ServiceGenerator.createSmartGymService(SportScheduleAPI.class, new SecretsHelper(getContext()).getAuthToken());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<SportSchedule> sportScheduleCall = smartGymService.deleteSportSchedule(sportSchedule.getId());
                sportScheduleCall.enqueue(new Callback<SportSchedule>(getContext()) {
                    @Override
                    public void onResponse(Call<SportSchedule> call, Response<SportSchedule> response) {
                        super.onResponse(call, response);
                    }

                    @Override
                    public void onFailure(Call<SportSchedule> call, Throwable t) {
                        super.onFailure(call, t);
                    }
                });
            }
        });

        tvName.setText(sportSchedule.getName());

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm");
        tvDateTime.setText(dateTimeFormatter.print(sportSchedule.getTime()));

        if (sportSchedule.getWeekdays() != null) {
            List<String> weekDays = new ArrayList<>();
            for (LocalDate.Property property : sportSchedule.getWeekdays()) {
                weekDays.add(property.getAsShortText());
            }

            tvDaysOfWeek.setText(TextUtils.join(", ", weekDays));
        }

        switchActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if (sportSchedule.isActive() != isChecked) {
                    sportSchedule.setIsActive(isChecked);

                    Call<SportSchedule> sportScheduleCall = smartGymService
                            .updateSportSchedule(sportSchedule.getId(), sportSchedule);

                    sportScheduleCall.enqueue(new Callback<SportSchedule>(getContext()) {
                        @Override
                        public void onResponse(Call<SportSchedule> call, Response<SportSchedule> response) {
                            super.onResponse(call, response);

                            if (response.code() == 200) {
                                Toast.makeText(
                                        getContext(),
                                        R.string.sport_schedule_changed,
                                        Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                sportSchedule.setIsActive(!isChecked);
                                MessageHelper.raiseGenericError(getContext());
                            }
                        }

                        @Override
                        public void onFailure(Call<SportSchedule> call, Throwable t) {
                            sportSchedule.setIsActive(!isChecked);
                            MessageHelper.raiseGenericError(getContext());
                        }
                    });
                }
            }
        });

        switchActive.setChecked(sportSchedule.isActive());

        return convertView;
    }
}
