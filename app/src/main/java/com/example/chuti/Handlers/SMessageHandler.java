package com.example.chuti.Handlers;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.chuti.R;

public class SMessageHandler {
    public static AlertDialog.Builder alertBox;
    static AlertDialog alertDialog;
    static Button btnSuccessOk;
    static TextView txtSuccessDesc;

    public static void SAlertDialog(final String title, String message, int icon,
                                       final Context context, final boolean redirectToPreviousScreen) {
        if (alertDialog != null && alertDialog.isShowing()) {
        } else {
            try {
                alertBox = new AlertDialog.Builder(context);
                alertBox.setMessage(message);
                alertBox.setTitle(title);
                alertBox.setIcon(icon);
                alertBox.setNeutralButton("Ok", (arg0, arg1) -> alertDialog.dismiss());
                alertDialog = alertBox.create();
                alertDialog.show();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public static void SConnectionFail(final String t,
                                           final Context context) {
        if (alertDialog != null && alertDialog.isShowing()) {
        } else {
            try {
                alertBox = new AlertDialog.Builder(context);
                alertBox.setMessage("Connection was closed. Please try again. " + t);
                alertBox.setTitle("Error: ");
                alertBox.setIcon(R.drawable.error_read_64);
                alertBox.setNeutralButton("Ok", (arg0, arg1) -> alertDialog.dismiss());
                alertDialog = alertBox.create();
                alertDialog.show();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public static void SAlertSuccess(String message, final Context context) {
        View viewSuccess = LayoutInflater.from(context).inflate(R.layout.message_dialog, null);
        btnSuccessOk = viewSuccess.findViewById(R.id.successDone);
        txtSuccessDesc = viewSuccess.findViewById(R.id.txtSuccessDesc);
        alertBox = new AlertDialog.Builder(context);

        alertBox.setView(viewSuccess);
        alertDialog = alertBox.create();

        btnSuccessOk.setOnClickListener(v -> alertDialog.dismiss());
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
        txtSuccessDesc.setText(message);
    }
    public static void SAlertError(String message, final Context context) {
        View viewError = LayoutInflater.from(context).inflate(R.layout.error_message_dialog, null);
        btnSuccessOk = viewError.findViewById(R.id.successDone);
        txtSuccessDesc = viewError.findViewById(R.id.txtSuccessDesc);
        alertBox = new AlertDialog.Builder(context);

        alertBox.setView(viewError);
        alertDialog = alertBox.create();

        btnSuccessOk.setOnClickListener(v -> alertDialog.dismiss());
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
        txtSuccessDesc.setText(message);
    }
}
