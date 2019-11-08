package main.model;

import java.util.ArrayList;

public class SongLibrary {
    private ArrayList<Song> songs;
    private String imagePath;
    private String name = "Song Library";

    public SongLibrary(ArrayList songs) {
        this.songs = songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public String getName() {
        return name;
    }
}
