package com.example.diram.parkingspacefinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.diram.parkingspacefinder.helper.Progress;
import com.example.diram.parkingspacefinder.ini.Constant;
import com.example.diram.parkingspacefinder.models.Profile;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {
    private Context context;
    CallbackManager callbackManager;


    private static final String EMAIL = "email";

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        if (Constant.getUserProfile(context).isLoggedIn()) {
            goHome();
            finish();
        }









        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton =  findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });





    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);

    }




    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e("error", "printHashKey() Hash Key:" + hashKey);
            }
        } catch (Exception e) {
            Log.e("error", "printHashKey()", e);
        }
    }

    public void login(View view) {
//        check for null and return
        printHashKey();

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
        final Progress loading = new Progress(this, "", "Loading please wait ...");
        loading.showDialog();
        Ion.with(this)
                .load("https://dirambora12.000webhostapp.com/parking/v1/Api.php")
                .setBodyParameter("username", username.getText().toString())
                .setBodyParameter("password", password.getText().toString())
                .setBodyParameter("action", "login")
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
                                    setUserProfile(resultBack.getJSONObject("profile"));
                                    goHome();
                                } else {
                                    Toast.makeText(context, "Password or username is invalid", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e1) {
                                Toast.makeText(context, "Check network info" + e1.getMessage(), Toast.LENGTH_LONG).show();
                                e1.printStackTrace();
                            }
                        } else {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void CreateAccount(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);

        startActivity(intent);
    }

    private void setUserProfile(JSONObject profile) {
        try {
            profile.accumulate("loggedIn",true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = getSharedPreferences("parking", MODE_PRIVATE).edit();
        editor.putString("profile", profile.toString());
        editor.apply();
    }







    private void goHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


}