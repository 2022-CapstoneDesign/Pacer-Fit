package com.example.project.YouTube;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.util.List;

public class YouTubeAdapter extends RecyclerView.Adapter<YouTubeViewHolder> {
    private List<YouTubeContent> contents;

    public YouTubeAdapter(@NonNull List<YouTubeContent> contents) {
        this.contents = contents;
    }

    @NonNull
    @Override
    public YouTubeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_card_you_tube_content, parent, false);

        return new YouTubeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YouTubeViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.youTubePlayerView.setEnableAutomaticInitialization(false);
        holder.youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(contents.get(position).videoId, 0);
            }
        }, false);
    }



    @Override
    public int getItemCount() {
        return (contents != null) ? contents.size() : 0;
    }
}