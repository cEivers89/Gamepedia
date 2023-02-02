package com.example.gamepedia;

import static com.example.gamepedia.Constants.fetchImage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GameHeaderAdapter extends RecyclerView.Adapter<GameHeaderAdapter.ViewHolder> {
    private final Activity context;
    private ArrayList<GameItem> games;

    public GameHeaderAdapter(Activity context, ArrayList<GameItem> games) {
        this.context = context;
        this.games = games;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView gameImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gameImage = itemView.findViewById(R.id.game_image_banner);
        }
    }

    @NonNull
    @Override
    public GameHeaderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_banner, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameHeaderAdapter.ViewHolder holder, int position) {
        fetchImage(games.get(position).getImage(), holder.gameImage, context);
    }

    @Override
    public int getItemCount() {
        return Math.min(games.size(), 5);
    }
}
