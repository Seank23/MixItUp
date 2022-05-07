package com.example.mixitup.queue;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mixitup.data.Playlist;
import com.example.mixitup.data.Repository;

import java.util.ArrayList;

public class QueueViewModel extends AndroidViewModel {

    public QueueViewModel(@NonNull Application application) {
        super(application);
    }

//    public LiveData<ArrayList<Playlist>> getPlaylists() {
//        return Repository.instance.getPlaylistLiveData();
//    }
}
