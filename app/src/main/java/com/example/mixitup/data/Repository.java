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
    private MutableLiveData<HashMap<String, Playlist>> activePlaylists;
    private MutableLiveData<ArrayList<Track>> mixedTracklist;
    private HashMap<String, Integer> trackCounts;

    public Repository() {
        instance = this;
        spotifyConnection = new SpotifyConnection();
        playlists = new MutableLiveData<>();
        activePlaylists = new MutableLiveData<>();
        mixedTracklist = new MutableLiveData<>();
        trackCounts = new HashMap<>();
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

        HashMap<String, Playlist> temp = playlists.getValue();
        Playlist playlist = temp.get(playlistId);
        spotifyConnection.getPlaylistTracks(playlistId, playlist.numTracks, result -> {
            playlist.tracks = result;
            callback.onGetPlaylistTracks(result);
        });
    }

    public LiveData<HashMap<String, Playlist>> getPlaylistLiveData() { return playlists; }

    public LiveData<HashMap<String, Playlist>> getActivePlaylistLiveData() { return activePlaylists; }

    public void setActivePlaylists(String[] activeIds) {
        HashMap<String, Playlist> temp = playlists.getValue();
        for(String id : activeIds)
            temp.get(id).isActive = true;

        HashMap<String, Playlist> updatedActivePlaylists = new HashMap<>();
        for(Playlist playlist : temp.values()) {
            if(playlist.isActive)
                updatedActivePlaylists.put(playlist.id, playlist);
        }
        activePlaylists.setValue(updatedActivePlaylists);
    }

    public void setMixedTracklist(ArrayList<Track> tracklist) { mixedTracklist.setValue(tracklist); }

    public LiveData<ArrayList<Track>> getMixedTracklistLiveData() { return mixedTracklist; }

    public void setTrackCounts(HashMap<String, Integer> trackCounts) { this.trackCounts = trackCounts; }

    public HashMap<String, Integer> getTrackCounts() { return trackCounts; }
}
