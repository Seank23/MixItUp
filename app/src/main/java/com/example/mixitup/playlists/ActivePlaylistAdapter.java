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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivePlaylistAdapter extends RecyclerView.Adapter<ActivePlaylistAdapter.PlaylistHolder> {

    private ArrayList<Playlist> playlists = new ArrayList<>();
    private Fragment p;
    private HashMap<String, Integer> trackCounts = new HashMap<>();
    private Playlist lastDeletedItem;
    private int lastDeletedItemPosition;

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

        if(trackCounts.size() < playlists.size() && !trackCounts.containsKey(playlists.get(position).id) && playlists.get(position).id != "Empty") {
            trackCounts.put(playlists.get(position).id, playlists.get(position).numTracks);
            ((PlaylistsFragment)p).setTrackCounts(trackCounts);
        }

        TextView name = holder.itemView.findViewById(R.id.lblPlaylistName);
        TextView author = holder.itemView.findViewById(R.id.lblPlaylistAuthor);
        TextView tracks = holder.itemView.findViewById(R.id.lblNumTracks);
        Slider tracksSlider = holder.itemView.findViewById(R.id.sldTracks);
        TextView noPlaylists = holder.itemView.findViewById(R.id.lblNoPlaylists);

        if(playlists.get(position).id == "Empty") {
            name.setVisibility(View.INVISIBLE);
            author.setVisibility(View.INVISIBLE);
            tracks.setVisibility(View.INVISIBLE);
            tracksSlider.setVisibility(View.INVISIBLE);
            noPlaylists.setVisibility(View.VISIBLE);
            return;
        }

        name.setText(playlists.get(position).name);
        author.setText(playlists.get(position).author);
        tracksSlider.setValueTo(playlists.get(position).numTracks);
        if(trackCounts.get(playlists.get(position).id) != null) {
            tracks.setText(trackCounts.get(playlists.get(position).id) + " tracks");
            tracksSlider.setValue(trackCounts.get(playlists.get(position).id));
        } else {
            tracks.setText(playlists.get(position).numTracks + " tracks");
            tracksSlider.setValue(playlists.get(position).numTracks);
        }
        tracksSlider.addOnChangeListener((slider, value, fromUser) -> {
            tracks.setText((int)value + " tracks");
            trackCounts.put(playlists.get(position).id, (int)value);
            ((PlaylistsFragment)p).setTrackCounts(trackCounts);
        });
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public void setData(ArrayList<Playlist> playlistData) {
        if(playlistData.size() == 0)
            playlistData.add(new Playlist("Empty", "", "", 0));
        playlists = playlistData;
        notifyDataSetChanged();
    }

    public Fragment getParent() { return p; }

    public void deleteItem(int position) {
        lastDeletedItem = playlists.get(position);
        lastDeletedItemPosition = position;
        playlists.remove(position);
        notifyItemRemoved(position);
        ArrayList<String> activeIds = new ArrayList<>();
        for(Playlist playlist : playlists) {
            activeIds.add(playlist.id);
        }
        ((PlaylistsFragment)p).setActivePlaylists(activeIds.toArray(new String[activeIds.size()]));
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        Snackbar snackbar = Snackbar.make(p.getView(), "Playlist removed", Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", v -> undoDelete());
        snackbar.show();
    }

    private void undoDelete() {
        playlists.add(lastDeletedItemPosition, lastDeletedItem);
        notifyItemInserted(lastDeletedItemPosition);
        ArrayList<String> activeIds = new ArrayList<>();
        for(Playlist playlist : playlists) {
            activeIds.add(playlist.id);
        }
        ((PlaylistsFragment)p).setActivePlaylists(activeIds.toArray(new String[activeIds.size()]));
    }
}
