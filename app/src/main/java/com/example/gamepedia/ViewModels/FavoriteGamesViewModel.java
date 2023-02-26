package com.example.gamepedia.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gamepedia.DatabaseFiles.DatabaseSingleton;
import com.example.gamepedia.DatabaseFiles.GameDatabase;
import com.example.gamepedia.GameFiles.GameItem;
import com.example.gamepedia.Gamepedia;

import java.util.List;

public class FavoriteGamesViewModel extends ViewModel {
    private MutableLiveData<List<GameItem>> liveData;
    private final GameDatabase db;

    public FavoriteGamesViewModel() {
        liveData = new MutableLiveData<>();
        db = DatabaseSingleton.getInstance(Gamepedia.getInstance());
        fetchFavoriteGames();
    }

    public LiveData<List<GameItem>> getFavoriteGamesLiveData() { return liveData; }

    private void fetchFavoriteGames() {
        new Thread(() -> {
            List<GameItem> favoriteGames = db.gameDAO().getFavoriteGames();
            liveData.postValue(favoriteGames);
        }).start();
    }
}
