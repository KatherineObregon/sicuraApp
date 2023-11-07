package com.example.sicuraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sicuraapp.Entities.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

public class MiPerfil extends AppCompatActivity {

    FirebaseFirestore db;

    Usuario usuario = new Usuario();
    ImageView imageView;


    TextView nombre;
    TextView correo;
    TextInputEditText celular ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        Intent intent = getIntent();

        usuario = (Usuario) intent.getSerializableExtra("usuarioActual");

        nombre = findViewById(R.id.tv_MiPerfilNombre);
        correo = findViewById(R.id.tv_MiperfilCorreo);
        celular = findViewById(R.id.inputMiPerfilCelular);

        usuario = (Usuario) intent.getSerializableExtra("usuarioActual");

        nombre.setText(usuario.getNombreApellidos());
        correo.setText(usuario.getCorreo());
        celular.setText(usuario.getCelular());


    }
}