package com.example.gamepedia;

import static com.example.gamepedia.Constants.API_KEY;
import static com.example.gamepedia.Constants.API_URL;
import static com.example.gamepedia.Constants.page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.room.Transaction;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import com.example.gamepedia.DatabaseFiles.AppExecutors;
import com.example.gamepedia.DatabaseFiles.DatabaseSingleton;
import com.example.gamepedia.DatabaseFiles.GameDatabase;
import com.example.gamepedia.GameFiles.GameAdapter;
import com.example.gamepedia.GameFiles.GameHeaderAdapter;
import com.example.gamepedia.GameFiles.GameItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    // Mainly variables used for UI updates
    RecyclerView gameRecyclerView;
    RecyclerView headerRecyclerView;
    private GameAdapter gameAdapter;
    private GameHeaderAdapter gameHeaderAdapter;
    // List of GameItem objects
    ArrayList<GameItem> gameItemsList;
    private GameDatabase gameDatabase;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameRecyclerView = findViewById(R.id.game_list_body);
        headerRecyclerView = findViewById(R.id.game_list_header);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(headerRecyclerView);
        gameItemsList = new ArrayList<>();

        gameDatabase = DatabaseSingleton.getInstance(this);
        fetchGames();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateGameView();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameDatabase.close();
    }


    private void updateGameView() {
        MainActivity.this.runOnUiThread(() -> {
            // Body games list
            gameAdapter = new GameAdapter(MainActivity.this, gameItemsList);
            gameRecyclerView.setAdapter(gameAdapter);
            gameRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
            // Header games list
            gameHeaderAdapter = new GameHeaderAdapter(MainActivity.this, gameItemsList);
            headerRecyclerView.setAdapter(gameHeaderAdapter);
            headerRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
            // Replace gameList with the items from the database
            AppExecutors.getInstance().diskIO().execute(() -> {
                gameItemsList.addAll(gameDatabase.gameDAO().getAllGames());
                Collections.shuffle(gameItemsList);
            });
            gameAdapter.notifyDataSetChanged();
            gameHeaderAdapter.notifyDataSetChanged();
        });
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
        final List<GameItem> gameList = new ArrayList<>();
        final Request request = new Request.Builder().url(API_URL + "&page_size=20&page=" + page + "&key=" + API_KEY).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<GameItem> gameItems = gameDatabase.gameDAO().getAllGames();
                    if (isDataUpToDate(gameItems.get(0).getTimeStamp())) {
                        // Use data from the database to update the UI
                        gameItemsList.clear();
                        gameItemsList.addAll(gameItems);
                        updateGameView();
                    }
                    else {
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
                JSONObject game = resultsArray.getJSONObject(i);
                String id = game.getString("id");
                GameItem gameItem = gameDatabase.gameDAO().getGameById(id);
                if (gameItem == null) {
                    final Request innerRequest = new Request.Builder().url("https://api.rawg.io/api/games/" + id + "?key=" + Constants.API_KEY).build();
                    try (Response responseGameDetails = client.newCall(innerRequest).execute()) {
                        if (!responseGameDetails.isSuccessful()) {
                            throw new IOException("Unexpected code " + responseGameDetails);
                        }
                        // Game not found in database
                        String name = game.getString("name");
                        String rating = Double.toString(game.getDouble("rating"));
                        String metacritic = Integer.toString(game.optInt("metacritic", 0));
                        String image = game.getString("background_image");
                        String released = game.getString("released");
                        String description = "";
                        if (game.has("description")) {
                            description = Html.fromHtml(game.getString("description"),
                                    Html.FROM_HTML_MODE_COMPACT).toString();
                        }
                        else {
                            description = "No description available";
                        }
                        gameList.add(new GameItem(id, name, image, description, rating, metacritic, released, false, System.currentTimeMillis()));
                    } catch (JSONException e) {
                        throw new JSONException("Error parsing JSON response: " + e.getMessage());
                    }
                }
                else {
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
            gameItemsList.clear();
            gameItemsList.addAll(gameList);
            updateGameView();
        }
    }
    /* private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE GAMES ADD COLUMN LAST_UPDATE INTEGER NOT NULL DEFAULT 0");
        }
    }; */
}