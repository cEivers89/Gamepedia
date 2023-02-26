package com.example.gamepedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.gamepedia.Fragments.TopGamesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavView = findViewById(R.id.bottom_nav_view);

        // Retrieve top game fragment
        Fragment topGamesFragment = new TopGamesFragment();

        // Set the top games fragment as the default fragment
        // This is so top games isn't reloaded everytime we navigate to a different view
        getSupportFragmentManager().beginTransaction().add(R.id.top_games_fragment, topGamesFragment).commit();
        // Initiates the NavController
        initNav();
    }

    private void initNav() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController =navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavView, navController);
        bottomNavView.setOnNavigationItemSelectedListener(this);
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

    // Static Map to map menu item IDs to fragment IDs
    // Provides more flexible and scalable way to map menu item IDs to fragment IDs
    private static final Map<Integer, Integer> MENU_ID_TO_FRAGMENT_ID_MAP = new HashMap<Integer, Integer>() {{
        put(R.id.menu_home, R.id.recent_games_screen);
        put(R.id.menu_upcoming_games, R.id.upcoming_games_screen);
        put(R.id.menu_favorites, R.id.favorite_games_screen);
    }};

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Integer fragmentId = MENU_ID_TO_FRAGMENT_ID_MAP.get(item.getItemId());
        if (fragmentId != null) {
            Navigation.findNavController(this, R.id.nav_host_fragment)
                    .navigate(fragmentId, null, getNavOptions());
            item.setChecked(true);
            return true;
        }
        else {
            return false;
        }
    }

    private NavOptions getNavOptions() {
        return new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.slide_out_left)
                .setPopEnterAnim(R.anim.slide_in_left)
                .setPopExitAnim(R.anim.slide_out_right)
                .build();
    }
}
    /* private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE GAMES ADD COLUMN LAST_UPDATE INTEGER NOT NULL DEFAULT 0");
        }
    }; */