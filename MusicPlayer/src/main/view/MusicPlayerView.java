package main.view;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import main.model.MusicPlayerModel;
import main.model.Playlist;
import main.model.Song;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayerView extends JFrame {

    private JLabel currentSong = new JLabel("Pick a song and press play");
    private JLabel albumImage = new JLabel();
    private JLabel status = new JLabel("");

    private DefaultTableModel tableModel = new DefaultTableModel();
    private JTable table = new JTable(tableModel);

    private JButton addButton = new JButton("Add song");
    private JButton removeButton = new JButton("Remove song");
    private JButton playButton = new JButton("Play");
    private JButton nextButton = new JButton("Next");
    private JButton prevButton = new JButton("Prev");
    private JButton stopButton = new JButton("Stop");


    //random variables, placeholder while trying things out
    private BufferedImage image;
    private ArrayList<Song> songList = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private Boolean songPlaying = false;

    //MusicPlayerModel not implemented ye t, might be changed in the future


    public MusicPlayerView(MusicPlayerModel model) {

        tableModel.addColumn("Id");
        tableModel.addColumn("Title");
        tableModel.addColumn("Artist");

        Playlist playlist = new Playlist("Playlist 1", "Initial playlist.", songList);

        JLabel currentPlaylist = new JLabel("");
        currentPlaylist.setText(playlist.getPlaylistName());

        try {
            image = ImageIO.read(new File("MusicPlayer/src/resources/images/Album.png"));
            albumImage.setIcon(new ImageIcon(image));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Initial values, remove later. song1 and song2 not working for some reason
        Song song1 = new Song("epic", "Artist1", "MusicPlayer/src/resources/songs/epic.wav");
        Song song2 = new Song("groove", "Artist2", "MusicPlayer/src/resources/songs/groove.wav");
        Song song3 = new Song("bassline", "Artist3", "MusicPlayer/src/resources/songs/bassline.wav", "MusicPlayer/src/resources/images/guitar-bass-icon.png");
        Song song4 = new Song("happy", "Artist4", "MusicPlayer/src/resources/songs/happy.wav", "MusicPlayer/src/resources/images/Album.png");

        playlist.getSongs().add(song1);
        playlist.getSongs().add(song2);
        playlist.getSongs().add(song3);
        playlist.getSongs().add(song4);

        tableModel.addRow(new Object[]{song1.getId(), song1.getName(), song1.getArtist()});
        tableModel.addRow(new Object[]{song2.getId(), song2.getName(), song2.getArtist()});
        tableModel.addRow(new Object[]{song3.getId(), song3.getName(), song3.getArtist()});
        tableModel.addRow(new Object[]{song4.getId(), song4.getName(), song4.getArtist()});

        table.setBounds(30, 40, 200, 300);

        //Currently messes up when sorting and removing files
        //table.setAutoCreateRowSorter(true);
        table.setDefaultEditor(Object.class, null);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panel = new JPanel();
        panel.add(currentPlaylist);
        panel.add(addButton);
        panel.add(removeButton);
        panel.add(currentSong);
        panel.add(scrollPane);
        panel.add(playButton);
        panel.add(nextButton);
        panel.add(prevButton);
        panel.add(stopButton);
        panel.add(albumImage);
        panel.add(status);

        //GUI stuff
        JFrame frame = new JFrame("Music Player");
        frame.add(panel);
        panel.setLayout(new FlowLayout());

        //extra customization
        table.setGridColor(Color.black);
        table.setBackground(Color.yellow);
        panel.setBackground(Color.lightGray);
        ImageIcon image = new ImageIcon("MusicPlayer/src/resources/images/icon.png");
        frame.setIconImage(image.getImage());

        frame.setSize(500, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void addSongListener(ActionListener actionListener) {
        addButton.addActionListener(actionListener);
    }

    public void addRemoveListener(ActionListener actionListener) {
        removeButton.addActionListener(actionListener);
    }

    public void addPlaySongListener(ActionListener actionListener) {
        playButton.addActionListener(actionListener);
    }

    public void addStopSongListener(ActionListener actionListener) {
        stopButton.addActionListener(actionListener);
    }

    public void addSongToTable(String title, String artist, String songPath) {
        Song song = new Song(title, artist, songPath);
        songList.add(song);
        tableModel.addRow(new Object[]{song.getId(), song.getName(), song.getArtist()});
    }

    public void removeSong() {

        try {
            while (true) {

                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1)
                    break;
                else {
                    status.setText("Song " + songList.get(selectedRow).getName() + " removed");
                    songList.remove(selectedRow);
                    tableModel.removeRow(selectedRow);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
        }
    }


    public void playSong() {

        try {
            if (songPlaying) {
                mediaPlayer.stop();
            }

            int selectedRow = table.getSelectedRow();

            Song song = songList.get(selectedRow);
            String songPath = song.getSongPath();

            if (song.getImagePath() != null) {
                ImageIcon image = new ImageIcon(song.getImagePath());
                Image img1 = image.getImage();
                Image img2 = img1.getScaledInstance(128, 128, Image.SCALE_SMOOTH);
                image = new ImageIcon(img2);
                albumImage.setIcon(image);
            }

            currentSong.setText("Now playing: " + songList.get(selectedRow).getName() + " by " + songList.get(selectedRow).getArtist());

            Media hit = new Media(new File(songPath).toURI().toString());
            mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();
            songPlaying = true;


            status.setText("");
        } catch (ArrayIndexOutOfBoundsException e) {
            status.setText("Select a song to play!");
        }

    }

    public void stopSong() {

        if (songPlaying) {
            mediaPlayer.stop();
        }

    }
}
