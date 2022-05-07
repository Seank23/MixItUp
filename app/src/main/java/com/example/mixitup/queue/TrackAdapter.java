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
import com.example.mixitup.data.Track;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackHolder> {

    private Track[] tracks = new Track[0];
    private Fragment p;

    public TrackAdapter(Fragment parent) {
        p = parent;
    }

    protected static class TrackHolder extends RecyclerView.ViewHolder {

        public TrackHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public TrackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item, parent, false);
        return new TrackHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackHolder holder, int position) {

        TextView name = holder.itemView.findViewById(R.id.lblTrackName);
        name.setText(tracks[position].title);
        TextView artist = holder.itemView.findViewById(R.id.lblTrackArtist);
        artist.setText(tracks[position].artist);
        TextView index = holder.itemView.findViewById(R.id.lblTrackIndex);
        index.setText("" + (position + 1));
    }

    @Override
    public int getItemCount() {
        return tracks.length;
    }

    public void setData(Track[] trackData) {
        tracks = trackData;
        notifyDataSetChanged();
    }
}
