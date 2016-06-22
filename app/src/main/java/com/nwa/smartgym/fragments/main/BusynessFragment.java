package com.nwa.smartgym.fragments.main;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.nwa.smartgym.R;
import com.nwa.smartgym.api.interfaces.BusynessAPIInterface;
import com.nwa.smartgym.lib.DefaultPageAdapter;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rikvanderwerf on 11-5-16.
 */

public class BusynessFragment extends Fragment {

    private static TextView mBusynessDate;
    private static GraphView mBusynessGraph;
    private String myFormat = "yyyy-MM-dd";
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.ENGLISH);
    private Date today;
    private BusynessAPIInterface busynessAPIInterface;
    public static BusynessFragment newInstance() {
        BusynessFragment busynessFragment = new BusynessFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DefaultPageAdapter.TAB_NAME, "Busyness");
        busynessFragment.setArguments(bundle);
        return busynessFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        busynessAPIInterface = new BusynessAPIInterface(this);
        View rootView = inflater.inflate(R.layout.fragment_busyness, container, false);
        mBusynessGraph = (GraphView) rootView.findViewById(R.id.busyness_graph);
        mBusynessDate = (TextView) rootView.findViewById(R.id.busyness_date);
        today = calendar.getTime();
        mBusynessDate.setText(dateFormat.format(today));
        final DateTime maxDate = new DateTime().plusDays(5);
        mBusynessDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                datePicker.getDatePicker().setMaxDate(maxDate.getMillis());
                datePicker.show();
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
        Date busynessDate = calendar.getTime();
        if(busynessDate.before(today)) {
             busynessAPIInterface.past(busynessDateText);
        } else if (busynessDate.after(today)) {
            busynessAPIInterface.predict(busynessDateText);
        } else {
            busynessAPIInterface.today();
        }

    }

    public void updateGraph(JSONObject busynessJSON) throws JSONException {
        mBusynessGraph.removeAllSeries();
        // this is the correct ISO format the API uses.
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = calendar.getTime();
        date.setMinutes(0);
        date.setSeconds(0);

        DataPoint[] dataPointArray = new DataPoint[24];
        for(int i =0; i < dataPointArray.length; i++) {
            try {
                date.setHours(i);
                System.out.println(df.format(date));
                dataPointArray[i] = new DataPoint(i, (int) busynessJSON.get((df.format(date))));
            } catch (JSONException e) {
                dataPointArray[i] = new DataPoint(i, 0);
            }
        }
        for (DataPoint aDataPointArray : dataPointArray) {
            System.out.println(aDataPointArray);
        }
        System.out.println(dataPointArray.length);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPointArray);
        mBusynessGraph.addSeries(series);
        mBusynessGraph.getViewport().setXAxisBoundsManual(true);
        mBusynessGraph.getViewport().setMaxX(24);

    }


}
