package main.model;

import main.API.APIDetails;

/**
 * This is the class for the song. A song contains the path of
 * that songs path on the users machine, and other information
 * related to the song.
 *
 * @author Viktor
 * @version 1.0
 */

public class Song {

    private String name;
    private int id;
    private String artist;
    private String songPath;
    private String imagePath;
    private APIDetails apiDetails;


    public Song(String userInput) throws Exception {
        this.apiDetails = new APIDetails(userInput);
    }

    public Song(String name, String artist, String songPath) {
        this.name = name;
        this.artist = artist;
        this.songPath = songPath;
    }

    /**
     * The constructor for Song.
     *
     * @param name      The name of the song
     * @param id        The unique id of the song
     * @param artist    The artist of the song
     * @param songPath  The path where the song file is located
     * @param imagePath The path where the art for the song is located
     */
    public Song(int id, String name, String artist, String songPath, String imagePath) {
        this.name = name;
        this.artist = artist;
        this.songPath = songPath;
        this.id = id;
        this.imagePath = imagePath;
    }

    /**
     * Getter for the song name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the song name.
     *
     * @param name The new name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the song id.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the song artist.
     *
     * @return artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Setter for the song artist.
     *
     * @param artist The artist name to be set
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * Getter for the song path.
     *
     * @return songPath
     */
    public String getSongPath() {
        return songPath;
    }

    /**
     * Getter for the image path of the song.
     *
     * @return imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

}
