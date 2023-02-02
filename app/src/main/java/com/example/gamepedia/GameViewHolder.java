package com.example.gamepedia;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GameViewHolder extends RecyclerView.ViewHolder {
    ImageView gameImage;
    TextView nameText;
    TextView ratingText;
    TextView releasedDateText;

    public GameViewHolder(@NonNull View itemView) {
        super(itemView);
        gameImage = itemView.findViewById(R.id.game_image);
        nameText = itemView.findViewById(R.id.name_text);
        ratingText = itemView.findViewById(R.id.rating_text);
        releasedDateText = itemView.findViewById(R.id.release_date_text);
    }
}
