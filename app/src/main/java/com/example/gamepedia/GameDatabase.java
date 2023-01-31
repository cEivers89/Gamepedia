package com.example.gamepedia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {GameItem.class}, version = 1, exportSchema = false)
public abstract class GameDatabase extends RoomDatabase {

    public abstract DatabaseDAO gameDatabase();

    private static GameDatabase gameDB;
    public static synchronized GameDatabase getDatabase(Context context) {
        if (gameDB == null) {
            gameDB = Room.databaseBuilder(context.getApplicationContext(),
                    GameDatabase.class, "game_files-db").build();
        }
        return gameDB;
    }
}
