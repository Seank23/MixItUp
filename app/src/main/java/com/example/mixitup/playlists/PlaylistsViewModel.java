package com.example.mixitup.playlists;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mixitup.data.Playlist;
import com.example.mixitup.data.Repository;
import com.example.mixitup.data.Track;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class PlaylistsViewModel extends AndroidViewModel {

    public PlaylistsViewModel(@NonNull Application application) {
        super(application);
    }

    public void setTrackCounts(HashMap<String, Integer> trackCounts) { Repository.instance.setTrackCounts(trackCounts); }

    public HashMap<String, Integer> getTrackCounts() { return Repository.instance.getTrackCounts(); }

    public void setActivePlaylists(String[] activeIds) { Repository.instance.setActivePlaylists(activeIds); }

    public LiveData<HashMap<String, Playlist>> getActivePlaylists() {
        return Repository.instance.getActivePlaylistLiveData();
    }

    public boolean mixPlaylists() {

        HashMap<String, Playlist> activePlaylists = getActivePlaylists().getValue();
        if(activePlaylists == null)
            return false;
        if(activePlaylists.isEmpty())
            return false;
        HashMap<String, Integer> trackCounts = getTrackCounts();
        ArrayList<Track> tracklist = new ArrayList<>();
        for(String id : activePlaylists.keySet()) {
            ArrayList<Track> tracks = activePlaylists.get(id).tracks;
            int numTracks = activePlaylists.get(id).numTracks;
            Random rnd = new Random();
            ArrayList<Integer> usedVals = new ArrayList<>();
            int rndIndex = -1;
            for(int i = 0; i < trackCounts.get(id); i++) {
                while(rndIndex == -1 || usedVals.contains(rndIndex))
                    rndIndex = rnd.nextInt(numTracks);
                tracklist.add(tracks.get(rndIndex));
                usedVals.add(rndIndex);
            }
        }
        Collections.shuffle(tracklist);
        Repository.instance.setMixedTracklist(tracklist);
        return true;
    }
}
