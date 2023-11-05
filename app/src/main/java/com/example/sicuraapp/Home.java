package com.example.sicuraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.home);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void irAMasOpciones (View view){
        Intent intent= new Intent(Home.this, MasOpciones.class);
        startActivity(intent);
    }
}