package com.example.sicuraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class AnadirContacto extends AppCompatActivity {


    TextInputEditText numeroContacto;
    Button btnAnadirContacto;
    FirebaseFirestore mfirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_contacto);

        FirebaseApp.initializeApp(/*context=*/ this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());
        mfirestore= FirebaseFirestore.getInstance();

        numeroContacto= findViewById(R.id.inputCelularContacto);
        btnAnadirContacto=findViewById(R.id.btn_anadirContacto);

        btnAnadirContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String celular= numeroContacto.getText().toString();
                if(celular.isEmpty()){
                    Toast.makeText(AnadirContacto.this, "Ingrese un número válido.", Toast.LENGTH_SHORT).show();
                }else{
                    postContacto(celular);
                }
            }
        });

    }

    private void postContacto(String celular) {

        Map<String, Object> map = new HashMap<>();
        map.put("celular", celular);
        mfirestore.collection("pueba").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(AnadirContacto.this, "Creado exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AnadirContacto.this, "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}