package com.example.gamepedia;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GameHeaderViewHolder extends RecyclerView.ViewHolder {
    ImageView gameImageBanner;

    public GameHeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        gameImageBanner = itemView.findViewById(R.id.game_image_banner);
    }
}
