package com.nwa.smartgym.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;

public class GoogleFitService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private ProgressDialog progressGoogleFit;
    private boolean fitApiReady;

    private String tag = "NWA";

    public GoogleFitService(Context context) {
        fitApiReady = false;
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Fitness.SESSIONS_API)
                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .enableAutoManage((AppCompatActivity) context, this)
                .build();
    }

    public void startSession(String runningTreadmill) {
        if (!googleApiClient.isConnected() || googleApiClient.isConnecting()) {
            Log.i(tag, "Google Fit was not connected");
            return;
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        fitApiReady = true;
        Log.i("NWA", "onConnected .!~");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("NWA", "onconnectionsuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("NWA", "onConnectionFailed ..  . " +  connectionResult);
    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }
}
