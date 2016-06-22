package com.nwa.smartgym.fragments.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.nwa.smartgym.R;
import com.nwa.smartgym.api.CardioActivityAPI;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.api.callbacks.Callback;
import com.nwa.smartgym.lib.DefaultPageAdapter;
import com.nwa.smartgym.lib.ErrorHelper;
import com.nwa.smartgym.lib.SecretsHelper;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CardioActivity extends Fragment {

    private CardioActivityAPI cardioActivityAPI;
    private List<com.nwa.smartgym.models.CardioActivity> sessions;
    private GraphView cardioActivityGaph;

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
                    ErrorHelper.raiseGenericError(getContext());
                } else {
                    sessions = response.body();
                    plotGraph();
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

        DataPoint[] data = new DataPoint[filteredSessions.size()];
        for (int i = 0; i < data.length; i++) {
            com.nwa.smartgym.models.CardioActivity session = filteredSessions.get(i);

            Date date = new Date(session.getStartDate().getMillis());

            data[i] = new DataPoint(date, session.getCalories());
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(data);
        cardioActivityGaph.addSeries(series);
    }

    private List<com.nwa.smartgym.models.CardioActivity> getFilteredSessions() {
        return sessions;
    }
}
