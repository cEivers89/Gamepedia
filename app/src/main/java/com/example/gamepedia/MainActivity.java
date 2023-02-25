package com.example.gamepedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import com.example.gamepedia.Fragments.RecentGamesFragment;
import com.example.gamepedia.Fragments.TopGamesFragment;
import com.example.gamepedia.Fragments.UpcomingGamesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavView = findViewById(R.id.bottom_nav_view);

        // Retrieve top and recent game fragments
        Fragment topGamesFragment = new TopGamesFragment();
        Fragment recentGamesFragment = new RecentGamesFragment();

        // Set the top and recent games fragments as the default fragments
        getSupportFragmentManager().beginTransaction().add(R.id.top_games_fragment, topGamesFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.recent_games_fragment, recentGamesFragment).commit();

        // Handles clicks on BottomNavView items here based on item's ID
        bottomNavView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_home:
                    Intent home = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(home);
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                    finish();
                    return true;
                case R.id.menu_upcoming_games:
                    // TODO: Upcoming games functionality
                    // Switch to UpcomingGamesFragment
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                            .replace(R.id.recent_games_fragment, new UpcomingGamesFragment())
                            .commit();
                case R.id.menu_favorites:
                    // TODO: Favorites functionality
                    return true;
                case R.id.menu_search:
                    // TODO: Search functionality
                    return true;
            }
            return false;
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
    }
}
    /* private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE GAMES ADD COLUMN LAST_UPDATE INTEGER NOT NULL DEFAULT 0");
        }
    }; */