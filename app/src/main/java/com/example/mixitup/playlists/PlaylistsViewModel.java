package com.example.mixitup.playlists;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mixitup.data.Playlist;
import com.example.mixitup.data.Repository;

import java.util.HashMap;

public class PlaylistsViewModel extends AndroidViewModel {

    public PlaylistsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<HashMap<String, Playlist>> getActivePlaylists() {
        return Repository.instance.getActivePlaylistLiveData();
    }
}
