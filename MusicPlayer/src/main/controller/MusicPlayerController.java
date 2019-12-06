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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * The controller for the application. Handles the events when buttons
 * are pressed.
 *
 * @author Viktor
 * @version 1.0
 */

public class MusicPlayerController {

    private MusicPlayerView view;
    private MusicPlayerDatabase database;
    //private MusicPlayerModel model;

    public MusicPlayerController(MusicPlayerView view, MusicPlayerDatabase database) {
        //this.model = model;
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
        view.addFullscreenListener(new FullscreenListener());
        view.addListEventListener(new ListEventListener());
        view.addVolumeListener(new VolumeChangeListener());
        view.addSeekbarListener(new SeekbarListener());
        view.addSearchListener(new SearchListener());

    }

    class AddSongToLibraryListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Audio Files", "mp3", "wav");
                chooser.setFileFilter(filter);
                chooser.setAcceptAllFileFilterUsed(false);

                int value = chooser.showOpenDialog(null);
                System.out.println(chooser.getSelectedFile().getPath());
                String songPath = chooser.getSelectedFile().getPath();
                String songName = JOptionPane.showInputDialog(null, "Enter song name");
                String artist = JOptionPane.showInputDialog(null, "Enter artist name");


                String[] songNameParsed = chooser.getSelectedFile().getName().split("\\.");
                //String songName = songNameParsed[0];

                if (songName.isEmpty()) {
                    System.out.println("no title given");
                }
                if (value == JFileChooser.APPROVE_OPTION) {

                    //Thread apiFetch = new Thread(() -> {


                        try {
                            String input = songName + " " + artist;
                            System.out.println(input.toLowerCase());

                            APIDetails songAPIDetails = new APIDetails(input);
                            String lyricsPath = songAPIDetails.getSongLyricsPath();
                            String artPath = songAPIDetails.getAlbumArtPath(songName, artist, songAPIDetails.getBigAlbumArtURL());
                            ImageIcon image;

                            System.out.println(lyricsPath);
                            System.out.println(artPath);

                            if (artPath == null) {
                                artPath = getClass().getResource("/resources/images/defaultImage.png").getPath();
                            }

                            database.addSongToLibrary(songName, artist, songPath, lyricsPath, artPath);
                            updateSongTable();
                            view.addSongToTable(songName, artist, songPath);


                        } catch (Exception ex) {

                        }


                    //});
                    //apiFetch.start();


                    /*String lyrics = songAPIDetails.getSongLyricsPath();
                    System.out.println(lyrics);
                    LyricWindow lw = new LyricWindow(lyrics);
                    lw.makeLyricWindow(lyrics);*/

                }
            } catch (NullPointerException n) {
                System.out.println(n);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    class AddPlaylistListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {


            try {
                String name = JOptionPane.showInputDialog(null, "Enter playlist name");

                if (name.equals("")) {
                    System.out.println("Playlist name cannot be empty");
                } else {
                    String description = JOptionPane.showInputDialog(null, "Enter description (optional)");
                    Playlist playlist = database.addPlaylist(name, description, view.playlistList);
                    view.libraryListModel.addElement(playlist.getPlaylistName());
                }

            } catch (NullPointerException np) {

            }
        }
    }

    class RemovePlaylistListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedPlaylist = view.libraryList.getSelectedIndex();
            int playlistID = view.playlistList.get(selectedPlaylist).getPlaylistID();

            if (selectedPlaylist == 0 || selectedPlaylist == 1) {
                JOptionPane.showMessageDialog(view.mainFrame, "Can't delete library or favorites!");
            } else {
                database.removePlaylist(playlistID);
                System.out.println(view.playlistList.get(selectedPlaylist).getPlaylistName() + " deleted");

            }
        }
    }

    class EditPlaylistListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (view.libraryList.isSelectionEmpty()) {
                JOptionPane.showMessageDialog(view.mainFrame, "Select a playlist first!");
                return;
            }
            String[] options = new String[]{"Name", "Description", "Cancel"};
            int choice = JOptionPane.showOptionDialog(null, "Edit playlist name or description",
                    "Edit", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (choice == -1 || choice == 2) {
                return;
            }
            if (choice == 0) {
                editPlaylistName();
            }
            if (choice == 1) {
                editPlaylistDescription();
            }

        }
    }

    private void editPlaylistDescription() {
        String desc = JOptionPane.showInputDialog(null, "Enter description");
        database.updatePlaylistDescription(desc);
        view.currentPlaylist.setPlaylistDescription(desc);
        view.playlistDescription.setText(desc);

    }

    private void editPlaylistName() {
        System.out.println("edit playlist name");
    }

    class RemoveSongListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            int selectedRow = view.songTable.getSelectedRow();
            //System.out.println(selectedRow);

            if (selectedRow == -1) {
                System.out.println("no song selected");
            } else {

                database.removeSongFromPlaylist(view.currentPlaylist, view.playlistList, selectedRow + 1);
                updateSongTable();

            }
        }
    }

    class PlaySongListener implements ActionListener {

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

    class StopSongListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (view.songPlaying) {
                view.mediaPlayer.stop();
            }

        }
    }

    class NextSongListener implements ActionListener {
        /**
         * This method plays the next song in the playlist.
         * If the current song is the last one in the playlist, the
         * user will be informed and the current song keeps playing.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            nextSong();
        }
    }


    class PrevSongListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (view.songPlaying) {
                    Song prevSong = view.currentPlaylist.getSongs().get(view.currentSong.getId() - 2);
                    playSong(prevSong);
                    view.songTable.setRowSelectionInterval(prevSong.getId() - 1, prevSong.getId() - 1);
                }
            } catch (IndexOutOfBoundsException ie) {
                view.status.setText("First song in playlist");
            }
        }
    }

    class ListEventListener implements ListSelectionListener {

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

            /*for (int i = view.songTableModel.getRowCount() - 1; i > -1; i--) {
                view.songTableModel.removeRow(i);
            }
            if (view.currentPlaylist.getSongs() != null) {
                for (Song song : view.currentPlaylist.getSongs()) {
                    view.songTableModel.addRow(new Object[]{song.getId(), song.getName(), song.getArtist()});
                }
            }*/
        }
    }

    private void updateSongTable() {

        database.updateTable();
        for (int i = view.songTableModel.getRowCount() - 1; i > -1; i--) {
            view.songTableModel.removeRow(i);
        }

        for (Song song : view.currentPlaylist.getSongs()) {
            view.songTableModel.addRow(new Object[]{song.getId(), song.getName(), song.getArtist()});
        }

    }

    private void updateLibraryList() {

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
                System.out.println();
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
                /*try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/


                Duration songDuration = hit.getDuration();
                Duration currentTime = view.mediaPlayer.getCurrentTime();
                DecimalFormat df = new DecimalFormat("#0");


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

                //System.out.println(songDuration.toSeconds());
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
                System.out.println("canceled");
                return;
            }

            database.addSongToPlaylist(song, view.playlistList.get(choice + 1));

        }
    }

    private class VolumeChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {

            try {
                double volume = view.volumeSlider.getValue() / 100.0;
                view.mediaPlayer.setVolume(volume);
            } catch (NullPointerException ne) {

            }

        }
    }

    private class SeekbarListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {

            int mouseX = e.getX();

            int seekbarVal = (int) Math.round(((double) mouseX / (double) view.seekBar.getWidth() * view.seekBar.getMaximum()));

            Duration songDuration = view.mediaPlayer.getTotalDuration();
            double newTime = songDuration.toSeconds() * (seekbarVal / 100.0);
            System.out.println(newTime);

            Duration newDuration = new Duration(newTime * 1000);

            view.mediaPlayer.seek(newDuration);

            //view.seekBar.setValue(seekbarVal);

        }
    }

    private class FullscreenListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (!view.fullscreen) {


                view.panel1.setVisible(false);
                view.panel2.setVisible(false);
                view.panel3.setVisible(false);

                view.lyricsScrollPane.setPreferredSize(new Dimension(600, 600));
                view.albumImage.setPreferredSize(new Dimension(500, 500));

                view.fullscreen = true;
            } else {
                view.panel1.setVisible(true);
                view.panel2.setVisible(true);
                view.panel3.setVisible(true);
                view.lyricsScrollPane.setPreferredSize(new Dimension(300, 500));
                view.albumImage.setPreferredSize(new Dimension(300, 300));
                view.fullscreen = false;
            }

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

            Playlist searchPlaylist = new Playlist();

            ArrayList<Song> songList = new ArrayList<>();
            Playlist library = view.playlistList.get(0);
            String searchText = view.searchField.getText().toLowerCase();

            for (Song song : library.getSongs()) {
                if (song.getName().toLowerCase().contains(searchText) || song.getArtist().toLowerCase().contains(searchText)) {
                    songList.add(song);
                }
            }

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
