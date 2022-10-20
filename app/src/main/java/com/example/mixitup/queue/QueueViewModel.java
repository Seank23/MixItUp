package com.example.mixitup.queue;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mixitup.data.Playlist;
import com.example.mixitup.data.Repository;
import com.example.mixitup.data.SpotifyConnection;
import com.example.mixitup.data.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class QueueViewModel extends AndroidViewModel {

    private boolean playState = false;
    private int currentTrack = 0;
    private boolean trackStarted = false;
    private long timer = 0;


    public QueueViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ArrayList<Track>> getMixedTracklist() {
        return Repository.instance.getMixedTracklistLiveData();
    }

    public boolean getPlayState() { return  playState; }

    public void setPlayState(boolean state) {
        playState = state;
        handlePlayState();
    }

    public void togglePlayState() {
        playState = !playState;
        handlePlayState();
    }

    public Track skipTrack() {
        currentTrack++;
        Track nextTrack = getMixedTracklist().getValue().get(currentTrack);
        Repository.instance.playTrack(nextTrack.id);
        return nextTrack;
    }

    private void handlePlayState() {
        if (playState) {
            Repository.instance.queueTracks(getMixedTracklist().getValue().stream().map(Track::getId).collect(Collectors.toList()));
//            Repository.instance.playTrack(getMixedTracklist().getValue().get(currentTrack).id);
            trackStarted = true;
        }
        else
            Repository.instance.pauseTrack();
    }

    public void handleTrackEnding(QueueFragment fragment) {
        Repository.instance.subscribeToPlayer(playerState -> {
            String curTrackId = getMixedTracklist().getValue().get(currentTrack).id;
            if(System.currentTimeMillis() - timer > 1000 && trackStarted) {
                if(!playerState.track.uri.equals("spotify:track:" + curTrackId)) {
                    timer = System.currentTimeMillis();
                    skipTrack();
                    fragment.updatePlayingTrack(getMixedTracklist().getValue().get(currentTrack));
                }
            }
        });
    }
}
