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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AnadirContacto extends AppCompatActivity {


    TextInputEditText numeroContacto;
    Button btnAnadirContacto;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    Usuario nuevoContactoUser;

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


        db.collection("user").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Boolean siHayContacto=false;
                if(task.isSuccessful()){
                    Log.d("msg-id", "entra a successful ");
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        nuevoContactoUser = doc.toObject(Usuario.class);
                        String celUsuario = nuevoContactoUser.getCelular();
                        Log.d("msg-id", "celUsuario: "+celUsuario);
                        if(celUsuario.equalsIgnoreCase(celular)){
                            siHayContacto=true;
                            Log.d("msg-id", "si entra al if del cel");
                            finish();
                            anadirContactoPorID(nuevoContactoUser.getId());
                        }

                    }
                    if (!siHayContacto){
                        Toast.makeText(AnadirContacto.this, "El número ingresado no cuenta con un usuario en la aplicación.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               Log.d("msg-id", "error al cargar");
            }
        });




    }

    public void anadirContactoPorID(String id){
        Log.d("msg-id", "Nuevo contacto: "+nuevoContactoUser.getId());
        DocumentReference docRef= db.collection("user").document(currentUser.getUid());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    List<String> miListaContactos = (List<String>) documentSnapshot.get("contactosID");
                    if(miListaContactos==null){

                        ArrayList<String> miArrayListNueva = new ArrayList<>();
                        miArrayListNueva.add(id);
                        List<String> miListaNueva = new ArrayList<>(miArrayListNueva);

                        Map<String, Object> data = new HashMap<>();
                        //data.put("contactosID", miListaNueva);

                        docRef.update("contactosID", miListaNueva)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Éxito al almacenar el ArrayList en Firestore
                                        Toast.makeText(AnadirContacto.this, "Se agregó el primer contacto de confianza.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Error al almacenar el ArrayList en Firestore
                                        Log.w("TAG", "Error al almacenar el ArrayList en Firestore", e);
                                    }
                                });

                    }
                    else if(miListaContactos!=null && miListaContactos.size()<5){
                        //String nuevoContacto = celular;
                        String nuevoContacto= id;
                        miListaContactos.add(nuevoContacto);
                        docRef.update("contactosID", miListaContactos).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AnadirContacto.this, "Contacto agreagado correctamente.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AnadirContacto.this, "Error al agregar contacto.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Log.d("msg", "No hay lista de contactos aun");
                        Toast.makeText(AnadirContacto.this, "Superaste el número máximo de contactos de confianza.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.d("msg", "No existe referencia");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("msg", "Error con documento");
            }
        });
    }
}