package com.example.gamepedia;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public abstract class DatabaseDAO {
    @Insert
    public abstract void insert(GameItem gameItem);

    @Update
    public abstract void update(GameItem gameItem);

    @Query("SELECT * FROM GAMES WHERE ID = :id")
    public abstract GameItem getGameItemById(String id);

    @Query("SELECT * FROM GAMES")
    public abstract List<GameItem> getGameItemList();

    @Query("SELECT * FROM GAMES WHERE FAVORITE = 1")
    public abstract List<GameItem> getFavoriteGameItemList();

    @Query("SELECT * FROM GAMES WHERE NAME LIKE '%' || :searchString || '%' ")
    public abstract List<GameItem> getGameItemListByNameSearch(String searchString);

    @Query("SELECT COUNT(*) FROM GAMES")
    public abstract int itemCount();

    public void addGameItem(GameItem gameItem) {
        insert(gameItem);
    }
    public void updateGameItem(GameItem gameItem) {
        update(gameItem);
    }
}
