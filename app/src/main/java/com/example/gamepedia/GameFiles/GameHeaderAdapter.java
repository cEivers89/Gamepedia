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

public class GameHeaderAdapter extends RecyclerView.Adapter<GameHeaderAdapter.ViewHolder> {
    private final Activity context;
    private ArrayList<GameItem> games;
    private PopupWindow window;

    public GameHeaderAdapter(Activity context, ArrayList<GameItem> games) {
        this.context = context;
        this.games = games;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView gameImage;
        LinearLayout gameHeaderImageRow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gameImage = itemView.findViewById(R.id.game_image_banner);
            gameHeaderImageRow = itemView.findViewById(R.id.game_header_image);
        }
    }

    @NonNull
    @Override
    public GameHeaderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_list_header, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameHeaderAdapter.ViewHolder holder, int position) {
        fetchImage(games.get(position).getImage(), holder.gameImage, context);
        holder.gameHeaderImageRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.gameHeaderImageRow.getWindowToken() != null) {
                    gameDetails(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(games.size(), 5);
    }

    private void gameDetails(final int position) {
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
