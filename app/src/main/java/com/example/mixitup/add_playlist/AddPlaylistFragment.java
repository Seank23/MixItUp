package com.example.mixitup.add_playlist;

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
import com.example.mixitup.queue.PlaylistAdapter;
import com.example.mixitup.queue.QueueViewModel;

public class AddPlaylistFragment extends Fragment {

    private AddPlaylistViewModel viewModel;
    private PlaylistAdapter playlistAdapter;
    private MainActivity p;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewModel  = new ViewModelProvider(this).get(AddPlaylistViewModel.class);
        p = (MainActivity) getActivity();
        return inflater.inflate(R.layout.fragment_add_playlist, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        playlistAdapter = new PlaylistAdapter();
        RecyclerView rvYourPlaylists = getView().findViewById(R.id.rvYourPlaylists);
        rvYourPlaylists.setAdapter(playlistAdapter);
        rvYourPlaylists.setLayoutManager(new LinearLayoutManager(this.getContext()));

        viewModel.getPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            playlistAdapter.setData(playlists);
        });

        viewModel.fetchUserPlaylists();
    }
}