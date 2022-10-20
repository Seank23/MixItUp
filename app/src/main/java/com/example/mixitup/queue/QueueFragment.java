package com.example.mixitup.queue;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mixitup.MainActivity;
import com.example.mixitup.R;
import com.example.mixitup.data.Track;

public class QueueFragment extends Fragment {

    private QueueViewModel viewModel;
    private TrackAdapter trackAdapter;
    private MainActivity p;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewModel  = new ViewModelProvider(this).get(QueueViewModel.class);
        p = (MainActivity) getActivity();
        return inflater.inflate(R.layout.fragment_queue, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        trackAdapter = new TrackAdapter(this);
        RecyclerView rvPlaylistQueue = getView().findViewById(R.id.rvSongQueue);
        rvPlaylistQueue.setAdapter(trackAdapter);
        rvPlaylistQueue.setLayoutManager(new LinearLayoutManager(this.getContext()));
        trackAdapter.setData(new Track[0]);

//        viewModel.handleTrackEnding(this);

        viewModel.getMixedTracklist().observe(getViewLifecycleOwner(), tracks -> {
            trackAdapter.setData(tracks.toArray(new Track[tracks.size()]));
            ((TextView)getView().findViewById(R.id.lblPlayerName)).setText(tracks.get(0).title);
            ((TextView)getView().findViewById(R.id.lblPlayerArtist)).setText(tracks.get(0).artist);
            ((ImageButton)getView().findViewById(R.id.ibtnPlay)).setImageResource(R.drawable.ic_play);
        });

        ImageButton btnPlay = getView().findViewById(R.id.ibtnPlay);
        btnPlay.setOnClickListener(click -> {
            viewModel.togglePlayState();
            if(viewModel.getPlayState())
                btnPlay.setImageResource(R.drawable.ic_pause);
            else
                btnPlay.setImageResource(R.drawable.ic_play);
        });

        getView().findViewById(R.id.ibtnSkip).setOnClickListener(click -> {
            Track nextTrack = viewModel.skipTrack();
            ((TextView)getView().findViewById(R.id.lblPlayerName)).setText(nextTrack.title);
            ((TextView)getView().findViewById(R.id.lblPlayerArtist)).setText(nextTrack.artist);
            btnPlay.setImageResource(R.drawable.ic_pause);
        });
    }

    public void updatePlayingTrack(Track track) {
        ((TextView)getView().findViewById(R.id.lblPlayerName)).setText(track.title);
        ((TextView)getView().findViewById(R.id.lblPlayerArtist)).setText(track.artist);
        ((ImageButton)getView().findViewById(R.id.ibtnPlay)).setImageResource(R.drawable.ic_pause);
    }
}