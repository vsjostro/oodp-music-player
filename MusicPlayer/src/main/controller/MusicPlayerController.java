package main.controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import main.API.APIDetails;
import main.db.MusicPlayerDatabase;
import main.model.Playlist;
import main.model.Song;
import main.view.MusicPlayerView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * The controller for the application. Handles the events when buttons/changes happen.
 *
 * @author Viktor
 * @version 1.0
 */

public class MusicPlayerController {

    private MusicPlayerView view;
    private MusicPlayerDatabase database;

    public MusicPlayerController(MusicPlayerView view, MusicPlayerDatabase database) {
        this.view = view;
        this.database = database;

        view.addSongToLibraryListener(new AddSongToLibraryListener());
        view.addSongToPlaylistListener(new AddSongToPlaylistListener());
        view.addRemoveSongListener(new RemoveSongListener());
        view.addPlaylistListener(new AddPlaylistListener());
        view.addRemovePlaylistListener(new RemovePlaylistListener());
        view.addEditPlaylistListener(new EditPlaylistListener());
        view.addMouseListener(new DoubleClickListener());
        view.addPlaySongListener(new PlaySongListener());
        view.addStopSongListener(new StopSongListener());
        view.addNextSongListener(new NextSongListener());
        view.addPrevSongListener(new PrevSongListener());
        view.addShuffleListener(new ShuffleListener());
        view.addLoopListener(new LoopListener());
        view.addFavoriteListener(new FavoriteListener());
        view.addListEventListener(new ListEventListener());
        view.addVolumeListener(new VolumeChangeListener());
        view.addSeekbarListener(new SeekbarListener());
        view.addSearchListener(new SearchListener());

    }

    private class AddSongToLibraryListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            addSongToLibrary();
        }
    }


    private class AddPlaylistListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            addPlaylist();
        }
    }

    private class RemovePlaylistListener implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent e) {
            removePlaylist();
        }
    }


    private class EditPlaylistListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            editPlaylist();
        }
    }


    private class RemoveSongListener implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent e) {
            removeSong();
        }
    }


    private class PlaySongListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int selectedRow = view.songTable.getSelectedRow();
                Song song = view.currentPlaylist.getSongs().get(selectedRow);
                playSong(song);

            } catch (ArrayIndexOutOfBoundsException ae) {
                JOptionPane.showMessageDialog(view.mainFrame, "Select a song to play!");
            }
        }
    }

    private class StopSongListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (view.songPlaying) {
                view.mediaPlayer.stop();
            }
        }
    }

    private class NextSongListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            nextSong();
        }
    }


    private class PrevSongListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            prevSong();
        }
    }

    private class ListEventListener implements ListSelectionListener {


        /**
         * This method changes the current playlist to a playlist
         * chosen by the user. The table is cleared and the songs
         * from the selected playlist will be added to the table
         */

        @Override
        public void valueChanged(ListSelectionEvent e) {
            String selectedPlaylist = view.libraryList.getSelectedValue().toString();

            for (Playlist playlist : view.playlistList) {
                if (playlist.getPlaylistName().equals(selectedPlaylist)) {
                    view.currentPlaylist = playlist;
                    view.currentPlaylistLabel.setText(view.currentPlaylist.getPlaylistName());
                    view.playlistDescription.setText(playlist.getPlaylistDescription());
                }
            }

            updateSongTable();

        }
    }

    private class SeekbarListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {

            int mouseX = e.getX();

            int seekbarVal = (int) Math.round(((double) mouseX / (double) view.seekBar.getWidth() * view.seekBar.getMaximum()));

            Duration songDuration = view.mediaPlayer.getTotalDuration();
            double newTime = songDuration.toSeconds() * (seekbarVal / 100.0);

            Duration newDuration = new Duration(newTime * 1000);

            view.mediaPlayer.seek(newDuration);
        }
    }


    private class ShuffleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.shuffleToggled = !view.shuffleToggled;
        }
    }

    private class LoopListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.loopToggled = !view.loopToggled;
        }
    }

    private class FavoriteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (view.songTable.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(view.mainFrame, "Select a song first!");

            } else {
                view.status.setText("Song added to favorites");
                database.addSongToPlaylist(view.currentPlaylist.getSongs().get(view.songTable.getSelectedRow()), view.playlistList.get(1));
            }

        }
    }

    private class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            search();
        }
    }

    private class DoubleClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int selectedRow = view.songTable.getSelectedRow();
                Song song = view.currentPlaylist.getSongs().get(selectedRow);
                playSong(song);
            }
        }
    }

    private class AddSongToPlaylistListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            addSongToPlaylist();
        }
    }

    private class VolumeChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            volumeChange();
        }
    }

    /**
     * This method adds a song to the song library. The user is first
     * prompted to select to song file, mp3 and wav files are accepted.
     * After selecting a song file, the user will enter the songs name and artist.
     * The API will then search for the song and artist and start downloading the
     * lyrics and album art. If the song isn't found or there is no internet connection,
     * the album art will be a default image and the lyrics will be empty.
     *
     */

    private void addSongToLibrary() {

        try {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Audio Files", "mp3", "wav");
            chooser.setFileFilter(filter);
            chooser.setAcceptAllFileFilterUsed(false);

            int value = chooser.showOpenDialog(null);
            String songPath = chooser.getSelectedFile().getPath();
            String songName = JOptionPane.showInputDialog(null, "Enter song name");
            String artist = JOptionPane.showInputDialog(null, "Enter artist name");


            if (songName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No title given!");
            }
            if (value == JFileChooser.APPROVE_OPTION) {

                String input = songName + " " + artist;
                String lyricsPath = null;
                String artPath;
                System.out.println(input.toLowerCase());

                APIDetails songAPIDetails = new APIDetails(input);

                if (songAPIDetails.getSongLyricsPath() != null) {
                    lyricsPath = songAPIDetails.getSongLyricsPath();
                    artPath = songAPIDetails.getAlbumArtPath();

                } else {
                    artPath = getClass().getResource("/resources/images/defaultImage.png").getPath();
                }

                database.addSongToLibrary(songName, artist, songPath, lyricsPath, artPath);
                updateSongTable();

            }
        } catch (NullPointerException ignored) {

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method adds a playlist to the list of playlists.
     * First it is added to the database and then to the list of playlists.
     */
    private void addPlaylist() {
        try {
            String name = JOptionPane.showInputDialog(null, "Enter playlist name");

            if (name.equals("")) {
                JOptionPane.showMessageDialog(null, "Playlist name cannot be empty");
            } else {
                String description = JOptionPane.showInputDialog(null, "Enter description (optional)");
                Playlist playlist = database.addPlaylist(name, description, view.playlistList);
                view.libraryListModel.addElement(playlist.getPlaylistName());
            }

        } catch (NullPointerException ignored) {

        }
    }

    /**
     * This method removes a playlist from the list of playlists.
     * If the user has chosen the song library to be removed, the
     * user will be informed that the song library cannot be removed.
     */

    private void removePlaylist() {
        int selectedPlaylist = view.libraryList.getSelectedIndex();
        int playlistID = view.playlistList.get(selectedPlaylist).getPlaylistID();

        if (selectedPlaylist == 0 || selectedPlaylist == 1) {
            JOptionPane.showMessageDialog(view.mainFrame, "Can't delete library or favorites!");
        } else {
            database.removePlaylist(playlistID);
            JOptionPane.showMessageDialog(null, view.playlistList.get(selectedPlaylist).getPlaylistName() + " deleted");
            view.libraryList.setSelectedIndex(0);
            view.libraryListModel.removeElementAt(playlistID - 1);

        }
    }

    /**
     * This method allows the user to change the name and description of
     * existing playlists. After a playlist is selected, the user presses
     * the edit option from the menu and the user is prompted to choose
     * to edit the name or description of the playlist.
     */

    private void editPlaylist() {
        if (view.libraryList.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(view.mainFrame, "Select a playlist first!");
            return;
        }

        String[] options = new String[]{"Name", "Description", "Cancel"};
        int choice = JOptionPane.showOptionDialog(null, "Edit playlist name or description",
                "Edit", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        System.out.println(choice);
        if (choice == -1 || choice == 2) {
            return;
        }
        if (choice == 0) {
            if (view.currentPlaylist.getPlaylistID() == 1 || view.currentPlaylist.getPlaylistID() == 2) {
                JOptionPane.showMessageDialog(null, "You cannot change the name of the Song library or Favorites.");
                return;
            }
            editPlaylistName();
        }
        if (choice == 1) {
            editPlaylistDescription();
        }

    }


    /**
     * This method is called if the user chooses to edit the description of a playlist.
     */
    private void editPlaylistDescription() {
        String desc = JOptionPane.showInputDialog(null, "Enter description:");
        if (desc == null) {
            return;
        }
        database.updatePlaylistDescription(desc);
        view.currentPlaylist.setPlaylistDescription(desc);
        view.playlistDescription.setText(desc);

    }

    /**
     * This method is called if the user chooses to edit the name of a playlist.
     */
    private void editPlaylistName() {
        String name = JOptionPane.showInputDialog(null, "Enter new playlist name:");
        if (name == null) {
            return;
        }
        database.updatePlaylistName(name);
        view.currentPlaylist.setPlaylistName(name);
        view.libraryListModel.setElementAt(view.currentPlaylist.getPlaylistName(), view.currentPlaylist.getPlaylistID() - 1);
    }

    /**
     * This method removes a song from the current playlist.
     * The song that is selected in the table is removed.
     * A song has to be selected before it can be removed.
     */
    private void removeSong() {
        int selectedRow = view.songTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "No song selected.");
        } else {

            database.removeSongFromPlaylist(view.currentPlaylist, view.playlistList, selectedRow + 1);
            updateSongTable();

        }
    }

    /**
     * This method updates the table, for example when
     * a song is removed or added to the current playlist.
     */
    private void updateSongTable() {

        database.updateTable();
        for (int i = view.songTableModel.getRowCount() - 1; i > -1; i--) {
            view.songTableModel.removeRow(i);
        }

        for (Song song : view.currentPlaylist.getSongs()) {
            view.songTableModel.addRow(new Object[]{song.getId(), song.getName(), song.getArtist()});
        }

    }


    /**
     * This method plays the selected song. If a song
     * is playing it will be stopped before the new song
     * starts playing. If the song has album art, it will be
     * displayed. Otherwise a default image will be displayed.
     *
     * @param song The selected song from the table
     */
    private void playSong(Song song) {

        if (view.songPlaying) {
            stopSong();
        }

        String songPath = song.getSongPath();
        view.currentSong = song;
        ImageIcon image;
        String lyrics;

        if (song.getImagePath() != null) {
            image = new ImageIcon(song.getImagePath());
        }
        //sets a default image if no image is set
        else {
            image = new ImageIcon(getClass().getResource("/resources/images/defaultImage.png"));
        }
        Image img1 = image.getImage();
        Image img2 = img1.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        image = new ImageIcon(img2);
        view.albumImage.setIcon(image);

        try {
            if (song.getLyricsPath() != null) {
                InputStream inputStream = new FileInputStream(new File(song.getLyricsPath()));
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line = bufferedReader.readLine();
                StringBuilder stringBuilder = new StringBuilder();

                while (line != null) {
                    stringBuilder.append(line).append("\n");
                    line = bufferedReader.readLine();
                }

                lyrics = stringBuilder.toString();
            } else {
                lyrics = "";
            }

            view.lyricsTextArea.setText(lyrics);
            view.lyricsTextArea.setCaretPosition(0);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Media hit = new Media(new File(songPath).toURI().toString());
        view.mediaPlayer = new MediaPlayer(hit);
        view.mediaPlayer.play();

        double volume = view.volumeSlider.getValue() / 100.0;
        view.mediaPlayer.setVolume(volume);


        view.currentSongLabel.setText("Now playing: " + song.getName() + " by " + song.getArtist());
        view.songPlaying = true;
        view.status.setText("");

        Thread background = new Thread(() -> {

            while (view.songPlaying) {

                Duration songDuration = hit.getDuration();
                Duration currentTime = view.mediaPlayer.getCurrentTime();

                if (songDuration.equals(currentTime)) {
                    nextSong();
                    break;
                }

                int time = (int) (currentTime.toSeconds() / songDuration.toSeconds() * 100);

                int songMin = (int) (songDuration.toMillis() / 1000) / 60;
                int songSec = (int) (songDuration.toMillis() / 1000);

                if (songSec / 60 >= 1) {
                    songSec %= 60;
                }

                String songTime = songMin + ":" + songSec;

                int min = (int) (currentTime.toMillis() / 1000) / 60;
                int sec = (int) (currentTime.toMillis() / 1000);

                if (sec / 60 >= 1) {
                    sec %= 60;
                }

                view.seekBar.setString(min + ":" + sec + "/" + songTime);
                view.seekBar.setValue(time);
                //view.seekBar.setString(df.format(currentTime.toMinutes())  + "/" + df.format(songDuration.toMinutes()));
                //view.status.setText(String.valueOf((int) currentTime.toSeconds()));

            }
        });
        background.start();
    }

    /**
     * This method stops a song if a song is playing
     */
    private void stopSong() {
        view.mediaPlayer.stop();
        view.songPlaying = false;
    }

    /**
     * This method plays the next song in the playlist.
     * If the current song is the last one in the playlist, the
     * user will be informed and the current song keeps playing.
     */
    private void nextSong() {
        try {
            if (view.songPlaying) {
                Song nextSong;


                if (view.shuffleToggled) {

                    Random rand = new Random();
                    int randomId = rand.nextInt(view.currentPlaylist.getSongs().size());
                    nextSong = view.currentPlaylist.getSongs().get(randomId);

                } else {
                    nextSong = view.currentPlaylist.getSongs().get(view.currentSong.getId());
                }
                playSong(nextSong);
                view.songTable.setRowSelectionInterval(nextSong.getId() - 1, nextSong.getId() - 1);
            }
        } catch (IndexOutOfBoundsException ie) {
            if (view.loopToggled) {
                Song song = view.currentPlaylist.getSongs().get(0);
                playSong(song);
                view.songTable.setRowSelectionInterval(song.getId() - 1, song.getId() - 1);
            } else {
                view.status.setText("Last song in playlist");
            }
            //stopSong();
        }
    }

    /**
     * This method plays the previous song in the playlist.
     * If the current song is the firest one in the playlist, the
     * user will be informed and the current song keeps playing.
     */

    private void prevSong() {
        try {
            if (view.songPlaying) {
                Song prevSong = view.currentPlaylist.getSongs().get(view.currentSong.getId() - 2);
                playSong(prevSong);
                view.songTable.setRowSelectionInterval(prevSong.getId() - 1, prevSong.getId() - 1);
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException ie) {
            view.status.setText("First song in playlist");
        }
    }

    /**
     * This method adds a song to a specific playlist. After selecting an existing
     * song from the table, the user can add that song to a playlist.
     */

    private void addSongToPlaylist() {
        int selectedRow = view.songTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view.mainFrame, "Select a song to be added!");
            return;
        }
        Song song = view.currentPlaylist.getSongs().get(selectedRow);
        ArrayList<String> playlists = new ArrayList<>();

        for (Playlist playlist : view.playlistList) {
            if (playlist == view.playlistList.get(0)) continue;

            playlists.add(playlist.getPlaylistName());
        }
        Object[] options = playlists.toArray();
        int choice = JOptionPane.showOptionDialog(null, "Add " + song.getName() + " to which playlist?",
                "Add song to playlist", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (choice == -1) {
            return;
        }

        database.addSongToPlaylist(song, view.playlistList.get(choice + 1));
    }


    /**
     * This method is called every time a change happens to the
     * volume slider. A song has to be playing for this method
     * to apply the volume change.
     */

    private void volumeChange() {
        try {
            if (view.songPlaying) {
                double volume = view.volumeSlider.getValue() / 100.0;
                view.mediaPlayer.setVolume(volume);
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        }
    }

    /**
     * This method searches for songs or artists that match the
     * string in the search field. If no results are found, a
     * prompt will appear to notify the user.
     */

    private void search() {
        Playlist searchPlaylist = new Playlist();
        ArrayList<Song> songList = new ArrayList<>();
        Playlist library = view.playlistList.get(0);
        String searchText = view.searchField.getText().toLowerCase();

        for (Song song : library.getSongs()) {
            if (song.getName().toLowerCase().contains(searchText) || song.getArtist().toLowerCase().contains(searchText)) {
                songList.add(song);
            }
        }

        if (songList.size() == 0) {
            JOptionPane.showMessageDialog(null, "No song or artist found with search: " + view.searchField.getText());

        } else {

            searchPlaylist.setSongs(songList);
            view.currentPlaylist = searchPlaylist;
            view.songTable.getSelectionModel().clearSelection();

            for (int i = view.songTableModel.getRowCount() - 1; i > -1; i--) {
                view.songTableModel.removeRow(i);
            }

            for (Song foundSong : songList) {
                view.songTableModel.addRow(new Object[]{foundSong.getId(), foundSong.getName(), foundSong.getArtist()});
            }
        }

    }
}