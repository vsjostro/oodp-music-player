package main.model;

public class Song {

    private String name;
    private int id;
    private String artist;
    private String songPath;
    private String imagePath;

    static int gid = 0;

    public Song(String name, String artist, String songPath) {
        this.name = name;
        this.artist = artist;
        this.songPath = songPath;
        this.id = gid;
        gid++;
    }

    public Song(String name, String artist, String songPath, String imagePath) {
        this.name = name;
        this.artist = artist;
        this.songPath = songPath;
        this.id = gid;
        this.imagePath = imagePath;
        gid++;
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
}
