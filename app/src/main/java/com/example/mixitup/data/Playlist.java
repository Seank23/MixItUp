package com.example.mixitup.data;

import java.util.ArrayList;

public class Playlist {

    public String id;
    public String name;
    public String author;
    public int numTracks;
    public ArrayList<Track> tracks;

    public Playlist(String id, String name, String author, int numTracks) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.numTracks = numTracks;
    }

    public void setTracks(ArrayList tracks) {
        this.tracks = tracks;
    }
}
