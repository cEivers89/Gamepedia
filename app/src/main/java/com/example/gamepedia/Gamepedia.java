package com.example.gamepedia;

import android.app.Application;

/**
 * Defines a customer "Application" and creates a global singleton instance of it
 */
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
