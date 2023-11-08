package com.example.sicuraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sicuraapp.Entities.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AnadirContacto extends AppCompatActivity {


    TextInputEditText numeroContacto;
    Button btnAnadirContacto;
    FirebaseFirestore db;
    FirebaseUser currentUser;


    Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.vista_clara);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_contacto);

        FirebaseApp.initializeApp(/*context=*/ this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());
        db= FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        Intent intent = getIntent();
//
//        usuario = (Usuario) intent.getSerializableExtra("usuarioActual");

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

//        Map<String, Object> map = new HashMap<>();
//        map.put("celular", celular);
//        db.collection("pueba").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Toast.makeText(AnadirContacto.this, "Creado exitosamente", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(AnadirContacto.this, "Error al ingresar", Toast.LENGTH_SHORT).show();
//            }
//        });
        ArrayList<String> miArrayList = new ArrayList<>();
        miArrayList.add("Elemento 1");
        miArrayList.add("Elemento 2");
        miArrayList.add("Elemento 3");
        List<String> miLista = new ArrayList<>(miArrayList);
        Map<String, Object> data = new HashMap<>();
        data.put("contactosID", miLista);

        DocumentReference docRef= db.collection("user").document(currentUser.getUid());

        docRef.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("msg", "Funciona");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("msg", "no funciona");
            }
        });




    }
}