package com.example.sicuraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.sicuraapp.Entities.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MasOpciones extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_opciones);

        mAuth= FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
    }

    public void irContactosConfianza (View view){
        Intent intent= new Intent(MasOpciones.this, ContactosConfianza.class);
        startActivity(intent);
    }

    public void iraCompartirRuta (View view){
        //Intent intent= new Intent(MasOpciones.this, CompartirRuta2.class);
        Intent intent= new Intent(MasOpciones.this, CompartirRuta_trasada.class);
        startActivity(intent);
    }

    public void irAMiPerfil (View view){

        if(currentUser!=null){
            db.collection("user").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            Usuario usuario = doc.toObject(Usuario.class);
                            String keyUsuario = usuario.getId();
                            String currentUserID = currentUser.getUid();

                            if(keyUsuario.equalsIgnoreCase(currentUserID)){
                                Intent intent = new Intent(MasOpciones.this, MiPerfil.class);
                                intent.putExtra("usuarioActual", usuario);
                                Log.d("msg-fb", usuario.getNombreApellidos());
                                startActivity(intent);
                            }
                        }
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MasOpciones.this, "Error al cargar.", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}