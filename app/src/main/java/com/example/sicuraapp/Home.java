package com.example.sicuraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.home);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}