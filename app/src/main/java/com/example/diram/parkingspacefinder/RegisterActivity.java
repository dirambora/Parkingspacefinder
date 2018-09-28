package com.example.diram.parkingspacefinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diram.parkingspacefinder.helper.Progress;
import com.example.diram.parkingspacefinder.ini.Constant;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

/**
 * Created by amardeep on 10/24/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView textViewLogin = (TextView) findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public void createAccount(View view) {
//        check for null and return

        EditText username = findViewById(R.id.editTextEmail);
        EditText email = findViewById(R.id.editTextEmail);
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
        final Progress loading = new Progress(this, "", "Loading please wait ...");
        loading.showDialog();
        Ion.with(this)
                .load("https://dirambora97.000webhostapp.com/parking/v1/Api.php")
                .setBodyParameter("username", username.getText().toString())
                .setBodyParameter("email", email.getText().toString())
                .setBodyParameter("mobileNumber", "")
                .setBodyParameter("password", password.getText().toString())
                .setBodyParameter("action", "createUser")
                .setBodyParameter("location", "")
                .setBodyParameter("fullName", "")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        loading.dismissDialog();
                        if (e == null) {
                            Log.e("Server=>", result);
                            try {
                                JSONObject resultBack = new JSONObject(result);
                                if (resultBack.getBoolean("status")) {
                                    Constant.setUserProfile(RegisterActivity.this,resultBack.getJSONObject("profile"));
                                    goHome();
                                } else {
                                    Toast.makeText(RegisterActivity.this, resultBack.getString("message"),Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e1) {
                                Toast.makeText(RegisterActivity.this, "Check network info" + e1.getMessage(), Toast.LENGTH_LONG).show();
                                e1.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
    private void goHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}