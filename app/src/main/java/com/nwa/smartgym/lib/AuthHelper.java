package com.nwa.smartgym.lib;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.j256.ormlite.table.TableUtils;
import com.nwa.smartgym.R;
import com.nwa.smartgym.activities.Welcome;

/**
 * Created by robin on 9-6-16.
 */
public class AuthHelper {
    private Context context;

    public AuthHelper(Context context) {
        this.context = context;
    }

    public void logOut() {
        removeSecurityHeader();
        wipeLocalUserData();
        launchWelcome();
    }

    private void launchWelcome() {
        Intent intent = new Intent(context, Welcome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void removeSecurityHeader() {
        SharedPreferences secrets = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = secrets.edit();
        editor.remove(context.getString(R.string.auth_tkt));
        editor.apply();
    }

    private void wipeLocalUserData() {
        // Make sure the logged out user's data does not remain on the device
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.clearAll();
    }
}
