package com.example.diram.cityparker;

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
        Intent intent = new Intent(com.example.diram.cityparker.LoginActivity.this, com.example.diram.cityparker.HomeActivity.class);

        startActivity(intent);
    }
    public void CreateAccount(View view){
        Intent intent = new Intent(com.example.diram.cityparker.LoginActivity.this, com.example.diram.cityparker.RegisterActivity.class);

        startActivity(intent);
    }
}