package com.example.gamepedia.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamepedia.Adapters.GameAdapter;
import com.example.gamepedia.R;
import com.example.gamepedia.ViewModels.FavoriteGamesViewModel;

public class FavoriteGamesFragment extends Fragment {
    private GameAdapter adapter;

    // Required empty constructor
    public FavoriteGamesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_favorite_games, container, false);

        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.favorite_games_recycler_view);

        // Retrieve favorite games
        FavoriteGamesViewModel viewModel = new ViewModelProvider(this).get(FavoriteGamesViewModel.class);
        viewModel.getFavoriteGamesLiveData().observe(getViewLifecycleOwner(), gameItems -> {
            adapter = new GameAdapter(getActivity(), gameItems);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        });
        return view;
    }
}
