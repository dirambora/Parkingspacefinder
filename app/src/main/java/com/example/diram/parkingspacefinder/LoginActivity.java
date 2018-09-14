package com.example.diram.parkingspacefinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }


    public void login(View view){
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

        startActivity(intent);
    }
    public void CreateAccount(View view){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);

        startActivity(intent);
    }
}