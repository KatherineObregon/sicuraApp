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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class ContactosConfianza extends AppCompatActivity {

    ListaContactosAdapter listaContactosAdapter;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private List<Usuario> firebaseUsuarios = new ArrayList<>();
    private ArrayList<Usuario> firabeseUsuariosContactos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos_confianza);

        mAuth= FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recyclerview_contactos);
        listaContactosAdapter = new ListaContactosAdapter(ContactosConfianza.this,firabeseUsuariosContactos);
        recyclerView.setAdapter(listaContactosAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ContactosConfianza.this));
    }

    @Override
    protected void onResume() {
        cargarDatosdeFirebase();
        super.onResume();
    }
    public void cargarDatosdeFirebase(){
        firebaseUsuarios.clear();
        Log.d("msg", "entra a cargar datos");
        db.collection("user").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        Usuario usuario = doc.toObject(Usuario.class);
                        firebaseUsuarios.add(usuario);
                    }
                    filtrarUsuarios();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("msg", "error en cargar datos firebase");
            }
        });

    }

    public void filtrarUsuarios(){
        firabeseUsuariosContactos.clear();

        DocumentReference docRef= db.collection("user").document(currentUser.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Log.d("msg", "entra a filtrar usuarios");
                    List<String> miListaContactos = (List<String>) documentSnapshot.get("contactosID");
                    if(miListaContactos==null){
                        Toast.makeText(ContactosConfianza.this, "Aún no hay ningún contacto de confianza. Agrega al menos uno para empezar a usar la aplicación.", Toast.LENGTH_SHORT).show();

                    }
                    else if(miListaContactos!=null){
                        Log.d("msg", "entra a listacontactos");
                        for(Usuario u: firebaseUsuarios){
                            String idU = u.getId();
                            Log.d("msg", "idu: "+ idU);

                            for(String cont : miListaContactos){
                                if (cont.equalsIgnoreCase(idU)){

                                    Log.d("msg", "entra a igual");
                                    firabeseUsuariosContactos.add(u);
                                }
                            }
                        }
                        listaContactosAdapter.notifyDataSetChanged();

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