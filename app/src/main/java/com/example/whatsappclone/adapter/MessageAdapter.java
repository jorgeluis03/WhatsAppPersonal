package com.example.whatsappclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.R;
import com.example.whatsappclone.databinding.IrvReceiverBinding;
import com.example.whatsappclone.databinding.IrvSentBinding;
import com.example.whatsappclone.model.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter{
    private final Context context;
    private final List<Message> messages;
    final int ITEM_SENT = 1;
    final int ITEM_RECEIVER=2;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
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

        if (holder.getClass()==SentViewHolder.class){
            SentViewHolder sentViewHolder = (SentViewHolder) holder;
            sentViewHolder.binding.tvMessageSent.setText(message.getMessage());

        }else {
            ReceivarViewHolder receivarViewHolder = (ReceivarViewHolder) holder;
            receivarViewHolder.binding.tvMessageReceiver.setText(message.getMessage());
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
