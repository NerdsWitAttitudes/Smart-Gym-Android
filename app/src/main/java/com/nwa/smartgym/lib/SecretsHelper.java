package com.nwa.smartgym.lib;

import android.content.Context;
import android.content.SharedPreferences;

import com.nwa.smartgym.R;

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
}
