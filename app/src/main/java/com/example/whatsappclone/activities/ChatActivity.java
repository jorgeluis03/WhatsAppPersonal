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
    String chatRoomId;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();





        String receiverName = getIntent().getStringExtra("name");
        String receiverId = getIntent().getStringExtra("uid");
        String senderId = FirebaseAuth.getInstance().getUid();
        binding.tvName.setText(receiverName);

        chatRoomId = FirebaseUtils.getStringChatroomId(senderId,receiverId);

        binding.buttonSend.setOnClickListener(v -> {
            String messageInput = binding.editTextInputMessage.getText().toString();
            binding.editTextInputMessage.setText(null);

            Message message = new Message(messageInput,senderId,new Date().getTime());

            FirebaseUtils.getChatroomIdReference(chatRoomId).setValue(new ChatRoom(chatRoomId, Arrays.asList(senderId,receiverId),senderId))
                    .addOnSuccessListener(unused -> {
                        FirebaseUtils.getChatsToChatRoomId(chatRoomId).setValue(message);
                    });
        });

        database.getReference().child("chatRooms")
                .child("chats")
                .child("messages")
                .push()
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                messages = new ArrayList<>();
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    Message message = child.getValue(Message.class);
                                    Log.d("msg-test",message.getMessage());
                                    messages.add(message);
                                }
                                adapter = new MessageAdapter(getApplicationContext(),messages);
                                binding.recyclerView.setAdapter(adapter);
                                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        //back
        binding.buttonBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

}