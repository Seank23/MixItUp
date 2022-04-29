package com.example.mixitup.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.ArrayList;

public class Repository {

    public static Repository instance;
    private SpotifyConnection spotifyConnection;
    private User currentUser;
    private MutableLiveData<ArrayList<Playlist>> playlists;

    public Repository() {
        instance = this;
        spotifyConnection = new SpotifyConnection();
        playlists = new MutableLiveData<>();
    }

    public void initSpotifyConnection(String accessToken, SpotifyAppRemote spotifyAppRemote) {
        spotifyConnection.initAPI(accessToken, spotifyAppRemote);
    }

    public void getCurrentUser(SpotifyConnection.APIGetUserCallback callback) {
        spotifyConnection.getCurrentUser(result -> {
            currentUser = result;
            callback.onGetUser(currentUser);
        });
    }

    public void fetchUserPlaylists() {
        spotifyConnection.getUserPlaylists(result -> {
            playlists.postValue(result);
        });
    }

    public LiveData<ArrayList<Playlist>> getPlaylistLiveData() {
        return playlists;
    }
}
