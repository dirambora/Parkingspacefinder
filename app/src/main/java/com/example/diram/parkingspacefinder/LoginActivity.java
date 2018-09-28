package com.example.diram.parkingspacefinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diram.parkingspacefinder.helper.Progress;
import com.example.diram.parkingspacefinder.ini.Constant;
import com.example.diram.parkingspacefinder.models.Profile;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        if (Constant.getUserProfile(context).isLoggedIn()) {
            goHome();
        }
    }


    public void login(View view) {
//        check for null and return

        EditText username = findViewById(R.id.editTextEmail);
        EditText password = findViewById(R.id.editTextPassword);

       /* if (username.getText().toString().isEmpty()) {
            username.setError("Username is Blank");
            username.requestFocus();
            Toast.makeText(this, "", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.getText().toString().isEmpty()) {
            password.setError("Password is Blank");
            password.requestFocus();
            Toast.makeText(this, "Password is Blank", Toast.LENGTH_LONG).show();
            return;
        }*/
        final Progress loading = new Progress(this,"","Loading please wait ...");
        loading.showDialog();
        Ion.with(this)
                .load("https://dirambora97.000webhostapp.com/parking/v1/Api.php")
                .setBodyParameter("username", username.getText().toString())
                .setBodyParameter("password", password.getText().toString())
                .setBodyParameter("action", "login")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        loading.dismissDialog();
                        if (e == null) {
                            Log.e("Server=>",result);
                            try {
                                JSONObject resultBack = new JSONObject(result);
                                if (resultBack.getBoolean("status")) {
                                    setUserProfile(resultBack.getJSONObject("profile"));
                                    goHome();
                                } else {
                                    Toast.makeText(context, "Password or username is invalid", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e1) {
                                Toast.makeText(context, "Check network info"+e1.getMessage(), Toast.LENGTH_LONG).show();
                                e1.printStackTrace();
                            }
                        } else {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void CreateAccount(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);

        startActivity(intent);
    }

    private void setUserProfile(JSONObject profile) {
        SharedPreferences.Editor editor = getSharedPreferences("parking", MODE_PRIVATE).edit();
        editor.putString("profile", profile.toString());
        editor.apply();
    }


    private void goHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


}