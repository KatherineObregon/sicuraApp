package com.example.sicuraapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sicuraapp.Entities.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MiPerfil extends AppCompatActivity {

    FirebaseFirestore db;
    StorageReference storageRef;

    String imageUrl;
    boolean cambioImagen=false;

    Usuario usuario = new Usuario();
    ImageView imageView;


    TextView nombre;
    TextView correo;
    TextInputEditText celular ;
    FirebaseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.vista_clara);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        Intent intent = getIntent();


        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference().child(currentUser.getUid());
        db= FirebaseFirestore.getInstance();
        nombre = findViewById(R.id.tv_MiPerfilNombre);
        correo = findViewById(R.id.tv_MiperfilCorreo);
        celular = findViewById(R.id.inputMiPerfilCelular);
        imageView =  findViewById(R.id.imageViewMiPerfil);
        usuario = (Usuario) intent.getSerializableExtra("usuarioActual");


        if(usuario.getFotoUrl()!=null){
            Glide.with(MiPerfil.this).load(usuario.getFotoUrl()).into(imageView);
        }


        nombre.setText(usuario.getNombreApellidos());
        correo.setText(usuario.getCorreo());
        celular.setText(usuario.getCelular());


    }
    public void subirFotoPerfil(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        launcherPhotos.launch(intent);
    }
    boolean entroSubida = false;
    ActivityResultLauncher<Intent> launcherPhotos = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Uri uri = result.getData().getData();
                    StorageReference child = storageRef.child(uri.getLastPathSegment());

                    child.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            child.getDownloadUrl().addOnSuccessListener(uri1 -> {
                                entroSubida=true;
                                imageUrl = uri1.toString();
                                //usuario.setFotoUrl(imageUrl);
                                Log.d("msg-test", "ruta archivo: " + imageUrl);
                                updateImageView();
                            }).addOnFailureListener(e -> {
                                imageUrl = "";
                                updateImageView();
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("msg", "Fallo subida",e);
                            imageUrl = "";
                            updateImageView();
                        }
                    });
                } else {
                    Toast.makeText(MiPerfil.this, "Debe seleccionar un archivo", Toast.LENGTH_SHORT).show();
                    imageUrl = "";
                    updateImageView();
                }
            }

    );

    public void updateImageView(){
        if(!imageUrl.isEmpty() && entroSubida){
            Glide.with(MiPerfil.this).load(imageUrl).into(imageView);
        }
    }

    public void actualizarMiPerfil(View view){
        String celularNuevo = celular.getText().toString();


        Map<String, Object> usuario = new HashMap<>();
        usuario.put("celular", Objects.requireNonNull(celularNuevo));
        if(entroSubida){

            usuario.put("fotoUrl", Objects.requireNonNull(imageUrl));
        }
        db.collection("user").document(currentUser.getUid()).update(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MiPerfil.this, "Perfil actualizado correctamente.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MiPerfil.this, "Ocurrió un error al actualizar el perfil.", Toast.LENGTH_SHORT).show();
            }
        });
    }






}