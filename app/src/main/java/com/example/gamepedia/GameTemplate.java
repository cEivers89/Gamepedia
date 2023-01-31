package com.example.gamepedia;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GameTemplate extends BaseAdapter {
    private final Activity context;
    ArrayList<GameItem> games;

    public GameTemplate(Activity context, ArrayList<GameItem> games, DatabaseDAO db) {
        this.context = context;
        this.games = games;
    }

    public static class Template {
        ImageView gameImage;
        TextView nameText;
        TextView ratingText;
        TextView releaseDateText;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        LayoutInflater inflater = context.getLayoutInflater();
        Template template;

        if (view == null) {
            template = new Template();
            row = inflater.inflate(R.layout.game_list_template, null, true);

            template.gameImage = row.findViewById(R.id.game_image);
            template.nameText = row.findViewById(R.id.name_text);
            template.ratingText = row.findViewById(R.id.rating_text);
            template.releaseDateText = row.findViewById(R.id.release_date_text);

            row.setTag(template);
        } else {
            template = (Template) view.getTag();
        }
        fetchImage(games.get(i).getImage(), template.gameImage, context);
        template.nameText.setText(games.get(i).getName());
        template.ratingText.setText(games.get(i).getRating());
        template.releaseDateText.setText(games.get(i).getReleaseDate());

        return row;
    }

    public static void fetchImage(String url, ImageView imageView, Context context) {
        Glide.with(context).load(url).centerCrop().into(imageView);
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
