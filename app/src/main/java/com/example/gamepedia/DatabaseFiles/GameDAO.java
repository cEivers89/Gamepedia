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
    @Update
    public abstract void update(GameItem gameItem);
}
