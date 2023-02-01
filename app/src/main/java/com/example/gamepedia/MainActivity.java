package com.example.gamepedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Transaction;

import android.os.Bundle;
import android.text.Html;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView gameRecyclerView;
    GameAdapter gameAdapter;
    ArrayList<GameItem> gameItemsList;

    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameRecyclerView = findViewById(R.id.game_list_body);
        gameItemsList = new ArrayList<>();

        fetchGames();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchGames();
    }

    private void updateView() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameAdapter = new GameAdapter(MainActivity.this, gameItemsList);
                gameRecyclerView.setAdapter(gameAdapter);
                gameRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                gameAdapter.notifyDataSetChanged();
            }
        });
    }
    private void fetchGames() {

        final List<GameItem> gameList = new ArrayList<>();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url("https://api.rawg.io/api/games?page_size=100&key="
                + Constants.API_KEY).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    // Fetch game_files list as a JSON list
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
                    JSONObject jsonGameListObject;
                    JSONArray resultsArray;
                    try {
                        jsonGameListObject = new JSONObject(response.body().string());
                        resultsArray = jsonGameListObject.getJSONArray("results");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                    addGamesToUI(resultsArray, client, gameList);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Transaction
    private void addGamesToUI(JSONArray resultsArray, final OkHttpClient client, final List<GameItem> gameList) {
        // Randomize list of games
        Random random = new Random();
        int randomStart = random.nextInt(resultsArray.length());
        int randomEnd = randomStart + 20;
        if (randomEnd > resultsArray.length()) {
            randomEnd = resultsArray.length();
        }
        // For every item in game_files, get attributes. Add to db
        for (int i = randomStart; i < randomEnd; i++) {
            try {
                JSONObject jsonGameObject;
                jsonGameObject = new JSONObject(resultsArray.get(i).toString());
                String id = jsonGameObject.getString("id");
                // Fetch game files details
                final Request innerRequest = new Request.Builder().url("https://api.rawg.io/api/games/" + id + "?key=" + Constants.API_KEY).build();
                Response responseGameDetails = client.newCall(innerRequest).execute();
                if (!responseGameDetails.isSuccessful()) {
                    throw new IOException("Unexpected code " + responseGameDetails);
                }
                try {
                    JSONObject jsonGameDetailsObject = new JSONObject(responseGameDetails.body().string());
                    String name = jsonGameObject.getString("name");
                    String rating = Double.toString(jsonGameObject.getDouble("rating"));
                    String metacritic = Integer.toString(jsonGameObject.getInt("metacritic"));
                    String image = jsonGameObject.getString("background_image");
                    String released = jsonGameObject.getString("released");
                    String description = Html.fromHtml(jsonGameDetailsObject.getString("description"),
                            Html.FROM_HTML_MODE_COMPACT).toString();
                    // Add all items to the gameList
                    gameList.add(new GameItem(id, name, image, description, rating, metacritic, released, false));
                    gameItemsList.clear();
                    gameItemsList.addAll(gameList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        updateView();
    }
}
