package com.example.sicuraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RegistrarUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.vista_clara);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
    }
}