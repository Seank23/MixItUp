package com.example.mixitup.playlists;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mixitup.MainActivity;
import com.example.mixitup.R;
import com.example.mixitup.data.Playlist;

public class PlaylistsFragment extends Fragment {

    private PlaylistsViewModel viewModel;
    private PlaylistAdapter playlistAdapter;
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

        playlistAdapter = new PlaylistAdapter(this);
        RecyclerView rvActivePlaylists = getView().findViewById(R.id.rvActivePlaylists);
        rvActivePlaylists.setAdapter(playlistAdapter);
        rvActivePlaylists.setLayoutManager(new LinearLayoutManager(this.getContext()));

        viewModel.getActivePlaylists().observe(getViewLifecycleOwner(), playlists -> {
            playlistAdapter.setData(playlists.values().toArray(new Playlist[playlists.values().size()]));
        });

        getView().findViewById(R.id.fbtnAddPlaylist).setOnClickListener(click -> {
            p.navAddPlaylist();
        });
    }
}