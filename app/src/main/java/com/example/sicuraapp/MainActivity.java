package com.example.sicuraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sicuraapp.R;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {



    Button botonEmpecemos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.launcher);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        botonEmpecemos = findViewById(R.id.btn_empecemos);

        botonEmpecemos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, Launcher2.class);
                startActivity(intent);

            }
        });



    }



}