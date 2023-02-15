package com.example.gamepedia.GameFiles;

import static com.example.gamepedia.Constants.fetchImage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamepedia.R;
import java.util.List;

public class GameHeaderAdapter extends RecyclerView.Adapter<GameHeaderAdapter.ViewHolder> {
    private final Activity context;
    private List<GameItem> games;

    public GameHeaderAdapter(Activity context, List<GameItem> games) {
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
                    GameDetailsPopupWindow popupWindow = new GameDetailsPopupWindow(context, games.get(holder.getAdapterPosition()));
                    popupWindow.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return games.size();
    }
}
