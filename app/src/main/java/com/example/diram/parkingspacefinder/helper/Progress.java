package com.example.diram.parkingspacefinder.helper;

import android.app.ProgressDialog;
import android.content.Context;

public class Progress {
    private ProgressDialog progressDialog;

    public Progress(Context context, String Title, String Message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(Title);
        progressDialog.setMessage(Message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void showDialog() {
        progressDialog.show();
    }

    public void dismissDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void setMessage(String message) {
        progressDialog.setMessage(message);
    }

}
