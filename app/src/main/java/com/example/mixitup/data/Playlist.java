package com.example.mixitup.data;

import java.util.ArrayList;

public class Playlist {

    public String id;
    public String name;
    public ArrayList<Track> tracks;

    public Playlist(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setTracks(ArrayList tracks) {
        this.tracks = tracks;
    }
}
