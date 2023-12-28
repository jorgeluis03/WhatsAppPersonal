package com.example.whatsappclone.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.R;
import com.example.whatsappclone.activities.MainActivity;
import com.example.whatsappclone.databinding.IrvEstadosBinding;
import com.example.whatsappclone.model.Status;
import com.example.whatsappclone.model.UserStatus;

import java.util.ArrayList;
import java.util.List;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class TopStatusAdapter extends RecyclerView.Adapter<TopStatusAdapter.TopStatusViewHolder>{
    private final Context context;
    private final List<UserStatus> userStatuses;

    public TopStatusAdapter(Context context, List<UserStatus> userEstados) {
        this.context = context;
        this.userStatuses = userEstados;
    }

    @NonNull
    @Override
    public TopStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_estados,parent,false);
        return new TopStatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopStatusViewHolder holder, int position) {
        UserStatus userEstados = userStatuses.get(position);


        Status lastStatus = userEstados.getStatusList().get(userEstados.getStatusList().size()-1);

        Glide.with(context).load(lastStatus.getImageUrl()).into(holder.binding.imageEstado);
        holder.binding.circleStatusView.setPortionsCount(userEstados.getStatusList().size());

        holder.binding.circleStatusView.setOnClickListener(v -> {
            ArrayList<MyStory> myStories = new ArrayList<>();

            for(Status story: userEstados.getStatusList()){
                myStories.add(new MyStory(story.getImageUrl()));
            }

            new StoryView.Builder(((MainActivity)context).getSupportFragmentManager())
                    .setStoriesList(myStories) // Required
                    .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                    .setTitleText(userEstados.getName()) // nombre
                    .setSubtitleText("") // Default is Hidden
                    .setTitleLogoUrl(userEstados.getProfileImagen()) //foto de perfil
                    .setStoryClickListeners(new StoryClickListeners() {
                        @Override
                        public void onDescriptionClickListener(int position) {
                            //your action
                        }

                        @Override
                        public void onTitleIconClickListener(int position) {
                            //your action
                        }
                    }) // Optional Listeners
                    .build() // Must be called before calling show method
                    .show();


        });

    }

    @Override
    public int getItemCount() {
        return userStatuses.size();
    }

    public class TopStatusViewHolder extends RecyclerView.ViewHolder {
        IrvEstadosBinding binding;
        public TopStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = IrvEstadosBinding.bind(itemView);

        }
    }

}
