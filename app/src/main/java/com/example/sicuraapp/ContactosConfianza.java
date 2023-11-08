package com.example.sicuraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.sicuraapp.Adapters.ListaContactosAdapter;
import com.example.sicuraapp.Entities.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ContactosConfianza extends AppCompatActivity {

    ListaContactosAdapter listaContactosAdapter;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos_confianza);

        mAuth= FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();

//        RecyclerView recyclerView = findViewById(R.id.recyclerview_contactos);
//        listaContactosAdapter = new ListaContactosAdapter(ContactosConfianza.this,firabeseUsuariosCoordinadores);
//        recyclerView.setAdapter(listaContactosAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(ContactosConfianza.this));
    }

    public void irAnadirContactos (View view){
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
                                Intent intent = new Intent(ContactosConfianza.this, AnadirContacto.class);
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
                    Toast.makeText(ContactosConfianza.this, "Error al cargar.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}