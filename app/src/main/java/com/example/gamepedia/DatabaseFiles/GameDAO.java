package com.example.gamepedia.DatabaseFiles;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.gamepedia.GameFiles.GameItem;

import java.util.List;

/**
 * Data Access Object (DAO) interface to handle database operations
 * */
@Dao
public interface GameDAO {
    @Query("SELECT * FROM GAMES")
    List<GameItem> getAllGames();

    @Insert
    void insertGames(GameItem... games);

    @Query("DELETE FROM GAMES")
    void deleteAllGames();
}
