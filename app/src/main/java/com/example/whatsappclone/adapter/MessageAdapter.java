package com.example.whatsappclone.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.R;
import com.example.whatsappclone.databinding.IrvReceiverBinding;
import com.example.whatsappclone.databinding.IrvSentBinding;
import com.example.whatsappclone.model.Message;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter{
    private final Context context;
    private final List<Message> messages;
    final int ITEM_SENT = 1;
    final int ITEM_RECEIVER=2;
    final String senderRoom,receiverRoom;

    public MessageAdapter(Context context, List<Message> messages,String senderRoom,String receiverRoom) {
        this.context = context;
        this.messages = messages;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SENT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_sent,parent,false);
            return new SentViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_receiver,parent,false);
            return new ReceivarViewHolder(view);
        }
    }

    //getItemViewType  determinar el tipo de vista que se mostrará en una posición específica dentro del RecyclerView
    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderId())){//si el uid de la sesion actual es igual que el senderId
            return ITEM_SENT;
        }else {
            return ITEM_RECEIVER;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        int reactions[] = new int[]{
                R.drawable.emoji_caguerisa,
                R.drawable.emoji_corazon,
                R.drawable.emoji_lentes,
                R.drawable.emoji_lloron,
                R.drawable.emoji_molesto,
                R.drawable.emoji_sorpresa
        };

        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reactions)
                .build();
        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
            int reaction = reactions[pos];
            if(holder.getClass()==SentViewHolder.class){
                SentViewHolder sentViewHolder = (SentViewHolder) holder;
                sentViewHolder.binding.emoji.setImageResource(reaction);
                sentViewHolder.binding.emoji.setVisibility(View.VISIBLE);
            }else {
                ReceivarViewHolder receivarViewHolder = (ReceivarViewHolder) holder;
                receivarViewHolder.binding.emoji.setImageResource(reaction);
                receivarViewHolder.binding.emoji.setVisibility(View.VISIBLE);
            }

            message.setFeeling(pos);


            FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(senderRoom)
                .child("messages")
                .child(message.getMessageId()).setValue(message);



            return true;
        });


        if (holder.getClass()==SentViewHolder.class){
            SentViewHolder sentViewHolder = (SentViewHolder) holder;
            sentViewHolder.binding.tvMessageSent.setText(message.getMessage());
            /*
            if (message.getFeeling()>=0){
                message.setFeeling(reactions[(int) message.getFeeling()]);
                sentViewHolder.binding.emoji.setVisibility(View.VISIBLE);
            }else {
                sentViewHolder.binding.emoji.setVisibility(View.GONE);
            }*/

            //emojis
            sentViewHolder.binding.tvMessageSent.setOnTouchListener((v, event) -> {
                popup.onTouch(v,event);
                return false;
            });

        }else {
            ReceivarViewHolder receivarViewHolder = (ReceivarViewHolder) holder;
            receivarViewHolder.binding.tvMessageReceiver.setText(message.getMessage());
            /*
            if (message.getFeeling()>=0){
                message.setFeeling(reactions[(int) message.getFeeling()]);
                receivarViewHolder.binding.emoji.setVisibility(View.VISIBLE);
            }else {
                receivarViewHolder.binding.emoji.setVisibility(View.GONE);
            }*/
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {
        IrvSentBinding binding;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = IrvSentBinding.bind(itemView);
        }
    }


    public class ReceivarViewHolder extends RecyclerView.ViewHolder {
        IrvReceiverBinding binding;
        public ReceivarViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=IrvReceiverBinding.bind(itemView);
        }
    }
}
