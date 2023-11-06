package com.example.sicuraapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.sicuraapp.Adapters.ListaContactosAdapter;

public class ContactosConfianza extends AppCompatActivity {

    ListaContactosAdapter listaContactosAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos_confianza);

//        RecyclerView recyclerView = findViewById(R.id.recyclerview_contactos);
//        listaContactosAdapter = new ListaContactosAdapter(ContactosConfianza.this,firabeseUsuariosCoordinadores);
//        recyclerView.setAdapter(listaContactosAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(ContactosConfianza.this));
    }

    public void irAnadirContactos (View view){
        Intent intent= new Intent(ContactosConfianza.this, AnadirContacto.class);
        startActivity(intent);
    }
}