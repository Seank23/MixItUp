package com.example.mixitup;

import com.spotify.android.appremote.api.SpotifyAppRemote;

public class Repository {

    private SpotifyConnection spotifyConnection;

    public Repository() {

    }

    public void initSpotifyConnection(String accessToken, SpotifyAppRemote spotifyAppRemote) {
        spotifyConnection = new SpotifyConnection(accessToken, spotifyAppRemote);
    }
}
