package com.example.gamepedia;

import static com.example.gamepedia.Constants.fetchImage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Activity context;
    private ArrayList<GameItem> games;
    private static final int HEADER_VIEW = 0;
    private static final int BODY_VIEW = 1;

    public HomeAdapter(Activity context, ArrayList<GameItem> games) {
        this.context = context;
        this.games = games;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == HEADER_VIEW) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_banner, parent, false);
            return new GameHeaderViewHolder(itemView);
        }
        else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.game_list_template, parent, false);
            return new GameViewHolder(itemView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == HEADER_VIEW) {
            GameHeaderViewHolder headerHolder = (GameHeaderViewHolder) holder;
            fetchImage(games.get(position).getImage(), headerHolder.gameImageBanner, context);
        }
        else {
            GameViewHolder bodyHolder = (GameViewHolder) holder;
            fetchImage(games.get(position).getImage(), bodyHolder.gameImage, context);
            bodyHolder.nameText.setText(games.get(position).getName());
            bodyHolder.ratingText.setText(games.get(position).getRating());
            bodyHolder.releasedDateText.setText(games.get(position).getReleaseDate());
        }
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_VIEW;
        }
        return BODY_VIEW;
    }
}
