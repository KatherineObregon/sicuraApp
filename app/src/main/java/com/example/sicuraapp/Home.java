package com.example.sicuraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sicuraapp.Entities.Alerta;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Home extends AppCompatActivity {

    Button logout;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String idUsuarioActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.home);
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_home);
        logout = findViewById(R.id.btn_logout);


        mAuth= FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();


    }

    public void botonAlerta(View view){
        idUsuarioActual= mAuth.getCurrentUser().getUid();

        // Obtiene la fecha y hora actual
        Date currentDate = new Date();

        // Define el formato deseado para la fecha y hora
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // Formatea la fecha y hora actual como un String separado por un guion
        String formattedDateTime = dateFormat.format(currentDate);
        String[] dateTimeParts = formattedDateTime.split(" ");
        String fechaHora= dateTimeParts[0].replace("-", "") + "-" + dateTimeParts[1].replace(":", "");

        String idAlerta = idUsuarioActual+fechaHora;
        Map<String, Object> map = new HashMap<>();
        map.put("id", idAlerta);
        map.put("fecha", dateTimeParts[0]);
        map.put("hora", dateTimeParts[1]);
        map.put("idUsuario", idUsuarioActual);

        db.collection("alerta").document(idAlerta).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
//                finish();
//                startActivity(new Intent(Home.this, Home.class));
                Toast.makeText(Home.this, "Alerta creada", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Home.this, "Alerta no creada :(", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void irAMasOpciones (View view){
        Intent intent= new Intent(Home.this, MasOpciones.class);
        startActivity(intent);
    }
    public void logout(View view){
        mAuth.signOut();
        finish();
        startActivity(new Intent(Home.this, MainActivity.class));
    }

}