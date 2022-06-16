package com.example.mixitup.playlists;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mixitup.data.Playlist;
import com.example.mixitup.data.Repository;
import com.example.mixitup.data.SpotifyConnection;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddPlaylistViewModel extends AndroidViewModel {

    public AddPlaylistViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<HashMap<String, Playlist>> getPlaylists() {
        return Repository.instance.getPlaylistLiveData();
    }

    public void fetchUserPlaylists() {
        Repository.instance.fetchUserPlaylists();
    }

    public void fetchPlaylistTracks(String playlistId, SpotifyConnection.APIGetPlaylistTracksCallback callback) {
        Repository.instance.fetchPlaylistTracks(playlistId, callback);
    }

    public void setActivePlaylists(String[] activeIds) { Repository.instance.setActivePlaylists(activeIds); }

    public HashMap<String, Playlist> getActivePlaylists() {
        HashMap<String, Playlist> playlists = Repository.instance.getActivePlaylistLiveData().getValue();
        if(playlists == null)
            return new HashMap<>();
        return  playlists;
    }

    public void checkTracksFetched(String[] selectedIds, SpotifyConnection.APIVoidCallback callback) {

        HashMap<String, Playlist> playlists = Repository.instance.getActivePlaylistLiveData().getValue();
        for(int i = 0; i < selectedIds.length; i++) {
            if(playlists.get(selectedIds[i]).tracks == null) {
                int finalI = i;
                System.out.println("Fetching tracks: " + selectedIds[i]);
                fetchPlaylistTracks(selectedIds[i], result -> {
                    if(finalI == selectedIds.length - 1) {
                        callback.onReturn();
                    }
                });
            } else if(i == selectedIds.length - 1) {
                callback.onReturn();
            }
        }
    }
}
