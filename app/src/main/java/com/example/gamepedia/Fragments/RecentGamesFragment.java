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
import com.example.gamepedia.ViewModels.RecentGamesViewModel;
import com.example.gamepedia.R;

public class RecentGamesFragment extends Fragment {
    private GameAdapter adapter;

    // Required empty constructor
    public RecentGamesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout
        return inflater.inflate(R.layout.fragment_recent_games, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recent_games_recycler_view);

        // Retrieve recent games
        RecentGamesViewModel viewModel = new ViewModelProvider(this).get(RecentGamesViewModel.class);
        viewModel.getGameItemsLiveData().observe(getViewLifecycleOwner(), gameItems -> {
            adapter = new GameAdapter(getActivity(), gameItems);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Set adapter to null and detach it from RecyclerView
        RecyclerView recyclerView = getView().findViewById(R.id.recent_games_recycler_view);
        recyclerView.setAdapter(null);
    }
}
