package main.model;

import main.API.APIDetails;

public class Song {

    private String name;
    private int id;
    private String artist;
    private String songPath;
    private String imagePath;
    private APIDetails apiDetails;

    //private static int gid = 0;

    public Song(String userInput) throws Exception {
        this.apiDetails = new APIDetails(userInput);
    }

    public Song(String name, String artist, String songPath) {
        this.name = name;
        this.artist = artist;
        this.songPath = songPath;
        this.id = id;
        //gid++;
    }

    public Song(int id, String name, String artist, String songPath, String imagePath) {
        this.name = name;
        this.artist = artist;
        this.songPath = songPath;
        this.id = id;
        this.imagePath = imagePath;
        //gid++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public APIDetails getApiDetails() {
        return apiDetails;
    }
}
