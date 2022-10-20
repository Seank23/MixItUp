package com.example.mixitup.data;

public class Track {

    public String id;
    public String title;
    public String artist;

    public Track(String id, String title, String artist) {
        this.id = id;
        this.title = title;
        this.artist = artist;
    }

    public String getId() { return id; }
}
