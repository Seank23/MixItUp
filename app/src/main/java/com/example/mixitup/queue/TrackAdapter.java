package com.example.mixitup.queue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mixitup.R;
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
        TextView artist = holder.itemView.findViewById(R.id.lblTrackArtist);
        TextView index = holder.itemView.findViewById(R.id.lblTrackIndex);
        TextView noTracks = holder.itemView.findViewById(R.id.lblNoTracks);

        if(tracks[position].id == "Empty") {
            name.setVisibility(View.INVISIBLE);
            artist.setVisibility(View.INVISIBLE);
            index.setVisibility(View.INVISIBLE);
            noTracks.setVisibility(View.VISIBLE);
            return;
        }

        name.setText(tracks[position].title);
        artist.setText(tracks[position].artist);
        index.setText("" + (position + 1));
    }

    @Override
    public int getItemCount() {
        return tracks.length;
    }

    public void setData(Track[] trackData) {
        if(trackData.length > 0)
            tracks = trackData;
        else {
            Track[] empty = new Track[] { new Track("Empty", "", "") };
            tracks = empty;
        }
        notifyDataSetChanged();
    }
}
