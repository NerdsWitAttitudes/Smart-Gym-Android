package com.nwa.smartgym.lib;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.nwa.smartgym.R;

/**
 * Created by robin on 31-5-16.
 */
public class MessageHelper {
    public static void raiseGenericError(Context context) {
        showToastError(context, context.getString(R.string.server_500_message));
    }

    public static void showToastError(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showLongTermSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).show();
    }
}
