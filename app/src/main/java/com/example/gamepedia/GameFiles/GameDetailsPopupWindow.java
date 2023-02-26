package com.example.gamepedia.GameFiles;

import static com.example.gamepedia.Constants.fetchImage;

import android.app.Activity;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.gamepedia.DatabaseFiles.DatabaseSingleton;
import com.example.gamepedia.DatabaseFiles.GameDatabase;
import com.example.gamepedia.Gamepedia;
import com.example.gamepedia.R;


public class GameDetailsPopupWindow {
    private final Activity context;
    private final GameItem gameItem;
    private GameDatabase gameDatabase;
    public PopupWindow window;


    public GameDetailsPopupWindow(Activity context, GameItem gameItem) {
        this.context = context;
        this.gameItem = gameItem;
    }

    public void show() {
        gameDatabase = DatabaseSingleton.getInstance(Gamepedia.getInstance());
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.game, context.findViewById(R.id.game_details_layout));

        // Sets animation for the layout
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        layout.startAnimation(animation);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int height = (int) (screenHeight * 0.87);

        window = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, height, true);
        window.setFocusable(true);
        window.showAtLocation(layout, Gravity.CENTER, 0, 0);


        ImageView gameDetailsImage = layout.findViewById(R.id.gameImage);
        TextView nameDetailsText = layout.findViewById(R.id.nameText);
        TextView metacriticDetailsText = layout.findViewById(R.id.metacriticText);
        TextView releaseDateDetailsText = layout.findViewById(R.id.releaseDateText);
        TextView detailsDescription = layout.findViewById(R.id.descriptionText);
        ImageView favoriteDetailsImage = layout.findViewById(R.id.favoriteImage);
        ImageView closeButton = layout.findViewById(R.id.close_button);

        fetchImage(gameItem.getImage(), gameDetailsImage, context);
        nameDetailsText.setText(gameItem.getName());
        metacriticDetailsText.setText(gameItem.getMetacritic());
        releaseDateDetailsText.setText(gameItem.getReleaseDate());
        detailsDescription.setText(gameItem.getDescription());
        favoriteDetailsImage.setOnClickListener(v -> {
            boolean isFavorite = !gameItem.isFavorite();
            gameItem.setFavorite(isFavorite);
            // TODO: Fix bug with drawable not persistent
            favoriteDetailsImage.setImageResource(gameItem.isFavorite() ? R.drawable.filled_heart : R.drawable.unfilled_heart);
            // Update game in database
            new Thread(() -> gameDatabase.gameDAO().updateGame(gameItem)).start();
        });


        closeButton.setOnClickListener(v -> {
            Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.slide_down);
            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation1) { }

                @Override
                public void onAnimationEnd(Animation animation1) {
                    new Handler().post(() -> window.dismiss());
                }

                @Override
                public void onAnimationRepeat(Animation animation1) { }
            });
            layout.startAnimation(animation1);
        });
    }
}
