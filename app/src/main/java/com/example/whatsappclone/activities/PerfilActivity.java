package com.example.whatsappclone.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.example.whatsappclone.databinding.ActivityPerfilBinding;
import com.example.whatsappclone.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PerfilActivity extends AppCompatActivity {
    ActivityPerfilBinding binding;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    Uri imagenSeleccionada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        binding.imageView.setOnClickListener(v -> { //subir imagen
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            launcher.launch(intent);
        });

        binding.buttonContinuar.setOnClickListener(v -> {
            inProgress(true);
            String nombre = binding.editTextNombre.getText().toString();
            if (nombre.isEmpty()){
                binding.editTextNombre.setError("Por favor introduzca un nombre");
            }

            if(imagenSeleccionada!=null){
                StorageReference reference = storage.getReference().child("Perfiles")
                        .child(auth.getUid());
                reference.putFile(imagenSeleccionada).addOnCompleteListener(task -> { //subir al firebaseStorage
                    if (task.isSuccessful()){
                        reference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUri = uri.toString();
                            String uid = auth.getUid();
                            String phone = auth.getCurrentUser().getPhoneNumber(); // obtengo el numero que se uso para iniciar sesion
                            String name = binding.editTextNombre.getText().toString();

                            User user = new User(uid,name,phone,imageUri);

                            database.getReference().child("users")
                                    .child(auth.getUid())
                                    .setValue(user)
                                    .addOnSuccessListener(unused -> {
                                        inProgress(false);

                                        Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    });
                        });
                    }
                });
            }else {
                String uid = auth.getUid();
                String phone = auth.getCurrentUser().getPhoneNumber(); // obtengo el numero que se uso para iniciar sesion
                String name = binding.editTextNombre.getText().toString();

                User user = new User(uid,name,phone,"No image");

                database.getReference().child("users")
                        .child(auth.getUid())
                        .setValue(user)
                        .addOnSuccessListener(unused -> {
                            inProgress(false);

                            Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        });

            }
        });

    }
    /* cargar imagen */public ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result->{
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            // Procesar el resultado
            binding.imageView.setImageURI(result.getData().getData());
            imagenSeleccionada = result.getData().getData();
        }
    });
    public void inProgress(Boolean inProgress){
        if(inProgress){
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.buttonContinuar.setVisibility(View.GONE);
        }else {
            binding.progressBar.setVisibility(View.GONE);
            binding.buttonContinuar.setVisibility(View.VISIBLE);
        }
    }
}