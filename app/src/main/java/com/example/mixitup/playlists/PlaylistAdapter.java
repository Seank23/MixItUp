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
        TextView author = holder.itemView.findViewById(R.id.lblPlaylistAuthor);
        Button tracks = holder.itemView.findViewById(R.id.btnPlaylistTracks);
        TextView noPlaylists = holder.itemView.findViewById(R.id.lblNoPlaylists);

        if(playlists[position].id == "Empty") {
            name.setVisibility(View.INVISIBLE);
            author.setVisibility(View.INVISIBLE);
            tracks.setVisibility(View.INVISIBLE);
            noPlaylists.setVisibility(View.VISIBLE);
            return;
        }

        name.setText(playlists[position].name);
        author.setText(playlists[position].author);
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
        if(playlistData.length > 0)
            playlists = playlistData;
        else {
            Playlist[] empty = new Playlist[] { new Playlist("Empty", "", "", 0) };
            playlists = empty;
        }
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
