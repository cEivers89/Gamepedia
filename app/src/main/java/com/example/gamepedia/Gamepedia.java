package com.example.gamepedia;

import android.app.Application;

public class Gamepedia extends Application {
    private static Gamepedia instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Gamepedia getInstance() {
        return instance;
    }
}
