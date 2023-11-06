package com.example.sicuraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MasOpciones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_opciones);
    }

    public void irContactosConfianza (View view){
        Intent intent= new Intent(MasOpciones.this, ContactosConfianza.class);
        startActivity(intent);
    }
}