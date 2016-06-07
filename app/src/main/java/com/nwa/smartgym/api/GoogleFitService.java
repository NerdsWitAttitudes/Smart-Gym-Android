package com.nwa.smartgym.api;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.fitness.result.SessionReadResult;
import com.google.android.gms.fitness.result.SessionStopResult;
import com.nwa.smartgym.models.CardioActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleFitService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final Context context;
    private GoogleApiClient googleApiClient;
    private CardioActivityAPI smartGymService;

    private CardioActivity currentCardioActivity;
    private Session currentSession;

    private Map<Field, Value> sessionResult;

    public GoogleFitService(Context context, CardioActivityAPI smartGymService) {
        this.googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Fitness.SESSIONS_API)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .enableAutoManage((AppCompatActivity) context, this)
                .build();
        this.context = context;
        this.smartGymService = smartGymService;
        this.sessionResult = new HashMap<>();
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

    public void startSession(CardioActivity cardioActivity) {
        Call<CardioActivity> session = smartGymService.createSession(cardioActivity);
        session.enqueue(new Callback<CardioActivity>() {
            @Override
            public void onResponse(Call<CardioActivity> call, Response<CardioActivity> response) {
                Log.i("NWA", " added cardio session to db code: " + response.code());

                if (response.code() == 201) {
                    CardioActivity cardioActivityResult = response.body();
                    startSession(cardioActivityResult, FitnessActivities.RUNNING_TREADMILL);
                }
            }

            @Override
            public void onFailure(Call<CardioActivity> call, Throwable t) {
                Log.i("NWA", " failed!!! to add session to db");
            }
        });
    }

    public void getCurrentSession(final String activityType) {
        Call<List<CardioActivity>> sessionsCall = smartGymService.getSessions();
        sessionsCall.enqueue(new com.nwa.smartgym.api.callbacks.Callback<List<CardioActivity>>(context) {
            @Override
            public void onResponse(Call<List<CardioActivity>> call, Response<List<CardioActivity>> response) {
                super.onResponse(call, response);
                Log.i("NWA", "get sessions ");

                List<CardioActivity> body = response.body();
                if (body != null) {
                    for (CardioActivity cardioActivity : body) {
                        if (cardioActivity.isActive()) {
                            currentCardioActivity = cardioActivity;
                            startSession(currentCardioActivity, activityType);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CardioActivity>> call, Throwable t) {
                super.onFailure(call, t);
                Log.i("NWA", "FAIL active session getting");
            }
        });
    }

    private void startSession(CardioActivity cardioActivity, String activityType) {
        if (currentSession == null) {
            currentSession = new Session.Builder()
                    .setIdentifier(String.valueOf(cardioActivity.getId()))
                    .setStartTime(cardioActivity.getStartDate().getMillis(), TimeUnit.MILLISECONDS)
                    .setActivity(activityType)
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

                    writeSessionDataType(DataType.TYPE_CALORIES_EXPENDED);
                } else {
                    String message = "AND YOU FAILED";
                    Log.i("NWA", message + " " + sessionStopResult.getStatus().getStatusMessage());
                }
            }
        });
    }

    private void writeSessionDataType(DataType dataType) {
        final SessionReadRequest sessionReadRequest = new SessionReadRequest.Builder()
                .setSessionId(currentSession.getIdentifier())
                .setTimeInterval(
                        currentSession.getStartTime(TimeUnit.MILLISECONDS),
                        System.currentTimeMillis(),
                        TimeUnit.MILLISECONDS)
                .read(dataType)
                .build();

        PendingResult<SessionReadResult> sessionReadResultPendingResult = Fitness.SessionsApi.readSession(googleApiClient, sessionReadRequest);

        sessionReadResultPendingResult.setResultCallback(new ResultCallback<SessionReadResult>() {
            @Override
            public void onResult(@NonNull SessionReadResult sessionReadResult) {
                if (sessionReadResult.getStatus().isSuccess()) {

                    List<Session> sessions = sessionReadResult.getSessions();
                    for (Session session : sessions) {
                        for (DataSet dataSet : sessionReadResult.getDataSet(session)) {
                            for (DataPoint dataPoint : dataSet.getDataPoints()) {
                                for (Field field : dataPoint.getDataType().getFields()) {
                                    Value value = dataPoint.getValue(field);
                                    sessionResult.put(field, value);
                                }
                            }
                        }
                    }

                    saveSession(currentCardioActivity, sessionResult);
                }
            }
        });
    }

    private void saveSession(CardioActivity currentCardioActivity, Map<Field, Value> sessionResult) {
        for (Field field : sessionResult.keySet()) {
            if (field.getName().equals(Field.FIELD_CALORIES.getName())) {
                currentCardioActivity.setCalories((double) sessionResult.get(field).asFloat());
            } else if (field.getName().equals(Field.FIELD_DISTANCE.getName())) {
                currentCardioActivity.setDistance((double) sessionResult.get(field).asFloat());
            } else if (field.getName().equals(Field.FIELD_SPEED.getName())) {
                currentCardioActivity.setSpeed((double) sessionResult.get(field).asFloat());
            }
        }

        Call<CardioActivity> cardioActivityCall = smartGymService.endSession(currentCardioActivity.getId(), currentCardioActivity);
        cardioActivityCall.enqueue(new com.nwa.smartgym.api.callbacks.Callback<CardioActivity>(context) {
            @Override
            public void onResponse(Call<CardioActivity> call, Response<CardioActivity> response) {
                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<CardioActivity> call, Throwable t) {
                super.onFailure(call, t);
                Log.i("NWA", "failed sving to api");
            }
        });
    }
}