package com.example.mixitup.add_playlist;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.mixitup.MainActivity;
import com.example.mixitup.R;
import com.example.mixitup.data.Playlist;
import com.example.mixitup.data.Track;
import com.example.mixitup.queue.PlaylistAdapter;
import com.example.mixitup.queue.QueueViewModel;
import com.example.mixitup.queue.TrackAdapter;

import java.util.ArrayList;

public class AddPlaylistFragment extends Fragment {

    private AddPlaylistViewModel viewModel;
    private PlaylistAdapter playlistAdapter;
    private MainActivity p;
    private Dialog tracksDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewModel  = new ViewModelProvider(this).get(AddPlaylistViewModel.class);
        p = (MainActivity) getActivity();
        return inflater.inflate(R.layout.fragment_add_playlist, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        tracksDialog = new Dialog((getContext()));
        tracksDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tracksDialog.setCancelable(true);
        tracksDialog.setContentView(R.layout.tracks_modal);

        playlistAdapter = new PlaylistAdapter(this);
        RecyclerView rvYourPlaylists = getView().findViewById(R.id.rvYourPlaylists);
        rvYourPlaylists.setAdapter(playlistAdapter);
        rvYourPlaylists.setLayoutManager(new LinearLayoutManager(this.getContext()));

        viewModel.getPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            playlistAdapter.setData(playlists.values().toArray(new Playlist[playlists.values().size()]));
        });

        getActivity().findViewById(R.id.btnConfirmAdd).setOnClickListener(click -> {
            String[] selectedIds = playlistAdapter.getSelectedPlaylists();
            viewModel.setActivePlaylists(selectedIds);
            p.navQueue();
        });

        viewModel.fetchUserPlaylists();
    }

    public void showTracks(Playlist playlist) {

        viewModel.fetchPlaylistTracks(playlist.id, result -> {
            getActivity().runOnUiThread(() -> createTracksDialog(playlist.name, result));
        });
    }

    private void createTracksDialog(String playlistName, ArrayList<Track> tracks) {

        tracksDialog = new Dialog((getContext()));
        tracksDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tracksDialog.setCancelable(true);
        tracksDialog.setContentView(R.layout.tracks_modal);

        TrackAdapter trackAdapter = new TrackAdapter(this);
        RecyclerView rvTracks = tracksDialog.findViewById(R.id.rvPlaylistTracks);
        rvTracks.setAdapter(trackAdapter);
        rvTracks.setLayoutManager(new LinearLayoutManager(this.getContext()));
        trackAdapter.setData(tracks.toArray(new Track[tracks.size()]));

        ((TextView) tracksDialog.findViewById(R.id.lblPlaylistTitle)).setText(playlistName);
        tracksDialog.show();
    }
}