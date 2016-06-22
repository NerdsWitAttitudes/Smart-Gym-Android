package com.nwa.smartgym.fragments.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.nwa.smartgym.R;
import com.nwa.smartgym.api.CardioActivityAPI;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.api.callbacks.Callback;
import com.nwa.smartgym.lib.DefaultPageAdapter;
import com.nwa.smartgym.lib.MessageHelper;
import com.nwa.smartgym.lib.SecretsHelper;

import org.joda.time.Duration;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CardioActivity extends Fragment {

    private CardioActivityAPI cardioActivityAPI;
    private List<com.nwa.smartgym.models.CardioActivity> sessions;
    private GraphView cardioActivityGaph;
    private Spinner spinner;

    public static CardioActivity newInstance() {
        CardioActivity cardioActivity = new CardioActivity();
        Bundle bundle = new Bundle();
        bundle.putString(DefaultPageAdapter.TAB_NAME, "Cardio progression");
        cardioActivity.setArguments(bundle);
        return cardioActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cardio_activity, container, false);

        spinner = (Spinner) rootView.findViewById(R.id.cardio_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.graph_types, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        cardioActivityAPI = ServiceGenerator.createSmartGymService(CardioActivityAPI.class, new SecretsHelper(getContext()).getAuthToken());
        cardioActivityGaph = (GraphView) rootView.findViewById(R.id.cardio_activity_graph);

        setSessions();

        return rootView;
    }

    private void setSessions() {
        final Call<List<com.nwa.smartgym.models.CardioActivity>> sessionsCall = cardioActivityAPI.getSessions();

        sessionsCall.enqueue(new Callback<List<com.nwa.smartgym.models.CardioActivity>>(getContext()) {
            @Override
            public void onResponse(Call<List<com.nwa.smartgym.models.CardioActivity>> call, Response<List<com.nwa.smartgym.models.CardioActivity>> response) {
                super.onResponse(call, response);

                if (response.body() == null) {
                    MessageHelper.raiseGenericError(getContext());
                } else {
                    sessions = response.body();
                    plotGraph();

                    spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            plotGraph();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<com.nwa.smartgym.models.CardioActivity>> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    private void plotGraph() {
        List<com.nwa.smartgym.models.CardioActivity> filteredSessions = getFilteredSessions();

        Log.i("NWA", String.valueOf(filteredSessions.size()) + " : " + filteredSessions);

        cardioActivityGaph.removeAllSeries();

        DataPoint[] data;
        if (spinner.getSelectedItem().toString().equals("Calories")) {
            data = getCaloriesData(filteredSessions);
        } else {
            data = getDurationData(filteredSessions);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(data);
        cardioActivityGaph.addSeries(series);
        cardioActivityGaph.getViewport().setMinY(0);
    }

    private DataPoint[] getCaloriesData(List<com.nwa.smartgym.models.CardioActivity> filteredSessions) {
        DataPoint[] data = new DataPoint[filteredSessions.size()];
        for (int i = 0; i < data.length; i++) {
            Log.i("NWA", String.valueOf(filteredSessions.get(i).getCalories()));
            data[i] = new DataPoint(i + 1, filteredSessions.get(i).getCalories());
        }

        return data;
    }

    private DataPoint[] getDurationData(List<com.nwa.smartgym.models.CardioActivity> filteredSessions) {
        DataPoint[] data = new DataPoint[filteredSessions.size()];
        for (int i = 0; i < data.length; i++) {
            com.nwa.smartgym.models.CardioActivity session = filteredSessions.get(i);
            Duration duration = new Duration(session.getStartDate(), session.getEndDate());

            data[i] = new DataPoint(i + 1, duration.getStandardMinutes());
        }

        return data;
    }

    private List<com.nwa.smartgym.models.CardioActivity> getFilteredSessions() {
        Collections.sort(sessions, new Comparator<com.nwa.smartgym.models.CardioActivity>() {
            @Override
            public int compare(com.nwa.smartgym.models.CardioActivity lhs, com.nwa.smartgym.models.CardioActivity rhs) {
                return lhs.getStartDate().compareTo(rhs.getStartDate());
            }
        });

        return sessions;
    }
}
