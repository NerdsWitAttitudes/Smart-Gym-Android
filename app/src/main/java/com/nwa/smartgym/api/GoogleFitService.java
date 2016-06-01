package com.nwa.smartgym.api;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessActivities;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.fitness.result.SessionReadResult;
import com.google.android.gms.fitness.result.SessionStopResult;
import com.nwa.smartgym.models.CardioActivity;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleFitService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private CardioActivityAPI smartGymService;

    private CardioActivity currentCardioActivity;
    private Session currentSession;

    public GoogleFitService(Context context, CardioActivityAPI smartGymService) {
        this.googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Fitness.SESSIONS_API)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .build();
        this.smartGymService = smartGymService;
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void getCurrentSession(final String activityType) {
        Call<List<CardioActivity>> sessionsCall = smartGymService.getSessions();
        sessionsCall.enqueue(new Callback<List<CardioActivity>>() {
            @Override
            public void onResponse(Call<List<CardioActivity>> call, Response<List<CardioActivity>> response) {
                Log.i("NWA", "get sessions ");

                for (CardioActivity cardioActivity : response.body()) {
                    if (cardioActivity.isActive()) {
                        currentCardioActivity = cardioActivity;
                        startSession(currentCardioActivity, activityType);
                        return;
                    }
                }

            }

            @Override
            public void onFailure(Call<List<CardioActivity>> call, Throwable t) {
                Log.i("NWA", "FAIL google fit service");
            }
        });
    }

    private void startSession(CardioActivity cardioActivity, String activityType) {
        if (currentSession == null) {
            currentSession = new Session.Builder()
                    .setIdentifier(String.valueOf(cardioActivity.getId()))
                    .setStartTime(cardioActivity.getStartDate().getMillis(), TimeUnit.MILLISECONDS)
                    .setActivity(FitnessActivities.RUNNING_TREADMILL)
                    .build();

            PendingResult<Status> pendingResult = Fitness.SessionsApi.startSession(googleApiClient, currentSession);

            pendingResult.setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()) {
                        Log.i("NWA", "JAWEL GESTART");
                    } else {
                        Log.i("NWA", "NEE NEE GEFAALT" + " " + status.getStatusMessage());
                    }
                }
            });
        }
    }

    public void stopSession() {
        if (currentCardioActivity == null || currentSession == null) {
            Log.i("NWA", "Er was niks actief ofzo");
            return;
        }

        PendingResult<SessionStopResult> pendingResult = Fitness.SessionsApi.stopSession(googleApiClient, currentSession.getIdentifier());

        pendingResult.setResultCallback(new ResultCallback<SessionStopResult>() {
            @Override
            public void onResult(@NonNull SessionStopResult sessionStopResult) {
                if (sessionStopResult.getStatus().isSuccess()) {
                    String message = "JAH JAH GOEDZO GESTOPT";
                    Log.i("NWA", message);

                    writeSessionData();
                } else {
                    String message = "AND YOU FAILED";
                    Log.i("NWA", message + " " + sessionStopResult.getStatus().getStatusMessage());
                }
            }
        });
    }

    private void writeSessionData() {
        List<DataType> dataTypes = Arrays.asList(DataType.TYPE_CALORIES_EXPENDED);

        for (DataType dataType : dataTypes) {
            writeSessionDataType(dataType);
        }
    }

    private void writeSessionDataType(DataType dataType) {
        final SessionReadRequest sessionReadRequest = new SessionReadRequest.Builder()
                .setSessionId(currentSession.getIdentifier())
                .read(dataType)
                .build();

        PendingResult<SessionReadResult> sessionReadResultPendingResult = Fitness.SessionsApi.readSession(googleApiClient, sessionReadRequest);

        sessionReadResultPendingResult.setResultCallback(new ResultCallback<SessionReadResult>() {
            @Override
            public void onResult(@NonNull SessionReadResult sessionReadResult) {
                // veel dingen hier
            }
        });
    }
}