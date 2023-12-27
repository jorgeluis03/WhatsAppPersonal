package com.example.whatsappclone.constanst;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {

    public static String getStringChatroomId(String user1Id,String user2Id){
        if(user1Id.hashCode()<user2Id.hashCode()){
            return user1Id+"-"+user2Id;
        }else {
            return user2Id+"-"+user1Id;
        }
    }

    public static DatabaseReference getChatroomIdReference(String chatroomId){
        return  FirebaseDatabase.getInstance().getReference().child("chatRooms").child(chatroomId);
    }

    public static DatabaseReference  getChatsToChatRoomId(String chatroomId){
        return getChatroomIdReference(chatroomId).child("chats").child("messages");
    }
}
