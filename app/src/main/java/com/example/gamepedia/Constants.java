package com.example.gamepedia;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Random;

public class Constants {
    static Random random = new Random();
    static int maxPage = 200;
    public static int page = random.nextInt(maxPage) + 1;
    public static final String API_KEY = "&key=9a82209ea5e94110a541bc42039a77a1";

    public static final String API_URL = "https://api.rawg.io/api/games?";


    public static void fetchImage(String url, ImageView imageView, Context context) {
        Glide.with(context).load(url).centerCrop().into(imageView);
    }
}
