package com.example.mixitup.playlists;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mixitup.R;
import com.example.mixitup.data.Playlist;
import com.google.android.material.slider.Slider;

import java.util.HashMap;

public class ActivePlaylistAdapter extends RecyclerView.Adapter<ActivePlaylistAdapter.PlaylistHolder> {

    private Playlist[] playlists = new Playlist[0];
    private Fragment p;
    private HashMap<String, Integer> trackCounts = new HashMap<>();

    public ActivePlaylistAdapter(Fragment parent) {
        p = parent;
    }

    protected static class PlaylistHolder extends RecyclerView.ViewHolder {

        public PlaylistHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public PlaylistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_playlist_item, parent, false);
        return new PlaylistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistHolder holder, int position) {

        if(trackCounts.size() == 0)
            trackCounts = ((PlaylistsFragment)p).getTrackCounts();

        if(trackCounts.size() < playlists.length && !trackCounts.containsKey(playlists[position].id)) {
            trackCounts.put(playlists[position].id, playlists[position].numTracks);
            ((PlaylistsFragment)p).setTrackCounts(trackCounts);
        }

        TextView name = holder.itemView.findViewById(R.id.lblPlaylistName);
        name.setText(playlists[position].name);
        TextView author = holder.itemView.findViewById(R.id.lblPlaylistAuthor);
        author.setText(playlists[position].author);
        TextView tracks = holder.itemView.findViewById(R.id.lblNumTracks);
        Slider tracksSlider = holder.itemView.findViewById(R.id.sldTracks);
        tracksSlider.setValueTo(playlists[position].numTracks);
        if(trackCounts.get(playlists[position].id) != null) {
            tracks.setText(trackCounts.get(playlists[position].id) + " tracks");
            tracksSlider.setValue(trackCounts.get(playlists[position].id));
        } else {
            tracks.setText(playlists[position].numTracks + " tracks");
            tracksSlider.setValue(playlists[position].numTracks);
        }
        tracksSlider.addOnChangeListener((slider, value, fromUser) -> {
            tracks.setText((int)value + " tracks");
            trackCounts.put(playlists[position].id, (int)value);
            ((PlaylistsFragment)p).setTrackCounts(trackCounts);
        });
    }

    @Override
    public int getItemCount() {
        return playlists.length;
    }

    public void setData(Playlist[] playlistData) {
        playlists = playlistData;
        notifyDataSetChanged();
    }
}
