package main.db;

import main.model.Playlist;
import main.model.Song;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * MusicPlayerDatabase handles all the database related
 * functions.
 *
 * @author Viktor
 * @version 1.0*/

public class MusicPlayerDatabase {

    /**
     * This method removes a song selected in the table,
     * from the current playlist. The song is removed from
     * the SONGS_IN_PLAYLIST table. If that 'playlist' is
     * the song library, the song is also removed from the
     * SONGS table.
     * @param playlist This is the current playlist.
     * @param playlists This is all the existing playlists.
     * @param selectedSong This is the index for the selected song.
     * */
    public void removeSongFromPlaylist(Playlist playlist, ArrayList<Playlist> playlists, int selectedSong) {

        System.out.println(selectedSong + " selected song");
        //System.out.println(playlist.getSongs().get(selectedSong).getName());

        Connection c;
        Statement stmt;
        ResultSet rs;
        int playlistID = playlist.getPlaylistID();
        System.out.println("playlist " + playlistID);
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:musicplayer.db");
            //c.setAutoCommit(false);

            stmt = c.createStatement();
            stmt.execute("DELETE FROM PLAYLIST_SONGS WHERE PLAYLIST_ID=" + playlistID + " AND SONG_IN_PLAYLIST_ID=" + selectedSong + "");
            stmt.execute("UPDATE PLAYLIST_SONGS SET SONG_IN_PLAYLIST_ID = SONG_IN_PLAYLIST_ID - 1 WHERE SONG_IN_PLAYLIST_ID >" + selectedSong + " AND PLAYLIST_ID=" + playlistID + ";");
            stmt.execute("UPDATE PLAYLIST_SONGS SET SONG_ID = SONG_ID - 1 WHERE SONG_ID >" + selectedSong + " AND PLAYLIST_ID=" + playlistID + ";");

            if (playlistID == 1) {
                stmt.execute("DELETE FROM SONGS WHERE SONG_ID=" + selectedSong + "");
                stmt.execute("UPDATE SONGS SET SONG_ID = SONG_ID - 1 WHERE SONG_ID >" + selectedSong + ";\n");

                for (Playlist ps : playlists) {

                    stmt.execute("DELETE FROM PLAYLIST_SONGS WHERE PLAYLIST_ID=" + playlistID + " AND SONG_IN_PLAYLIST_ID=" + selectedSong + ";");

                }

            }

            stmt.close();
            c.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method removes a playlist from the existing playlists.
     * The playlist is removed from the PLAYLIST table based on the
     * playlist id.
     * @param playlistID The id if the playlist to be removed*/
    public void removePlaylist(int playlistID) {

        Connection c;
        Statement stmt;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:musicplayer.db");

            stmt = c.createStatement();
            stmt.execute("DELETE FROM PLAYLIST WHERE PLAYLIST_ID=" + playlistID + ";");
            stmt.execute("UPDATE PLAYLIST SET PLAYLIST_ID = PLAYLIST_ID - 1 WHERE PLAYLIST_ID >" + playlistID + ";");
            stmt.close();
            c.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method adds a playlist to PLAYLISTS table and
     * creates a new playlist in the list of playlists.
     * @param name The user chosen name of the playlist.
     * @param desc The description of the playlist, optional (can be empty)
     * @param playlists The list of all existing playlists
     * @return Playlist playlist*/

    public Playlist addPlaylist(String name, String desc, ArrayList<Playlist> playlists) {

        Connection c;
        Statement stmt;
        ResultSet rs;
        Playlist playlist = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:musicplayer.db");
            //c.setAutoCommit(false);

            stmt = c.createStatement();
            stmt.execute("UPDATE sqlite_sequence SET seq=(SELECT COUNT(*) FROM PLAYLIST) WHERE name='PLAYLIST'");
            stmt.execute("INSERT INTO PLAYLIST (NAME, DESCRIPTION) VALUES ('" + name + "','" + desc + "');");
            rs = stmt.executeQuery("SELECT * FROM PLAYLIST WHERE NAME='" + name + "';");

            int id = rs.getInt("PLAYLIST_ID");
            name = rs.getString("NAME");
            desc = rs.getString("DESCRIPTION");

            playlist = new Playlist(id, name, desc, false);
            playlists.add(playlist);

            stmt.close();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return playlist;
    }

    /**
     * This method adds a song to the library. The user
     * selects a song on with JFileChooser.
     * The song is added to the SONGS table and also to the
     * PLAYLIST_SONGS tables, so that it can be viewed in the
     * song library 'playlist'.
     * @param title The song title, chosen as the file name
     * @param artist The artist the user input, optional currently
     * @param songPath Path on disk of the file*/

    public void addSongToLibrary(String title, String artist, String songPath) {

        Connection c;
        Statement stmt;
        ResultSet rs;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:musicplayer.db");

            stmt = c.createStatement();
            stmt.executeUpdate("UPDATE sqlite_sequence SET seq=(SELECT COUNT(*) FROM SONGS) WHERE name='SONGS'");
            stmt.executeUpdate("INSERT INTO SONGS (SONG_NAME, FILE_PATH, ARTIST) VALUES ('" + title + "','" + songPath + "','" + artist + "')");

            rs = stmt.executeQuery("SELECT COUNT(*) AS amount FROM SONGS");
            int songID = rs.getInt("amount");
            rs = stmt.executeQuery("SELECT COUNT(*) AS amount FROM PLAYLIST_SONGS WHERE PLAYLIST_ID=1");
            int playlistID = rs.getInt("amount") + 1;


            stmt.executeUpdate("INSERT INTO PLAYLIST_SONGS (PLAYLIST_ID, SONG_ID, SONG_IN_PLAYLIST_ID) VALUES (1,'" + songID + "','" + playlistID + "')");

            stmt.close();
            c.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This function is run when the application starts. It
     * gets the playlists from the database and adds the songs
     * to each playlist, accoring to the information on the database.
     *
     * @param playlists The playlists list is passed so that the playlists can be added to the list*/

    public void initPlaylists(ArrayList<Playlist> playlists) {

        Connection c = null;
        Statement stmt = null;
        ResultSet rs;
        Playlist playlist;
        ArrayList<Song> songList;


        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:musicplayer.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT * FROM PLAYLIST;");


            while (rs.next()) {
                boolean isLibrary;
                int id = rs.getInt("PLAYLIST_ID");
                String name = rs.getString("NAME");
                String desc = rs.getString("DESCRIPTION");

                isLibrary = id == 1;

                playlist = new Playlist(id, name, desc, isLibrary);
                playlists.add(playlist);

                System.out.println(playlist.getPlaylistName() + " added");
            }
            rs.close();
            stmt.close();

            for (Playlist ps : playlists) {
                //add songs
                stmt = c.createStatement();
                rs = stmt.executeQuery("SELECT PLAYLIST_SONGS.SONG_IN_PLAYLIST_ID, SONGS.SONG_NAME, SONGS.ARTIST, SONGS.FILE_PATH, SONGS.ARTWORK_PATH FROM SONGS\n" +
                        "INNER JOIN PLAYLIST_SONGS ON SONGS.SONG_ID=PLAYLIST_SONGS.SONG_ID WHERE PLAYLIST_ID=" + ps.getPlaylistID());
                songList = new ArrayList<>();

                while (rs.next()) {


                    int id = rs.getInt("SONG_IN_PLAYLIST_ID");
                    String name = rs.getString("SONG_NAME");
                    String file_path = rs.getString("FILE_PATH");
                    String artwork_path = rs.getString("ARTWORK_PATH");
                    String artist = rs.getString("ARTIST");

                    System.out.println(id);
                    System.out.println(name);
                    System.out.println(file_path);
                    System.out.println(artwork_path);
                    System.out.println(artist);

                    Song song = new Song(id, name, artist, file_path, artwork_path);

                    songList.add(song);
                }
                ps.setSongs(songList);
                rs.close();
                stmt.close();
            }

            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            //System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            //System.exit(0);
        }
    }

    /**
     * This method executes when the table needs to update, when songs
     * are removed or added.
     * @param currentPlaylist The current playlist where the change has occurred */

    public void updateTable(Playlist currentPlaylist) {
        Connection c;
        Statement stmt;
        ResultSet rs;
        ArrayList songList = new ArrayList();
        int playlistID = currentPlaylist.getPlaylistID();
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:musicplayer.db");
            stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT PLAYLIST_SONGS.SONG_IN_PLAYLIST_ID, SONGS.SONG_NAME, SONGS.ARTIST, SONGS.FILE_PATH, SONGS.ARTWORK_PATH FROM SONGS\n" +
                    "INNER JOIN PLAYLIST_SONGS ON SONGS.SONG_ID=PLAYLIST_SONGS.SONG_ID WHERE PLAYLIST_ID=" + playlistID);
            songList = new ArrayList<>();

            while (rs.next()) {

                int id = rs.getInt("SONG_IN_PLAYLIST_ID");
                String name = rs.getString("SONG_NAME");
                String file_path = rs.getString("FILE_PATH");
                String artwork_path = rs.getString("ARTWORK_PATH");
                String artist = rs.getString("ARTIST");

                Song song = new Song(id, name, artist, file_path, artwork_path);

                songList.add(song);
            }
            currentPlaylist.setSongs(songList);

            stmt.close();
            c.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
