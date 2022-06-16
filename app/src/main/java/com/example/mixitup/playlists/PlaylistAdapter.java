package com.example.mixitup.playlists;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mixitup.R;
import com.example.mixitup.data.Playlist;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistHolder> {

    private Playlist[] playlists = new Playlist[0];
    private boolean[] playlistActive;
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

        MaterialCardView card = holder.itemView.findViewById(R.id.cardPlaylistItem);
        card.setOnClickListener(click -> {
            playlistActive[position] = !playlistActive[position];
            if(playlistActive[position])
                card.setCardBackgroundColor(p.getResources().getColor(R.color.btnBg));
            else
                card.setCardBackgroundColor(p.getResources().getColor(R.color.mainBg));
        });

        if(playlistActive[position])
            card.setCardBackgroundColor(p.getResources().getColor(R.color.btnBg));
        else
            card.setCardBackgroundColor(p.getResources().getColor(R.color.mainBg));
    }

    @Override
    public int getItemCount() {
        return playlists.length;
    }

    public void setData(Playlist[] playlistData, boolean[] activePlaylists) {
        playlists = playlistData;
        playlistActive = activePlaylists;
        notifyDataSetChanged();
    }

    public String[] getSelectedPlaylists() {
        ArrayList<String> temp = new ArrayList<>();
        for(int i = 0; i < playlistActive.length; i++) {
            if(playlistActive[i])
                temp.add(playlists[i].id);
        }
        return temp.toArray(new String[temp.size()]);
    }
}
