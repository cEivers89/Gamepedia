package com.example.gamepedia.DatabaseFiles;

import android.content.Context;

import androidx.room.Room;

public class DatabaseSingleton {
    private static GameDatabase instance;

    public static GameDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),GameDatabase.class, "game_db")
                    .build();
        }
        return instance;
    }
}
