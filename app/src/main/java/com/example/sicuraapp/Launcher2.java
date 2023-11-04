package com.example.sicuraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Launcher2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.launcher2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher2);
    }

    public void RegistroUsuario (View view){
        Intent intent= new Intent(Launcher2.this, RegistrarUsuario.class);
        startActivity(intent);
    }

    public void IniciarSesion (View view){
        Intent intent= new Intent(Launcher2.this, IniciarSesion.class);
        startActivity(intent);
    }
}