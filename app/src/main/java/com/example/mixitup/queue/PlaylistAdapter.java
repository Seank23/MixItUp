package com.example.mixitup.queue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mixitup.R;
import com.example.mixitup.add_playlist.AddPlaylistFragment;
import com.example.mixitup.data.Playlist;

import java.util.ArrayList;
import java.util.Collection;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistHolder> {

    private Playlist[] playlists = new Playlist[0];
    private Fragment p;

    public PlaylistAdapter(Fragment parent) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
        return new PlaylistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistHolder holder, int position) {

        TextView name = holder.itemView.findViewById(R.id.lblPlaylistName);
        name.setText(playlists[position].name);
        TextView author = holder.itemView.findViewById(R.id.lblPlaylistAuthor);
        author.setText(playlists[position].author);
        Button tracks = holder.itemView.findViewById(R.id.btnPlaylistTracks);
        tracks.setText(playlists[position].numTracks + " tracks");

        tracks.setOnClickListener(click -> {
            ((AddPlaylistFragment) p).showTracks(playlists[position]);
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
