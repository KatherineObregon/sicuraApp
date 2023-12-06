package com.example.sicuraapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sicuraapp.Entities.Alerta;
import com.example.sicuraapp.Entities.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Home extends AppCompatActivity {

    Button logout;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String idUsuarioActual;
    private String chanNotifDefaultId = "chanNotifDefaultId";


    private List<Usuario> firebaseUsuarios = new ArrayList<>();
    //private ArrayList<Usuario> firabeseUsuariosContactos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.home);
        super.onCreate(savedInstanceState);
        

        setContentView(R.layout.activity_home);
        logout = findViewById(R.id.btn_logout);

        createNotificationChannel();

        mAuth= FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();


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

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("msg", "error en cargar datos firebase");
            }
        });

        db.collection("alerta").addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                // Maneja errores aquí
                return;
            }

            // Procesa los cambios en la colección "alerta"
            // TODO: sale toast por cada documento cuando se inicia la aplicacion
            for (DocumentChange
                    documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    // Si se añade un nuevo documento, muestra un Toast
                    //Toast.makeText(this, "Nuevo documento en la coleccion", Toast.LENGTH_SHORT).show();

                    //String newdocumentId = documentChange.getDocument().getId();

                    Alerta alertaNueva = documentChange.getDocument().toObject(Alerta.class);

                    // Ahora, puedes usar el objeto Alerta según tus necesidades

                    //Toast.makeText(this, "Nuevo documento en la colección 'alerta' con hora: " + alerta.getHora(), Toast.LENGTH_SHORT).show();

                    String usuarioAlerta = alertaNueva.getIdUsuario();

                    //firabeseUsuariosContactos.clear();
                    recibirNotificacion("Ariana Castro");

                    DocumentReference docRef= db.collection("user").document(usuarioAlerta);
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                Log.d("msg", "entra a filtrar usuarios");
                                List<String> listaContactosAlerta = (List<String>) documentSnapshot.get("contactosID");
                                if(listaContactosAlerta==null){
                                    Toast.makeText(Home.this, "Aún no hay ningún contacto de confianza. Agrega al menos uno para empezar a usar la aplicación.", Toast.LENGTH_SHORT).show();

                                }
                                else if(listaContactosAlerta!=null){
                                    Log.d("msg", "entra a listacontactos");
                                    for(Usuario u: firebaseUsuarios){
                                        String idU = u.getId();
                                        Log.d("msg", "idu: "+ idU);

                                        for(String cont : listaContactosAlerta){
                                            if (cont.equalsIgnoreCase(idU)){

                                                Log.d("msg", "entra a igual "+ cont);
                                                //firabeseUsuariosContactos.add(u);

                                                if(cont.equalsIgnoreCase(mAuth.getCurrentUser().getUid())){
                                                    //recibirNotificacion();
                                                    recibirNotificacion(u.getNombreApellidos());
                                                }
                                            }
                                        }
                                    }

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
        });


    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channelDefault = new NotificationChannel(chanNotifDefaultId, "Canal notificaciones 3", NotificationManager.IMPORTANCE_DEFAULT);
            channelDefault.setDescription("Canal de notificaciones con importancia = DEFAULT");
            channelDefault.enableVibration(true);


            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channelDefault);
        }
    }

    private void recibirNotificacion(String nombresAlerta) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, chanNotifDefaultId)
                .setSmallIcon(R.mipmap.ic_sicura_logo_letras_foreground).
                setContentTitle("sicuraApp")
                .setContentText("Tu contacto "+nombresAlerta+" se encuentra en peligro." )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat= NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(3, builder.build());

    }


    public void botonAlerta(View view){
        idUsuarioActual= mAuth.getCurrentUser().getUid();

        // Obtiene la fecha y hora actual
        Date currentDate = new Date();

        // Define el formato deseado para la fecha y hora
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // Formatea la fecha y hora actual como un String separado por un guion
        String formattedDateTime = dateFormat.format(currentDate);
        String[] dateTimeParts = formattedDateTime.split(" ");
        String fechaHora= dateTimeParts[0].replace("-", "") + "-" + dateTimeParts[1].replace(":", "");

        String idAlerta = idUsuarioActual+fechaHora;
        Map<String, Object> map = new HashMap<>();
        map.put("id", idAlerta);
        map.put("fecha", dateTimeParts[0]);
        map.put("hora", dateTimeParts[1]);
        map.put("idUsuario", idUsuarioActual);

        db.collection("alerta").document(idAlerta).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
//                finish();
//                startActivity(new Intent(Home.this, Home.class));
                //Toast.makeText(Home.this, "Alerta creada", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Home.this, "Alerta no creada :(", Toast.LENGTH_SHORT).show();
            }
        });


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