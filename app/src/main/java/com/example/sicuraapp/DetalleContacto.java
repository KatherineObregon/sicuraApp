package com.example.sicuraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sicuraapp.Entities.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DetalleContacto extends AppCompatActivity {


    Usuario usuario = new Usuario();
    FirebaseFirestore db;
    FirebaseUser currentUser;
    DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_contacto);
        db= FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        docRef= db.collection("user").document(currentUser.getUid());


        Intent intent = getIntent();

        TextView nombreTV= findViewById(R.id.tv_detalle_contacto_nombre);
        TextView correoTV= findViewById(R.id.tv_detalle_contacto_correo);
        TextView celularTV = findViewById(R.id.tv_detalle_contacto_celular);

        ImageView imageView = findViewById(R.id.imageViewContacto);
        usuario = (Usuario) intent.getSerializableExtra("usuarioDetalle");
        if(usuario.getFotoUrl()!=null){
            Glide.with(DetalleContacto.this).load(usuario.getFotoUrl()).into(imageView);
        }
        nombreTV.setText(usuario.getNombreApellidos());
        correoTV.setText(usuario.getCorreo());
        celularTV.setText(usuario.getCelular());

    }

    public void borrarContacto(View view){
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // El documento existe, ahora puedes obtener la lista
                            List<String> miLista = (List<String>) documentSnapshot.get("contactosID");

                            if (miLista != null) {
                                // Elimina el elemento deseado de la lista localmente
                                String elementoAEliminar = usuario.getId();
                                miLista.remove(elementoAEliminar);

                                // Actualiza el documento en Firestore con la nueva lista que excluye el elemento eliminado
                                docRef.update("contactosID", miLista)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(DetalleContacto.this, "Se eliminó el contacto con éxito.", Toast.LENGTH_SHORT).show();
                                                Intent intent= new Intent(DetalleContacto.this, ContactosConfianza.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error al actualizar la lista en Firestore", e);
                                            }
                                        });
                            } else {
                                Log.d("TAG", "La lista está vacía o el campo no existe en el documento.");
                            }
                        } else {
                            Log.d("TAG", "El documento no existe en Firestore.");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error al recuperar el documento de Firestore", e);
                    }
                });
    }
}