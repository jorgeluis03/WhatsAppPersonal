package com.example.whatsappclone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.whatsappclone.R;
import com.example.whatsappclone.adapter.UsersAdapter;
import com.example.whatsappclone.databinding.ActivityMainBinding;
import com.example.whatsappclone.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    UsersAdapter adapter;
    FirebaseDatabase database;
    List<User> users;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cargarUsers();

        toolbar = findViewById(R.id.toolbar);  // Asegúrate de tener un elemento con el ID 'toolbar' en tu layout
        setSupportActionBar(toolbar);
        getSupportActionBar().show();
    }

    public void cargarUsers(){
        database = FirebaseDatabase.getInstance();
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