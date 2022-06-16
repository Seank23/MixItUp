package com.example.mixitup.queue;

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
import com.example.mixitup.playlists.PlaylistAdapter;

public class QueueFragment extends Fragment {

    private QueueViewModel viewModel;
    private PlaylistAdapter playlistAdapter;
    private MainActivity p;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewModel  = new ViewModelProvider(this).get(QueueViewModel.class);
        p = (MainActivity) getActivity();
        return inflater.inflate(R.layout.fragment_queue, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        playlistAdapter = new PlaylistAdapter(this);
        RecyclerView rvPlaylistQueue = getView().findViewById(R.id.rvSongQueue);
        rvPlaylistQueue.setAdapter(playlistAdapter);
        rvPlaylistQueue.setLayoutManager(new LinearLayoutManager(this.getContext()));

        viewModel.getActivePlaylists().observe(getViewLifecycleOwner(), playlists -> {
            playlistAdapter.setData(playlists.values().toArray(new Playlist[playlists.values().size()]), new boolean[playlists.size()]);
        });
    }
}