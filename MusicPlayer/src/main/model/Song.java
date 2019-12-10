package main.model;

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
    private String lyricsPath;


    /**
     * The constructor for Song.
     *
     * @param name      The name of the song
     * @param id        The unique id of the song
     * @param artist    The artist of the song
     * @param songPath  The path where the song file is located
     * @param imagePath The path where the art for the song is located
     */
    public Song(int id, String name, String artist, String songPath, String imagePath, String lyrics) {
        this.name = name;
        this.artist = artist;
        this.songPath = songPath;
        this.id = id;
        this.imagePath = imagePath;
        this.lyricsPath = lyrics;
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


    /**
     * Getter for the lyrics path of the song.
     *
     * @return lyricsPath
     */
    public String getLyricsPath() {
        return lyricsPath;
    }
}
