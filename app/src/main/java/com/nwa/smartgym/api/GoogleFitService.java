package com.nwa.smartgym.api;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.fitness.result.SessionReadResult;
import com.google.android.gms.fitness.result.SessionStopResult;
import com.nwa.smartgym.R;
import com.nwa.smartgym.api.callbacks.GoogleFitCallback;
import com.nwa.smartgym.lib.ErrorHelper;
import com.nwa.smartgym.models.CardioActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class GoogleFitService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final DataType[] DataTypes = { DataType.TYPE_CALORIES_EXPENDED };

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

    public void addCardioActivity(CardioActivity cardioActivity, final String fitnessActivity) {
        Call<CardioActivity> session = smartGymService.createSession(cardioActivity);
        session.enqueue(new com.nwa.smartgym.api.callbacks.Callback<CardioActivity>(context) {
            @Override
            public void onResponse(Call<CardioActivity> call, Response<CardioActivity> response) {
                super.onResponse(call, response);

                if (response.code() == 201) {
                    currentCardioActivity = response.body();
                    startGoogleFitSession(currentCardioActivity, fitnessActivity);
                    ErrorHelper.showToastError(context, "Started " + fitnessActivity + " activity successfully");
                }
            }
        });
    }

    private void startGoogleFitSession(CardioActivity cardioActivity, String activityType) {
        if (currentSession == null) {
            currentSession = new Session.Builder()
                    .setIdentifier(String.valueOf(cardioActivity.getId()))
                    .setStartTime(cardioActivity.getStartDate().getMillis(), TimeUnit.MILLISECONDS)
                    .setActivity(activityType)
                    .build();

            PendingResult<Status> pendingResult = Fitness.SessionsApi.startSession(googleApiClient, currentSession);

            pendingResult.setResultCallback(new GoogleFitCallback<Status>(context) {
                @Override
                public void onResult(@NonNull Status result) {
                    super.onResult(result);
                }
            });
        }
    }

    public void stopSession() {
        if (currentCardioActivity == null || currentSession == null) {
            ErrorHelper.raiseGenericError(context);
            return;
        }

        PendingResult<SessionStopResult> pendingResult = Fitness.SessionsApi.stopSession(googleApiClient, currentSession.getIdentifier());

        pendingResult.setResultCallback(new GoogleFitCallback<SessionStopResult>(context) {
            @Override
            public void onResult(@NonNull SessionStopResult result) {
                super.onResult(result);

                for (DataType dataType : DataTypes) {
                    try {
                        setSessionResult(dataType);
                    } catch (Exception e) {
                        ErrorHelper.raiseGenericError(context);
                    }
                }
            }
        });
    }

    private void setSessionResult(DataType dataType) {
        final SessionReadRequest sessionReadRequest = new SessionReadRequest.Builder()
                .setSessionId(currentSession.getIdentifier())
                .setTimeInterval(
                        currentSession.getStartTime(TimeUnit.MILLISECONDS),
                        System.currentTimeMillis(),
                        TimeUnit.MILLISECONDS)
                .read(dataType)
                .build();

        PendingResult<SessionReadResult> sessionReadResultPendingResult = Fitness.SessionsApi.readSession(googleApiClient, sessionReadRequest);

        sessionReadResultPendingResult.setResultCallback(new GoogleFitCallback<SessionReadResult>(context) {
            @Override
            public void onResult(@NonNull SessionReadResult sessionReadResult) {
                super.onResult(sessionReadResult);

                if (sessionReadResult.getStatus().isSuccess()) {
                    for (Session session : sessionReadResult.getSessions()) {
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
                ErrorHelper.showToastError(context, context.getResources().getString(R.string.cardio_activity_stopped_successfully));
            }
        });
    }
}