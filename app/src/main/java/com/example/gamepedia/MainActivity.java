package com.example.gamepedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Transaction;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ListView gameListView;
    GameTemplate gameTemplate;
    ArrayList<GameItem> gameItemsList;
    //DatabaseDAO db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameListView = findViewById(R.id.game_list_body);
        gameItemsList = new ArrayList<>();
       // db = GameDatabase.getDatabase(MainActivity.this).gameDatabase();

        downloadDatabase();
        //new LoadDatabaseTask().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        downloadDatabase();
    }

    private void updateView() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameTemplate = new GameTemplate(MainActivity.this, gameItemsList);
                gameListView.setAdapter(gameTemplate);
            }
        });
    }

    /*private class LoadDatabaseTask extends AsyncTask<Void, Void, List<GameItem>> {
        @Override
        protected List<GameItem> doInBackground(Void... voids) {
            return db.getGameItemList();
        }

        protected void onPostExecute(List<GameItem> result) {
            gameItemsList = (ArrayList<GameItem>) result;
            gameTemplate = new GameTemplate(MainActivity.this, gameItemsList, db);
            gameListView.setAdapter(gameTemplate);
        }
    } */

    private void downloadDatabase() {
        final List<GameItem> gameList = new ArrayList<>();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url("https://api.rawg.io/api/games?key="
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
                    addGamesToDb(resultsArray, client, gameList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Transaction
    private void addGamesToDb(JSONArray resultsArray, final OkHttpClient client, final List<GameItem> gameList) {
        // For every item in game_files, get attributes. Add to db
        for (int i = 0; i < resultsArray.length(); i++) {
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
