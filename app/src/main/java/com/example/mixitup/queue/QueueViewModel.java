package com.example.mixitup.queue;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mixitup.data.Playlist;
import com.example.mixitup.data.Repository;
import com.example.mixitup.data.Track;

import java.util.ArrayList;
import java.util.HashMap;

public class QueueViewModel extends AndroidViewModel {

    public QueueViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ArrayList<Track>> getMixedTracklist() {
        return Repository.instance.getMixedTracklistLiveData();
    }
}
