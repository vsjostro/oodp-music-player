package main.model;

import java.util.ArrayList;

/**
 * This class is for the playlist. The playlists contain the
 * songs that the user has added to the application. The Song
 * Library is a special kind of playlist, as it contains every
 * song that the user has added and the song library cannot be removed.
 *
 * @author Viktor
 * @version 1.0
 */

public class Playlist {

    private String playlistName;
    private String playlistDescription;
    private ArrayList<Song> songs;
    private int playlistID;
    private boolean isLibrary;


    /**
     * The constructior for the playlist.
     *
     * @param playlistID          The unique id for the playlist. SongLibrary has the id=1
     * @param playlistName        The name for the playlist
     * @param playlistDescription The description of the playlist, optional
     * @param isLibrary           If the playlist is the SongLibrary, this value is True. Otherwise it will be false.
     */
    public Playlist(int playlistID, String playlistName, String playlistDescription, boolean isLibrary) {
        this.playlistID = playlistID;
        this.playlistName = playlistName;
        this.playlistDescription = playlistDescription;
        this.isLibrary = isLibrary;
    }

    /**
     * Sets the playlist name, will be used when playlist name is edited
     * @param playlistName The name of the playlist
     */
    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    /**
     * Sets the playlist description, will be used when playlist description is edited
     * @param playlistDescription The description of the playlist
     */
    public void setPlaylistDescription(String playlistDescription) {
        this.playlistDescription = playlistDescription;
    }

    /**
     * Sets the current song list for the playlist
     * @param songs The list of songs for the playlist
     */
    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    /**
     * Getter for the playlist name.
     *
     * @return playlistName
     */
    public String getPlaylistName() {
        return playlistName;
    }

    /**
     * Getter for the playlist description.
     *
     * @return playlistDescription
     */
    public String getPlaylistDescription() {
        return playlistDescription;
    }

    /**
     * Getter for the song list.
     *
     * @return songs
     */
    public ArrayList<Song> getSongs() {
        return songs;
    }

    /**
     * Getter for the playlist id.
     *
     * @return playlistID
     */
    public int getPlaylistID() {
        return playlistID;
    }

}
