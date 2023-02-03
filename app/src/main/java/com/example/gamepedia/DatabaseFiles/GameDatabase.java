package com.example.gamepedia.DatabaseFiles;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gamepedia.GameFiles.GameItem;

/**
 * Database class using Room library
 * */
@Database(entities = {GameItem.class}, version = 2, exportSchema = false)
public abstract class GameDatabase extends RoomDatabase {
    public abstract GameDAO gameDAO();
}
