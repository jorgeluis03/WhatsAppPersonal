package com.example.whatsappclone.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.whatsappclone.R;
import com.example.whatsappclone.adapter.TopStatusAdapter;
import com.example.whatsappclone.adapter.UsersAdapter;
import com.example.whatsappclone.databinding.ActivityMainBinding;
import com.example.whatsappclone.model.User;
import com.example.whatsappclone.model.UserStatus;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    UsersAdapter adapter;
    FirebaseDatabase database;
    List<User> users;
    List<UserStatus> userStatuses;
    TopStatusAdapter statusAdapter;
    Toolbar toolbar;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Subiendo estado...");
        progressDialog.setCancelable(false);

        cargarUsers();

        toolbar = findViewById(R.id.toolbar);  // Asegúrate de tener un elemento con el ID 'toolbar' en tu layout
        setSupportActionBar(toolbar);
        getSupportActionBar().show();


        //bottomNavigationVIew
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id==R.id.status){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                launcher.launch(intent);
                return true;
            }
            return false;
        });
    }

    public ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result->{
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {

            progressDialog.show();

            // Procesar el resultado y subirlo al firebaseStorage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            Date date = new Date();
            StorageReference reference = storage.getReference().child("status").child(date.getTime()+"");
            reference.putFile(result.getData().getData())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){ //si ya se subio lo obtengo para cargarlo y mostrarlo
                            reference.getDownloadUrl().addOnSuccessListener(uri -> {

                                progressDialog.dismiss();



                            });

                        }
                    });
        }
    });

    public void cargarUsers(){

        database.getReference().child("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        users = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            User user = child.getValue(User.class);
                            users.add(user);
                        }
                        adapter = new UsersAdapter(getApplicationContext(),users);
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        binding.recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.search){
            showToast("search");
            return true;
        }
        if(id==R.id.config){
            showToast("config");
            return true;
        }
        if(id==R.id.salir){
            showToast("salir");
            return true;
        }
        if(id==R.id.grupos){
            showToast("grupos");
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void showToast(String m){
        Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
    }
}