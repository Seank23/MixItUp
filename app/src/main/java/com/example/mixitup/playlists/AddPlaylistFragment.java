package com.example.mixitup.playlists;

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
import com.example.mixitup.queue.TrackAdapter;

import java.util.ArrayList;
import java.util.Set;

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
            Set<String> activeIds = viewModel.getActivePlaylists().keySet();
            boolean[] playlistsActive = new boolean[playlists.size()];
            String[] ids = playlists.keySet().toArray(new String[0]);
            for(int i = 0; i < ids.length; i++) {
                if(activeIds.contains(ids[i]))
                    playlistsActive[i] = true;
            }
            playlistAdapter.setData(playlists.values().toArray(new Playlist[playlists.values().size()]), playlistsActive);
        });

        getActivity().findViewById(R.id.ibtnBack).setOnClickListener(click -> {
            p.navPlaylists();
        });

        getActivity().findViewById(R.id.ibtnConfirm).setOnClickListener(click -> {
            String[] selectedIds = playlistAdapter.getSelectedPlaylists();
            viewModel.setActivePlaylists(selectedIds);
            viewModel.checkTracksFetched(selectedIds, () -> {
                getActivity().findViewById(R.id.pbSpinner).setVisibility(View.INVISIBLE);
                p.navPlaylists();
            });
            getActivity().findViewById(R.id.pbSpinner).setVisibility(View.VISIBLE);
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