package main.model;

import java.util.ArrayList;

public class Playlist {

    private String playlistName;
    private String playlistDescription;
    private ArrayList<Song> songs;
    private String imagePath;

    public Playlist(String playlistName, String playlistDescription, ArrayList songs) {
        this.playlistName = playlistName;
        this.playlistDescription = playlistDescription;
        this.songs = songs;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public void setPlaylistDescription(String playlistDescription) {
        this.playlistDescription = playlistDescription;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public String getPlaylistDescription() {
        return playlistDescription;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }
}
