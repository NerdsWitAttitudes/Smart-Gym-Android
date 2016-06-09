package com.nwa.smartgym.lib;

import android.content.Context;
import android.content.SharedPreferences;

import com.nwa.smartgym.R;

import java.util.UUID;

/**
 * Created by robin on 24-5-16.
 */
public class SecretsHelper {
    private SharedPreferences secrets;
    private Context context;

    public SecretsHelper(Context context) {
        this.context = context;
        this.secrets = context.getSharedPreferences(context.getString(R.string.preference_file_key),
                                                    Context.MODE_PRIVATE);
    }

    public String getAuthToken() {
        return secrets.getString(context.getString(R.string.auth_tkt), null);
    }

    public void setCurrentUserID(UUID userID) {
        SharedPreferences.Editor editor = secrets.edit();
        editor.putString("currentUserID", userID.toString());
        editor.apply();
    }

    public UUID getCurrentUserID() {
        return UUID.fromString(secrets.getString("currentUserID", null));
    }
}
