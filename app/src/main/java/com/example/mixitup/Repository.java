package com.example.mixitup;

import com.example.mixitup.data.Playlist;
import com.example.mixitup.data.User;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.ArrayList;

public class Repository {

    private SpotifyConnection spotifyConnection;
    private User currentUser;
    private ArrayList<Playlist> playlists;

    public Repository() {
        spotifyConnection = new SpotifyConnection();
        playlists = new ArrayList<>();
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

    public void getUserPlaylists(SpotifyConnection.APIGetPlaylistsCallback callback) {
        spotifyConnection.getUserPlaylists(result -> {
            playlists.addAll(result);
            callback.onGetPlaylists(playlists);
        });
    }
}
