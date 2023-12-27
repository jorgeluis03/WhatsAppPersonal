package com.example.whatsappclone.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.whatsappclone.R;
import com.example.whatsappclone.databinding.ActivityNumTelefonBinding;

public class NumTelefonActivity extends AppCompatActivity {
    ActivityNumTelefonBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityNumTelefonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonContinuar.setOnClickListener(v -> {
            Intent intent = new Intent(this, OTPActivity.class);
            intent.putExtra("numeroTelefono",binding.editTextIngreseNumero.getText().toString().trim());
            startActivity(intent);
        });
    }
}