package com.example.gamepedia.DatabaseFiles;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gamepedia.GameFiles.GameItem;

import java.util.List;

/**
 * Data Access Object (DAO) class to handle database operations
 * */
@Dao
public abstract class GameDAO {
    @Query("SELECT * FROM GAMES")
    public abstract List<GameItem> getAllGames();
    @Insert
    public abstract void insert(GameItem gameItem);
    @Update
    public abstract void update(GameItem gameItem);

    public void addGameItem(GameItem gameItem) {
        List<GameItem> existingGames = getAllGames();
        for (GameItem existingGame : existingGames) {
            if (existingGame.getId().equals(gameItem.getId())) {
                update(gameItem);
                return;
            }
        }
        insert(gameItem);
    }

    public void updateGameItem(GameItem gameItem) { update(gameItem); }
}
