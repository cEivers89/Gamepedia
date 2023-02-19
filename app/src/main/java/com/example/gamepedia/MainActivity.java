package com.example.gamepedia;




import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;
import android.util.Log;

import com.example.gamepedia.GameFiles.GameAdapter;
import com.example.gamepedia.GameFiles.GameHeaderAdapter;

import com.example.gamepedia.GameFiles.RecentGamesViewModel;
import com.example.gamepedia.GameFiles.TopGamesViewModel;

public class MainActivity extends AppCompatActivity {
    RecyclerView gameRecyclerView;
    RecyclerView headerRecyclerView;
    SnapHelper snapHelper = new PagerSnapHelper();
    private GameAdapter gameAdapter;
    private GameHeaderAdapter gameHeaderAdapter;
    private RecentGamesViewModel recentGamesViewModel;
    private TopGamesViewModel topGamesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameRecyclerView = findViewById(R.id.game_list_body);
        headerRecyclerView = findViewById(R.id.game_list_header);
        // Snaps headerRecyclerView into place
        snapHelper.attachToRecyclerView(headerRecyclerView);

        // Sets recent games in the body
        recentGamesViewModel = new ViewModelProvider(this).get(RecentGamesViewModel.class);
        recentGamesViewModel.getGameItemsLiveData().observe(this, gameItems ->  {
            gameAdapter = new GameAdapter(MainActivity.this, gameItems);
            gameRecyclerView.setAdapter(gameAdapter);
            gameRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
        });

        // Sets top games to the header
        topGamesViewModel = new ViewModelProvider(this).get(TopGamesViewModel.class);
        topGamesViewModel.getTopGamesLiveData().observe(this, headerItems -> {
            gameHeaderAdapter = new GameHeaderAdapter(MainActivity.this, headerItems);
            headerRecyclerView.setAdapter(gameHeaderAdapter);
            headerRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recentGamesViewModel = null;
        gameAdapter = null;
        gameHeaderAdapter = null;
        snapHelper.attachToRecyclerView(null);
    }
}
    /* private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE GAMES ADD COLUMN LAST_UPDATE INTEGER NOT NULL DEFAULT 0");
        }
    }; */