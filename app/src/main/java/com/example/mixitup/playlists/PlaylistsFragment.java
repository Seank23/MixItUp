package com.example.mixitup.playlists;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mixitup.MainActivity;
import com.example.mixitup.R;
import com.example.mixitup.SwipeToDeleteCallback;
import com.example.mixitup.data.Playlist;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;

public class PlaylistsFragment extends Fragment {

    private PlaylistsViewModel viewModel;
    private ActivePlaylistAdapter playlistAdapter;
    private MainActivity p;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewModel  = new ViewModelProvider(this).get(PlaylistsViewModel.class);
        p = (MainActivity) getActivity();
        return inflater.inflate(R.layout.fragment_playlists, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        playlistAdapter = new ActivePlaylistAdapter(this);
        RecyclerView rvActivePlaylists = getView().findViewById(R.id.rvActivePlaylists);
        rvActivePlaylists.setAdapter(playlistAdapter);
        rvActivePlaylists.setLayoutManager(new LinearLayoutManager(this.getContext()));
        ItemTouchHelper touchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(playlistAdapter));
        touchHelper.attachToRecyclerView(rvActivePlaylists);
        playlistAdapter.setData(new ArrayList<>());

        viewModel.getActivePlaylists().observe(getViewLifecycleOwner(), playlists -> {
            playlistAdapter.setData(new ArrayList<>(playlists.values()));
        });

        getView().findViewById(R.id.fbtnAddPlaylist).setOnClickListener(click -> {
            p.navAddPlaylist();
        });

        getView().findViewById(R.id.btnMix).setOnClickListener(click -> {
            if(viewModel.mixPlaylists())
                p.navQueue();
            else
                Toast.makeText(requireContext(), "Active playlists are required to create a mix", Toast.LENGTH_SHORT).show();
        });
    }

    public void setTrackCounts(HashMap<String, Integer> trackCounts) { viewModel.setTrackCounts(trackCounts); }

    public HashMap<String, Integer> getTrackCounts() { return viewModel.getTrackCounts(); }

    public void setActivePlaylists(String[] activeIds) { viewModel.setActivePlaylists(activeIds); }
}