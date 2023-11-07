package com.example.sicuraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrarUsuario extends AppCompatActivity {

    TextInputEditText nombresApellidos, email, celular, contra, contra2;
    Button btnRegistro;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.vista_clara);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        nombresApellidos = findViewById(R.id.inputNombreUsuario);
        email= findViewById(R.id.inputCorreoElectronicoInput);
        celular= findViewById(R.id.inputCelular);
        contra=findViewById(R.id.inputPassword);
        contra2= findViewById(R.id.inputPassword2);
        btnRegistro= findViewById(R.id.btn_registrarme);

    }

    public void registroUsuario (View view){
        String nombresString = nombresApellidos.getText().toString();
        String emailString = email.getText().toString();
        String celularString = celular.getText().toString().trim();
        String contraString = contra.getText().toString();
        String contra2String = contra2.getText().toString();

        if(nombresString.isEmpty() || emailString.isEmpty()|| celularString.isEmpty() || contraString.isEmpty()|| contra2String.isEmpty()){
            Toast.makeText(this, "Ingresar datos válidos.", Toast.LENGTH_SHORT).show();
        }else {
            mAuth.createUserWithEmailAndPassword(emailString, contraString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    String id = mAuth.getCurrentUser().getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", id);
                    map.put("nombreApellidos", nombresString);
                    map.put("correo", emailString);
                    map.put("celular", celularString);

                    mFirestore.collection("user").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            finish();
                            startActivity(new Intent(RegistrarUsuario.this, RegistroExito.class));
                            Toast.makeText(RegistrarUsuario.this, "Usuario registrado con exito.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistrarUsuario.this, "Error al registrarse", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegistrarUsuario.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}