package com.example.gamepedia.DatabaseFiles;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
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
    @Query("SELECT * FROM GAMES WHERE id = :id")
    public abstract GameItem getGameById(String id);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertGames(List<GameItem> gameItems);

    @Insert
    public abstract void insert(GameItem gameItem);

    @Update
    public abstract void update(GameItem gameItem);


    public void addGameItem(GameItem gameItem) {
        gameItem.setTimeStamp(System.currentTimeMillis());
        List<GameItem> existingGames = getAllGames();
        for (GameItem existingGame : existingGames) {
            if (existingGame.getId().equals(gameItem.getId())) {
                gameItem.setTimeStamp(System.currentTimeMillis());
                update(gameItem);
                return;
            }
        }
        gameItem.setTimeStamp(System.currentTimeMillis());
        insert(gameItem);
    }

    public void updateGameItem(GameItem gameItem) {
        gameItem.setTimeStamp(System.currentTimeMillis());
        update(gameItem);
    }
}
