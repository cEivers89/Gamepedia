package com.example.gamepedia.GameFiles;

import static com.example.gamepedia.Constants.fetchImage;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamepedia.R;

import java.util.ArrayList;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {
    private final Activity context;
    private ArrayList<GameItem> games;

    private PopupWindow window;

    public GameAdapter(Activity context, ArrayList<GameItem> games) {
        this.context = context;
        this.games = games;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView gameImage;
        TextView nameText;
        TextView ratingText;
        TextView releaseDateText;

        LinearLayout gameRow;

        public ViewHolder(View view) {
            super(view);
            gameImage = view.findViewById(R.id.game_image);
            nameText = view.findViewById(R.id.name_text);
            ratingText = view.findViewById(R.id.rating_text);
            releaseDateText = view.findViewById(R.id.release_date_text);
            gameRow = view.findViewById(R.id.game_recycler_row);
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
        holder.gameRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameDetails(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public void gameDetails(final int position) {
        ImageView gameDetailsImage;
        TextView nameDetailsText;
        TextView metacriticDetailsText;
        TextView releaseDateDetailsText;
        TextView detailsDescription;
        ImageView favoriteDetailsImage;

        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.game, context.findViewById(R.id.game_details_layout));

        window = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        window.showAtLocation(layout, Gravity.CENTER, 0, 0);

        gameDetailsImage = layout.findViewById(R.id.gameImage);
        nameDetailsText = layout.findViewById(R.id.nameText);
        metacriticDetailsText = layout.findViewById(R.id.metacriticText);
        releaseDateDetailsText = layout.findViewById(R.id.releaseDateText);
        detailsDescription = layout.findViewById(R.id.descriptionText);
        favoriteDetailsImage = layout.findViewById(R.id.favoriteImage);

        fetchImage(games.get(position).getImage(), gameDetailsImage, context);
        nameDetailsText.setText(games.get(position).getName());
        metacriticDetailsText.setText(games.get(position).getMetacritic());
        releaseDateDetailsText.setText(games.get(position).getReleaseDate());
        detailsDescription.setText(games.get(position).getDescription());
        favoriteDetailsImage.setImageResource(games.get(position).isFavorite() ? R.drawable.ic_favorite_heart : R.drawable.ic_empty_heart);
    }
}
