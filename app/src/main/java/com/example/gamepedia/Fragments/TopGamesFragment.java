package com.example.gamepedia.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.gamepedia.Adapters.TopGamesAdapter;
import com.example.gamepedia.ViewModels.TopGamesViewModel;
import com.example.gamepedia.R;

public class TopGamesFragment extends Fragment {
    private TopGamesAdapter adapter;

    // Required empty public constructor
    public TopGamesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_games, container, false);

        // Set up RecyclerView with LinearLayoutManager
        RecyclerView recyclerView = view.findViewById(R.id.top_games_recycler_view);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        // Retrieve top games using TopGamesViewModel
        TopGamesViewModel viewModel = new ViewModelProvider(this).get(TopGamesViewModel.class);
        viewModel.getTopGamesLiveData().observe(getViewLifecycleOwner(), gameItems -> {
            adapter = new TopGamesAdapter(getActivity(), gameItems);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        });
        return view;
    }
}



