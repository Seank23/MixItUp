package com.example.mixitup.add_playlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mixitup.data.Playlist;
import com.example.mixitup.data.Repository;
import com.example.mixitup.data.SpotifyConnection;

import java.util.ArrayList;
import java.util.HashMap;

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
}
