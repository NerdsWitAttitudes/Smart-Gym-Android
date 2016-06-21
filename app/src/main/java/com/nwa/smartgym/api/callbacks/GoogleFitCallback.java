package com.nwa.smartgym.api.callbacks;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.nwa.smartgym.lib.ErrorHelper;

public abstract class GoogleFitCallback<S extends Result> implements ResultCallback<S> {
    private static Context context;

    public GoogleFitCallback(Context context) {
        GoogleFitCallback.context = context;
    }

    @Override
    public void onResult(@NonNull S result) {
        if (!result.getStatus().isSuccess()) {
            ErrorHelper.raiseGenericError(context);
        }
    }
}
