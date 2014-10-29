package com.taxifare.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.taxifare.R;

/**
 * Created by test on 2014-10-20.
 */
public class DirectionAPIsErrorHandler {
    private String errorType;
    private Context context;

    public DirectionAPIsErrorHandler(Context context, String errorType) {
        this.errorType = errorType;
        this.context = context;
    }

    // TODO: Interface maybe???
    public void showError(){
        String message = context.getString(R.string.unknown_direction_api_error);

        if (errorType.equalsIgnoreCase("ZERO_RESULTS")) {
            message = context.getString(R.string.no_direction_found_error);
        }

        if (errorType.equalsIgnoreCase("MAX_WAYPOINTS_EXCEEDED")) {
            message = context.getString(R.string.max_waypoints_exceeded);
        }

        if (errorType.equalsIgnoreCase("INVALID_REQUEST")) {
            message = context.getString(R.string.invalid_request_message);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();

        alertDialog.show();
    }
}
