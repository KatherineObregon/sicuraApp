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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class IniciarSesion extends AppCompatActivity {

    TextInputEditText email, passwd;
    Button btnLogin;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.vista_clara);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        mAuth= FirebaseAuth.getInstance();

        email= findViewById(R.id.inputEmailLogin);
        passwd= findViewById(R.id.inputPasswordLogin);
        btnLogin = findViewById(R.id.btn_iniciarSesion);
    }

    public void login (View view){

        String emailString = email.getText().toString();
        String passwdString = passwd.getText().toString();

        if( emailString.isEmpty() || passwdString.isEmpty()){
            Toast.makeText(this, "Ingresar los datos", Toast.LENGTH_SHORT).show();
        }else {
            mAuth.signInWithEmailAndPassword(emailString, passwdString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        finish();
                        startActivity(new Intent(IniciarSesion.this, Home.class));
                        Toast.makeText(IniciarSesion.this, "Bienvenida", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(IniciarSesion.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                }
            });
        }


        Intent intent= new Intent(IniciarSesion.this, Home.class);
        startActivity(intent);




    }
}