package com.nwa.smartgym.fragments.main;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.nwa.smartgym.R;
import com.nwa.smartgym.api.AuthAPI;
import com.nwa.smartgym.api.BusynessAPI;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.models.HTTPResponse;
import com.nwa.smartgym.models.Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

/**
 * Created by rikvanderwerf on 11-5-16.
 */

public class BusynessFragment extends Fragment {

    private static TextView mBusynessDate;
    private static GraphView mBusynessGraph;
    private String myFormat = "yyyy-MM-dd";
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.ENGLISH);
    private BusynessAPI busynessService = ServiceGenerator.createSmartGymService(BusynessAPI.class);
    private Date today;
    public static BusynessFragment newInstance() {
        return new BusynessFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_busyness, container, false);
        mBusynessGraph = (GraphView) rootView.findViewById(R.id.busyness_graph);
        mBusynessDate = (TextView) rootView.findViewById(R.id.busyness_date);
        today = calendar.getTime();
        mBusynessDate.setText(dateFormat.format(today));
        mBusynessDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        getBusynessData();
        return rootView;
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mBusynessDate.setText(dateFormat.format(calendar.getTime()));
            getBusynessData();
        }

    };

    private void getBusynessData() {
        String busynessDateText = mBusynessDate.getText().toString();
        System.out.println(busynessDateText);
        Call<ResponseBody> call;
        Date busynessDate = calendar.getTime();
        if(busynessDate.before(today)) {
            call = busynessService.past(busynessDateText);
        } else if (busynessDate.after(today)) {
            call = busynessService.predict(busynessDateText);
        } else {
            call = busynessService.today();
        }

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    updateGraph(new JSONObject(response.body().string()));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                if (response.code() != 200) {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//             TODO failure ding maken
            }



        });

    }

    private void updateGraph(JSONObject busynessJSON) {
        System.out.println(busynessJSON.length());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        DataPoint[] dataPointArray = new DataPoint[24];
        System.out.println("oi");
        for(int i =0; i < 24; i++) {
            try {
                dataPointArray[i] = new DataPoint(i, (int) busynessJSON.get(Integer.toString(i)));
            } catch (JSONException e) {
                dataPointArray[i] = new DataPoint(i, 0);
            }
        }

        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(dataPointArray);
        mBusynessGraph.addSeries(series);

        series.setSpacing(50);


    }


}
