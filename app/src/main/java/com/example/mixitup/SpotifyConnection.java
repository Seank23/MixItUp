package com.example.mixitup;

import android.content.Context;
import android.util.Log;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

import java.util.concurrent.Callable;

public class SpotifyConnection {

    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "88e0b9a9d20b4d9299ec788f630e0638";
    private static final String REDIRECT_URI = "http://com.yourdomain.yourapp/callback";

    private String token;
    private SpotifyAppRemote appRemote;

    public SpotifyConnection(String accessToken, SpotifyAppRemote spotifyAppRemote) {

        token = accessToken;
        appRemote = spotifyAppRemote;
        connected();
    }

    public static int getRequestCode() { return REQUEST_CODE; }

    public static String getClientId() { return CLIENT_ID; }

    public static String getRedirectUri() { return REDIRECT_URI; }


    private void connected() {
        // Play a playlist
        appRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");

        // Subscribe to PlayerState
        appRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }

    public void disconnect() {
        SpotifyAppRemote.disconnect(appRemote);
    }
}
