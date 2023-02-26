package com.example.gamepedia.ViewModels;

import static com.example.gamepedia.Constants.API_KEY;
import static com.example.gamepedia.Constants.API_URL;

import android.text.Html;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gamepedia.Constants;
import com.example.gamepedia.DatabaseFiles.DatabaseSingleton;
import com.example.gamepedia.DatabaseFiles.GameDatabase;
import com.example.gamepedia.GameFiles.GameItem;
import com.example.gamepedia.Gamepedia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class UpcomingGamesViewModel extends ViewModel {
    private MutableLiveData<List<GameItem>> upcomingGamesLiveData;
    private GameDatabase gameDatabase;
    private final OkHttpClient client = new OkHttpClient();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public UpcomingGamesViewModel() {
        upcomingGamesLiveData = new MutableLiveData<>();
        gameDatabase = DatabaseSingleton.getInstance(Gamepedia.getInstance());
        fetchUpcomingGames();
    }
    public LiveData<List<GameItem>> getUpcomingGamesLiveData() { return upcomingGamesLiveData; }

    private void fetchUpcomingGames() {
        // Calculates current time in milliseconds, 'days' days in milliseconds, and years in milliseconds
        Calendar calendar = Calendar.getInstance();
        // Date from tomorrow
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = calendar.getTime();
        String fromDate = dateFormat.format(tomorrow);

        // Date from a year from tomorrow
        calendar.add(Calendar.YEAR, 1);
        Date yearFromTmrow = calendar.getTime();
        String toDate = dateFormat.format(yearFromTmrow);

        final Request request = new Request.Builder().url(API_URL + "key=" + API_KEY + "&dates=" + fromDate + "," +
                toDate).build();
        new Thread(() -> {
            try {
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
                    List<GameItem> newGames = new ArrayList<>();

                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject jsonGameItem = resultsArray.getJSONObject(i);
                        String id = jsonGameItem.getString("id");
                        GameItem gameItem = gameDatabase.gameDAO().getGameById(id);
                        if (gameItem == null) {
                            final Request innerRequest = new Request.Builder().url("https://api.rawg.io/api/games/" + id + "?key=" + Constants.API_KEY).build();
                            try (Response responseGameDetails = client.newCall(innerRequest).execute()) {
                                if (!responseGameDetails.isSuccessful()) {
                                    throw new IOException("Unexpected code " + responseGameDetails);
                                }
                                JSONObject jsonGameDetailsObject = new JSONObject(responseGameDetails.body().string());
                                String name = jsonGameItem.getString("name");
                                String rating = Double.toString(jsonGameItem.getDouble("rating"));
                                String metacritic = Integer.toString(jsonGameItem.optInt("metacritic", 0));
                                String image = jsonGameItem.getString("background_image");
                                String released = jsonGameItem.getString("released");
                                String description;
                                description = Html.fromHtml(jsonGameDetailsObject.getString("description"), Html.FROM_HTML_MODE_LEGACY).toString();
                                newGames.add(new GameItem(id, name, image, description, rating, metacritic, released, false, System.currentTimeMillis()));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        // gameItem does exist in database. Check for updates
                        else {
                            boolean gameChanged = false;
                            String rating = Double.toString(jsonGameItem.getDouble("rating"));
                            if (!gameItem.getRating().equals(rating)) {
                                gameItem.setRating(rating);
                                gameChanged = true;
                            }
                            String metacritic = Integer.toString(jsonGameItem.optInt("metacritic"));
                            if (!gameItem.getMetacritic().equals(metacritic)) {
                                gameItem.setRating(metacritic);
                                gameChanged = true;
                            }
                            String image = jsonGameItem.getString("background_image");
                            if (!gameItem.getImage().equals(image)) {
                                gameItem.setImage(image);
                                gameChanged = true;
                            }
                            String released = jsonGameItem.getString("released");
                            if (!gameItem.getReleaseDate().equals(released)) {
                                gameItem.setReleaseDate(released);
                                gameChanged = true;
                            }
                            final Request descRequest = new Request.Builder().url("https://api.rawg.io/api/games/" + id + "?key=" + Constants.API_KEY).build();
                            try (Response responseGameDetails = client.newCall(descRequest).execute()) {
                                if (!responseGameDetails.isSuccessful()) {
                                    throw new IOException("Unexpected code " + responseGameDetails);
                                }
                                JSONObject jsonGameDetailsObject = new JSONObject(responseGameDetails.body().string());
                                String description = Html.fromHtml(jsonGameDetailsObject.getString("description"), Html.FROM_HTML_MODE_LEGACY).toString();
                                if (!gameItem.getDescription().equals(description)) {
                                    gameItem.setDescription(description);
                                    gameChanged = true;
                                }
                            }
                            if (gameChanged) {
                                gameDatabase.gameDAO().updateGame(gameItem);
                            }
                        }
                        if (!newGames.isEmpty()) {
                            gameDatabase.gameDAO().insertGames(newGames);
                        }
                        List<GameItem> upcomingReleases = gameDatabase.gameDAO().getUpcomingReleases();
                        upcomingGamesLiveData.postValue(upcomingReleases);

                    }
                } catch (IOException | JSONException e) {
                    Log.e("fetchGames", e.getMessage());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
