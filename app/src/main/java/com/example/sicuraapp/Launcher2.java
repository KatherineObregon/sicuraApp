package com.example.sicuraapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Launcher2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.launcher2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher2);
    }
}