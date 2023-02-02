package com.example.gamepedia.DatabaseFiles;

import androidx.room.Database;

import com.example.gamepedia.GameFiles.GameItem;

/**
 * Database class using Room library
 * */
@Database(entities = {GameItem.class}, version = 1)
public abstract class GameDatabase {
    public GameDAO gameDAO;
}
