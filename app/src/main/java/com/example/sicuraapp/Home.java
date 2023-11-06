package com.example.sicuraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    Button logout;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.home);
        super.onCreate(savedInstanceState);
        mAuth= FirebaseAuth.getInstance();

        setContentView(R.layout.activity_home);
        logout = findViewById(R.id.btn_logout);
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