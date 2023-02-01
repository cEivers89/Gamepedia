package com.example.gamepedia;

import static com.example.gamepedia.Constants.fetchImage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {
    private final Activity context;
    private ArrayList<GameItem> games;

    public GameAdapter(Activity context, ArrayList<GameItem> games) {
        this.context = context;
        this.games = games;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView gameImage;
        TextView nameText;
        TextView ratingText;
        TextView releaseDateText;

        public ViewHolder(View view) {
            super(view);
            gameImage = view.findViewById(R.id.game_image);
            nameText = view.findViewById(R.id.name_text);
            ratingText = view.findViewById(R.id.rating_text);
            releaseDateText = view.findViewById(R.id.release_date_text);
        }
    }
    @NonNull
    @Override
    public GameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_list_template, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameAdapter.ViewHolder holder, int position) {
        fetchImage(games.get(position).getImage(), holder.gameImage, context);
        holder.nameText.setText(games.get(position).getName());
        holder.ratingText.setText(games.get(position).getRating());
        holder.releaseDateText.setText(games.get(position).getReleaseDate());
    }

    @Override
    public int getItemCount() {
        return games.size();
    }
}
