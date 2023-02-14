package com.example.gamepedia.GameFiles;

import static com.example.gamepedia.Constants.API_KEY;
import static com.example.gamepedia.Constants.API_URL;
import static com.example.gamepedia.Constants.page;

import android.text.Html;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Transaction;

import com.example.gamepedia.Constants;
import com.example.gamepedia.DatabaseFiles.DatabaseSingleton;
import com.example.gamepedia.DatabaseFiles.GameDatabase;
import com.example.gamepedia.Gamepedia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GameViewModel extends ViewModel {
    private MutableLiveData<List<GameItem>> gameItemsLiveData;
    private final GameDatabase gameDatabase;
    private final OkHttpClient client = new OkHttpClient();

    public GameViewModel() {
        gameItemsLiveData = new MutableLiveData<>();
        gameDatabase = DatabaseSingleton.getInstance(Gamepedia.getInstance());
        fetchGames();
    }

    public LiveData<List<GameItem>> getGameItemsLiveData() {
        return gameItemsLiveData;
    }

    private boolean isDataUpToDate(long timeStamp) {
        long now = System.currentTimeMillis();
        long diff = now - timeStamp;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds);
        long hours = TimeUnit.MINUTES.toHours(minutes);
        return hours < 24;
    }

    private void fetchGames() {
        List<GameItem> gameList = new ArrayList<>();
        final Request request = new Request.Builder().url(API_URL + "&page_size=20&page=" + page + "&key=" + API_KEY).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<GameItem> dbGameList = gameDatabase.gameDAO().getAllGames();
                    if (!dbGameList.isEmpty() && isDataUpToDate(dbGameList.get(0).getTimeStamp())) {
                        // Use data from the database to update the UI
                        gameList.addAll(dbGameList);
                        gameItemsLiveData.postValue(gameList);
                    } else {
                        Response response = client.newCall(request).execute();
                        if (!response.isSuccessful()) {
                            Log.e("fetchGames", "Unexpected code " + response);
                            return;
                        }
                        try (ResponseBody responseBody = response.body()) {
                            if (responseBody == null) {
                                Log.e("fetchGames", "Response body is null");
                                return;
                            }
                            JSONObject jsonGameListObject;
                            JSONArray resultsArray;
                            try {
                                jsonGameListObject = new JSONObject(responseBody.string());
                                resultsArray = jsonGameListObject.getJSONArray("results");
                            } catch (JSONException e) {
                                Log.e("fetchGames", e.getMessage());
                                return;
                            }
                            addGamesToDb(resultsArray, client, gameList);
                        }
                    }
                } catch (IOException e) {
                    Log.e("fetchGames", e.getMessage());
                }
            }
        }).start();
    }

    @Transaction
    private void addGamesToDb(JSONArray resultsArray, final OkHttpClient client, final List<GameItem> gameList) {
        // For every item in game_files, get attributes. Add to db
        for (int i = 0; i < resultsArray.length(); i++) {
            try {
                //JSONObject game = resultsArray.getJSONObject(i);
                JSONObject jsonGameObject = new JSONObject(resultsArray.get(i).toString());
                String id = jsonGameObject.getString("id");
                GameItem gameItem = gameDatabase.gameDAO().getGameById(id);
                if (gameItem == null) {
                    final Request request = new Request.Builder().url("https://api.rawg.io/api/games/" + id + "?key=" + Constants.API_KEY).build();
                    try (Response responseGameDetails = client.newCall(request).execute()) {
                        if (!responseGameDetails.isSuccessful()) {
                            throw new IOException("Unexpected code " + responseGameDetails);
                        }
                        JSONObject jsonGameDetailsObject = new JSONObject(responseGameDetails.body().string());
                        // Game not found in database
                        String name = jsonGameObject.getString("name");
                        String rating = Double.toString(jsonGameObject.getDouble("rating"));
                        String metacritic = Integer.toString(jsonGameObject.optInt("metacritic", 0));
                        String image = jsonGameObject.getString("background_image");
                        String released = jsonGameObject.getString("released");
                        String description;
                        description = Html.fromHtml(jsonGameDetailsObject.getString("description"), Html.FROM_HTML_MODE_LEGACY).toString();
                        gameList.add(new GameItem(id, name, image, description, rating, metacritic, released, false, System.currentTimeMillis()));
                    } catch (JSONException e) {
                        throw new JSONException("Error parsing JSON response: " + e.getMessage());
                    }
                } else {
                    gameList.add(gameItem);
                }
            } catch (JSONException e) {
                Log.e("addGamesToDb", e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (!gameList.isEmpty()) {
            gameDatabase.gameDAO().insertGames(gameList);
            gameItemsLiveData.postValue(gameList);
        }
    }
}
