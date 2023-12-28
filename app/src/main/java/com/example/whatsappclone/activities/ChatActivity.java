package com.example.whatsappclone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;

import com.example.whatsappclone.R;
import com.example.whatsappclone.adapter.MessageAdapter;
import com.example.whatsappclone.constanst.FirebaseUtils;
import com.example.whatsappclone.databinding.ActivityChatBinding;
import com.example.whatsappclone.model.ChatRoom;
import com.example.whatsappclone.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    List<Message> messages;
    MessageAdapter adapter;
    FirebaseDatabase database;
    String senderRoom, receiverRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        messages = new ArrayList<>();


        String receiverName = getIntent().getStringExtra("name");
        String receiverId = getIntent().getStringExtra("uid");
        String senderId = FirebaseAuth.getInstance().getUid();

        binding.tvName.setText(receiverName);
        senderRoom = senderId+receiverId;
        receiverRoom = receiverId+senderId;

        adapter = new MessageAdapter(this,messages,senderRoom,receiverRoom);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();

                        for (DataSnapshot child : snapshot.getChildren()) {
                            Message message = child.getValue(Message.class);
                            message.setMessageId(child.getKey());
                            messages.add(message);
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //enviar mensaje
        binding.buttonSend.setOnClickListener(v -> {
            String messageInput = binding.editTextInputMessage.getText().toString();
            binding.editTextInputMessage.setText(null);

            Message message = new Message(messageInput,senderId,new Date().getTime());

            String randomKey = database.getReference().push().getKey();

            database.getReference().child("chats")
                    .child(senderRoom)
                    .child("messages")
                    .child(randomKey)// push es el simil de add, es decir que genera una Id aleatorio
                    .setValue(message)
                    .addOnSuccessListener(unused -> {

                        database.getReference().child("chats")
                                .child(receiverRoom)
                                .child("messages")
                                .child(randomKey)//es el simil de add, es decir que genera una Id aleatorio
                                .setValue(message);

                    });

        });

        //back
        binding.buttonBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

}