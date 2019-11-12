package main.db;

import main.model.Playlist;
import main.model.Song;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MusicPlayerDatabase {

    public void removeSongFromPlaylist(Playlist playlist, int selectedSong) {
        System.out.println(selectedSong);

        Connection c = null;
        Statement stmt = null;
        ResultSet rs;
        int playlistID = playlist.getPlaylistID();
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:musicplayer.db");
            //c.setAutoCommit(false);

            stmt = c.createStatement();
            stmt.execute("DELETE FROM PLAYLIST_SONGS WHERE PLAYLIST_ID=1 AND SONG_IN_PLAYLIST_ID="+selectedSong+"");
            stmt.execute("UPDATE PLAYLIST_SONGS SET SONG_IN_PLAYLIST_ID = SONG_IN_PLAYLIST_ID - 1 WHERE SONG_IN_PLAYLIST_ID >"+selectedSong+";\n");
            stmt.close();
            c.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void removePlaylist(int playlistID) {

        Connection c = null;
        Statement stmt = null;
        ResultSet rs;;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:musicplayer.db");
            //c.setAutoCommit(false);

            stmt = c.createStatement();
            stmt.execute("DELETE FROM PLAYLIST WHERE PLAYLIST_ID="+playlistID+";");
            stmt.execute("UPDATE PLAYLIST SET PLAYLIST_ID = PLAYLIST_ID - 1 WHERE PLAYLIST_ID >"+playlistID+";");
            stmt.close();
            c.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
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
            stmt.execute("INSERT INTO PLAYLIST (NAME, DESCRIPTION) VALUES ('"+name+"','"+desc+"');");
            rs = stmt.executeQuery("SELECT * FROM PLAYLIST WHERE NAME='"+name+"';");

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

    public void addSongToLibrary(String title, String artist, String songPath) {

        Connection c = null;
        Statement stmt = null;
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


            stmt.executeUpdate("INSERT INTO PLAYLIST_SONGS (PLAYLIST_ID, SONG_ID, SONG_IN_PLAYLIST_ID) VALUES (1,'"+songID+"','" +playlistID+ "')");

            stmt.close();
            c.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public void updateTable(Playlist currentPlaylist) {
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList<Song> songList = new ArrayList();
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

                System.out.println(id);
                System.out.println(name);
                System.out.println(file_path);
                System.out.println(artwork_path);
                System.out.println(artist);

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
