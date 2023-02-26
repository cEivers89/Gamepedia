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
    @Query("SELECT * FROM GAMES WHERE release_date BETWEEN date('now', '-120 day') AND date('now', 'localtime') order by release_date desc")
    public abstract List<GameItem> getGames120Days();
    @Query("SELECT * from games where metacritic >= 80 and (RELEASE_DATE between date('now', '-1 year') and date('now', 'localtime'))")
    public abstract List<GameItem> getTopGames();
    @Query("SELECT * FROM GAMES WHERE RELEASE_DATE > date('now') order by release_date asc")
    public abstract List<GameItem> getUpcomingReleases();
    @Query("SELECT * FROM GAMES WHERE FAVORITE = 1")
    public abstract List<GameItem> getFavoriteGames();
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertGames(List<GameItem> gameItems);
    @Update
    public abstract void updateGame(GameItem gameItem);
}
