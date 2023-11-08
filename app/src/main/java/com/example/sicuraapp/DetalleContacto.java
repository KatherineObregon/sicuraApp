package com.example.sicuraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sicuraapp.Entities.Usuario;

public class DetalleContacto extends AppCompatActivity {


    Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_contacto);


        Intent intent = getIntent();

        TextView nombreTV= findViewById(R.id.tv_detalle_contacto_nombre);
        TextView correoTV= findViewById(R.id.tv_detalle_contacto_correo);
        TextView celularTV = findViewById(R.id.tv_detalle_contacto_celular);

        ImageView imageView = findViewById(R.id.imageViewContacto);
        usuario = (Usuario) intent.getSerializableExtra("usuarioDetalle");
        if(usuario.getFotoUrl()!=null){
            Glide.with(DetalleContacto.this).load(usuario.getFotoUrl()).into(imageView);
        }
        nombreTV.setText(usuario.getNombreApellidos());
        correoTV.setText(usuario.getCorreo());
        celularTV.setText(usuario.getCelular());

    }
}