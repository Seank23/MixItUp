package com.example.mixitup.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.ArrayList;
import java.util.HashMap;

public class Repository {

    public static Repository instance;
    private SpotifyConnection spotifyConnection;
    private User currentUser;
    private MutableLiveData<HashMap<String, Playlist>> playlists;

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

    public void fetchPlaylistTracks(String playlistId, SpotifyConnection.APIGetPlaylistTracksCallback callback) {
        spotifyConnection.getPlaylistTracks(playlistId, result -> {
            HashMap<String, Playlist> temp = playlists.getValue();
            Playlist playlist = temp.get(playlistId);
            playlist.tracks = result;
            callback.onGetPlaylistTracks(result);
        });
    }

    public LiveData<HashMap<String, Playlist>> getPlaylistLiveData() {
        return playlists;
    }
}
