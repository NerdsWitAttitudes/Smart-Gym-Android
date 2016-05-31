package com.nwa.smartgym.lib;

import android.content.Context;
import android.widget.Toast;

import com.nwa.smartgym.R;

/**
 * Created by robin on 31-5-16.
 */
public class ErrorHelper {
    public static void raiseGenericError(Context context) {
        showToastError(context, context.getString(R.string.server_500_message));
    }

    public static void showToastError(Context context, String string) {
        Toast toast = Toast.makeText(context,
                context.getString(R.string.server_500_message),
                Toast.LENGTH_SHORT);
        toast.show();
    }
}
