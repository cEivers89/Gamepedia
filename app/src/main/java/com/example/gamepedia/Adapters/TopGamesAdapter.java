package com.example.gamepedia.Adapters;

import static com.example.gamepedia.Constants.fetchImage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamepedia.GameFiles.GameDetailsPopupWindow;
import com.example.gamepedia.GameFiles.GameItem;
import com.example.gamepedia.R;
import java.util.List;

public class TopGamesAdapter extends RecyclerView.Adapter<TopGamesAdapter.ViewHolder> {
    private final Activity context;
    private List<GameItem> games;

    public TopGamesAdapter(Activity context, List<GameItem> games) {
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
    public TopGamesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_list_header, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopGamesAdapter.ViewHolder holder, int position) {
        fetchImage(games.get(position).getImage(), holder.gameImage, context);
        holder.gameHeaderImageRow.setOnClickListener(v -> {
            if (holder.gameHeaderImageRow.getWindowToken() != null) {
                GameDetailsPopupWindow popupWindow = new GameDetailsPopupWindow(context, games.get(holder.getAdapterPosition()));
                popupWindow.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return games.size();
    }
}
