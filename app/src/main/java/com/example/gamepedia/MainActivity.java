package com.example.gamepedia;




import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;

import com.example.gamepedia.GameFiles.GameAdapter;
import com.example.gamepedia.GameFiles.GameHeaderAdapter;

import com.example.gamepedia.GameFiles.GameViewModel;

public class MainActivity extends AppCompatActivity {
    RecyclerView gameRecyclerView;
    RecyclerView headerRecyclerView;
    SnapHelper snapHelper = new PagerSnapHelper();
    private GameAdapter gameAdapter;
    private GameHeaderAdapter gameHeaderAdapter;
    private GameViewModel gameViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameRecyclerView = findViewById(R.id.game_list_body);
        headerRecyclerView = findViewById(R.id.game_list_header);

        snapHelper.attachToRecyclerView(headerRecyclerView);

        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        gameViewModel.getGameItemsLiveData().observe(this, gameItems ->  {
            gameAdapter = new GameAdapter(MainActivity.this, gameItems);
            gameRecyclerView.setAdapter(gameAdapter);
            gameRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));

            gameHeaderAdapter = new GameHeaderAdapter(MainActivity.this, gameItems);
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
        gameViewModel = null;
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