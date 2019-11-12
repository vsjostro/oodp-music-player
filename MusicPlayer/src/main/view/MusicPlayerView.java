package main.view;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import main.db.MusicPlayerDatabase;
import main.model.Playlist;
import main.model.Song;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayerView extends JFrame {

    private MusicPlayerDatabase database = new MusicPlayerDatabase();

    private JLabel currentSongLabel = new JLabel("Pick a song and press play");
    private JLabel currentPlaylistLabel = new JLabel("");
    private JLabel albumImage = new JLabel();
    private JTextArea lyricsTextArea = new JTextArea("foo");
    JScrollPane lyricsScrollPane;
    private JLabel status = new JLabel("");

    private DefaultTableModel songTableModel = new DefaultTableModel();
    private JTable songTable = new JTable(songTableModel);
    JScrollPane scrollPane;

    private DefaultListModel libraryListModel = new DefaultListModel();
    private JList libraryList = new JList(libraryListModel);

    private JButton addButton = new JButton("Add song");
    private JButton removeButton = new JButton("Remove song");
    private JButton editButton = new JButton("Edit playlist");
    private JButton playButton = new JButton("Play");
    private JButton nextButton = new JButton("Next");
    private JButton prevButton = new JButton("Prev");
    private JButton stopButton = new JButton("Stop");

    //Menubar init
    JMenuBar menuBar = new JMenuBar();
    JMenu addMenu = new JMenu("Add");
    JMenu removeMenu = new JMenu("Remove");
    JMenu editMenu = new JMenu("Edit");
    JMenuItem addSongItem = new JMenuItem("Add Song");
    JMenuItem addPlaylistItem = new JMenuItem("Add Playlist");
    JMenuItem removeSongItem = new JMenuItem("Remove Song");
    JMenuItem removePlaylistItem = new JMenuItem("Remove Playlist");
    JMenuItem editSongItem = new JMenuItem("Edit Song");
    JMenuItem editPlaylistItem = new JMenuItem("Edit Playlist");


    //random variables, placeholder while trying things out
    private BufferedImage image;
    private ArrayList<Song> songList = new ArrayList<>();
    private ArrayList<Playlist> playlistList = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private Boolean songPlaying = false;
    private Song currentSong;
    private Playlist currentPlaylist;

    //MusicPlayerModel not implemented ye t, might be changed in the future
    public MusicPlayerView() {


        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        //UIManager.put("Button.mouseHoverEnable", false);
        //JDialog.setDefaultLookAndFeelDecorated(true);
        //JFrame.setDefaultLookAndFeelDecorated(false);

        menuBar.add(addMenu);
        menuBar.add(removeMenu);
        menuBar.add(editMenu);
        addMenu.add(addSongItem);
        addMenu.add(addPlaylistItem);
        removeMenu.add(removeSongItem);
        removeMenu.add(removePlaylistItem);
        editMenu.add(editSongItem);
        editMenu.add(editPlaylistItem);

        songTableModel.addColumn("Id");
        songTableModel.addColumn("Title");
        songTableModel.addColumn("Artist");

        database.initPlaylists(playlistList);
        currentPlaylist = playlistList.get(0);

        for (Playlist playlist : playlistList) {
            libraryListModel.addElement(playlist.getPlaylistName());
        }


        for (Song song : currentPlaylist.getSongs()) {
            songTableModel.addRow(new Object[]{song.getId(), song.getName(), song.getArtist()});
        }

        currentPlaylistLabel.setText(currentPlaylist.getPlaylistName());
        currentPlaylistLabel.setHorizontalAlignment(JLabel.CENTER);
        currentPlaylistLabel.setVerticalAlignment(JLabel.CENTER);

        //song library is a playlist object for now
        /*Playlist playlist1 = new Playlist("Playlist 1", "Playlist1 description", songList);
        Playlist playlist2 = new Playlist("Playlist 2", "Playlist2 description", songList);

        Playlist songLibrary = new Playlist("Song library", "test", songList);*/
        //SongLibrary songLibrary = new SongLibrary(songList);

        /*playlistList.add(playlist1);
        playlistList.add(playlist2);
        playlistList.add(songLibrary);*/

        libraryList.setPreferredSize(new Dimension(300, 800));
        libraryList.setBackground(Color.lightGray);
        libraryList.setFont(new Font(null, Font.PLAIN, 18));

        //lyricsTextArea.setPreferredSize(new Dimension(300, 600));
        lyricsTextArea.setLineWrap(true);
        lyricsTextArea.setWrapStyleWord(true);

        albumImage.setPreferredSize(new Dimension(300, 300));
        albumImage.setHorizontalAlignment(JLabel.CENTER);
        albumImage.setVerticalAlignment(JLabel.CENTER);

        try {
            image = ImageIO.read(new File("MusicPlayer/src/resources/images/defaultImage.png"));
            Image img = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            albumImage.setIcon(new ImageIcon(img));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Initial values, remove later. song1 and song2 not working for some reason
        /*Song song1 = new Song("epic", "Artist1", "MusicPlayer/src/resources/songs/epic.wav");
        Song song2 = new Song("groove", "Artist2", "MusicPlayer/src/resources/songs/groove.wav");
        Song song3 = new Song("bassline", "Artist3", "MusicPlayer/src/resources/songs/bassline.wav", "MusicPlayer/src/resources/images/guitar-bass-icon.png");
        Song song4 = new Song("happy", "Artist4", "MusicPlayer/src/resources/songs/happy.wav", "MusicPlayer/src/resources/images/tameimpalaitmightbetime.png");*/

        /*playlist1.getSongs().add(song1);
        playlist1.getSongs().add(song2);
        playlist1.getSongs().add(song3);
        playlist1.getSongs().add(song4);

        songTableModel.addRow(new Object[]{song1.getId() + 1, song1.getName(), song1.getArtist()});
        songTableModel.addRow(new Object[]{song2.getId() + 1, song2.getName(), song2.getArtist()});
        songTableModel.addRow(new Object[]{song3.getId() + 1, song3.getName(), song3.getArtist()});
        songTableModel.addRow(new Object[]{song4.getId() + 1, song4.getName(), song4.getArtist()});*/


        songTable.setBounds(30, 40, 200, 300);

        //Currently messes up when sorting and removing files
        //songTable.setAutoCreateRowSorter(true);
        songTable.setDefaultEditor(Object.class, null);
        songTable.setFont(new Font("", Font.PLAIN, 24));
        songTable.setRowHeight(40);
        scrollPane = new JScrollPane(songTable);
        scrollPane.setPreferredSize(new Dimension(1100, 800));

        lyricsScrollPane = new JScrollPane(lyricsTextArea);
        //lyricsScrollPane.setPreferredSize(new Dimension(lyricsTextArea.getPreferredSize()));
        lyricsScrollPane.setPreferredSize(new Dimension(300, 600));


        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        JPanel panel5 = new JPanel();
        panel1.setLayout(new BorderLayout(15, 15));
        panel2.setLayout(new BorderLayout(15, 15));
        panel3.setLayout(new BorderLayout(15, 15));
        panel4.setLayout(new FlowLayout());
        panel5.setLayout(new BorderLayout(15, 15));
        panel1.add(addButton, BorderLayout.WEST);
        panel1.add(currentPlaylistLabel, BorderLayout.CENTER);
        panel1.add(removeButton, BorderLayout.EAST);
        panel2.add(libraryList, BorderLayout.WEST);
        panel3.add(scrollPane, BorderLayout.CENTER);
        panel4.add(currentSongLabel);
        panel4.add(playButton);
        panel4.add(prevButton);
        panel4.add(nextButton);
        panel4.add(stopButton);
        panel5.add(lyricsScrollPane, BorderLayout.NORTH);
        panel5.add(albumImage, BorderLayout.SOUTH);
        panel5.add(status, BorderLayout.CENTER);


        JFrame frame = new JFrame("Music Player");
        frame.add(panel1, BorderLayout.NORTH);
        frame.add(panel2, BorderLayout.WEST);
        frame.add(panel3, BorderLayout.CENTER);
        frame.add(panel5, BorderLayout.EAST);
        frame.add(panel4, BorderLayout.SOUTH);

        //extra customization
        songTable.setGridColor(Color.black);
        //songTable.setBackground(Color.yellow);
        //panel1.setBackground(Color.lightGray);
        ImageIcon image = new ImageIcon("MusicPlayer/src/resources/images/icon.png");
        frame.setIconImage(image.getImage());
        frame.setJMenuBar(menuBar);

        //frame.setSize(1700, 900);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setExtendedState(MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void addSongListener(ActionListener actionListener) {
        addButton.addActionListener(actionListener);
        addSongItem.addActionListener(actionListener);
    }

    public void addPlaylistListener(ActionListener actionListener) {
        addPlaylistItem.addActionListener(actionListener);
    }

    public void removePlaylistListener(ActionListener actionListener) {
        removePlaylistItem.addActionListener(actionListener);
    }



    public void addRemoveListener(ActionListener actionListener) {
        removeButton.addActionListener(actionListener);
        removeSongItem.addActionListener(actionListener);
    }

    public void addPlaySongListener(ActionListener actionListener) {
        playButton.addActionListener(actionListener);
    }

    public void addStopSongListener(ActionListener actionListener) {
        stopButton.addActionListener(actionListener);
    }

    public void addNextSongListener(ActionListener actionListener) {
        nextButton.addActionListener(actionListener);
    }

    public void addPrevSongListener(ActionListener actionListener) {
        prevButton.addActionListener(actionListener);
    }

    public void addListEventListener(ListSelectionListener listSelectionListener) {
        libraryList.addListSelectionListener(listSelectionListener);
    }

    public void addSongToTable(String title, String artist, String songPath) {

        database.addSongToLibrary(title, artist, songPath);
        updateSongTable();
/*
        Song song = new Song(title, artist, songPath);
        currentPlaylist.getSongs().add(song);
        songTableModel.addRow(new Object[]{song.getId(), song.getName(), song.getArtist()});*/
    }
    public void addSongToLibrary(String title, String artist, String songPath) {

        database.addSongToLibrary(title, artist, songPath);

        /*Song song = new Song(title, artist, songPath);
        currentPlaylist.getSongs().add(song);
        songTableModel.addRow(new Object[]{song.getId(), song.getName(), song.getArtist()});*/
    }

    public void removeSongFromPlaylist() {

        int selectedRow = songTable.getSelectedRow();

        database.removeSongFromPlaylist(currentPlaylist, selectedRow+1);

        /*status.setText("Song " + currentPlaylist.getSongs().get(selectedRow).getName() + " removed");
        currentPlaylist.getSongs().remove(selectedRow);
        songTableModel.removeRow(selectedRow);*/
        updateSongTable();

    }

    public void playButtonPressed() {

        try {
            int selectedRow = songTable.getSelectedRow();
            Song song = currentPlaylist.getSongs().get(selectedRow);
            playSong(song);

        } catch (ArrayIndexOutOfBoundsException e) {
            status.setText("Select a song to play!");
        }
    }

    public void playSong(Song song) {

        //stops song if a song is currently playing
        stopSong();

        String songPath = song.getSongPath();
        currentSong = song;
        ImageIcon image;

        if (song.getImagePath() != null) {
            image = new ImageIcon(song.getImagePath());
        }
        //sets a default image if no image is set
        else {
            image = new ImageIcon("MusicPlayer/src/resources/images/defaultImage.png");
        }
        Image img1 = image.getImage();
        Image img2 = img1.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        image = new ImageIcon(img2);
        albumImage.setIcon(image);


        Media hit = new Media(new File(songPath).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();

        currentSongLabel.setText("Now playing: " + song.getName() + " by " + song.getArtist());
        songPlaying = true;
        status.setText("");
    }

    public void stopSong() {

        if (songPlaying) {
            mediaPlayer.stop();
        }
    }

    public void nextSong() {

        try {
            if (songPlaying) {
                Song nextSong = currentPlaylist.getSongs().get(currentSong.getId());
                playSong(nextSong);
                songTable.setRowSelectionInterval(nextSong.getId() - 1, nextSong.getId() - 1);
            }
        } catch (IndexOutOfBoundsException e) {
            status.setText("Last song in playlist");
        }
    }

    public void prevSong() {

        try {
            if (songPlaying) {
                Song prevSong = currentPlaylist.getSongs().get(currentSong.getId() - 2);
                playSong(prevSong);
                songTable.setRowSelectionInterval(prevSong.getId() - 1, prevSong.getId() - 1);
            }
        } catch (IndexOutOfBoundsException e) {
            status.setText("First song in playlist");
        }
    }

    public void changePlaylist() {

        String selectedPlaylist = libraryList.getSelectedValue().toString();

        for (Playlist playlist : playlistList) {
            if (playlist.getPlaylistName().equals(selectedPlaylist)) {
                currentPlaylist = playlist;
                currentPlaylistLabel.setText(currentPlaylist.getPlaylistName());
            }
        }

        for (int i = songTableModel.getRowCount() - 1; i > -1; i--) {
            songTableModel.removeRow(i);
        }
        if (currentPlaylist.getSongs() != null) {
            for (Song song : currentPlaylist.getSongs()) {
                songTableModel.addRow(new Object[]{song.getId(), song.getName(), song.getArtist()});
            }
        }

    }

    public void updateSongTable() {

        database.updateTable(currentPlaylist);
        for (int i = songTableModel.getRowCount() - 1; i > -1; i--) {
            songTableModel.removeRow(i);
        }

        for (Song song : currentPlaylist.getSongs()) {
            songTableModel.addRow(new Object[]{song.getId(), song.getName(), song.getArtist()});
        }

    }

    public void addPlaylist(String name, String description) {
        Playlist playlist = database.addPlaylist(name, description, playlistList);
        libraryListModel.addElement(playlist.getPlaylistName());

    }

    public void removePlaylist() {

        int selectedPlaylist = libraryList.getSelectedIndex();
        int playlistID = playlistList.get(selectedPlaylist).getPlaylistID();

        if (selectedPlaylist == 0) {
            System.out.println("Can't delete library!");
        }else {
            database.removePlaylist(playlistID);
            System.out.println(playlistList.get(selectedPlaylist).getPlaylistName()+ " deleted");
        }

    }
}
