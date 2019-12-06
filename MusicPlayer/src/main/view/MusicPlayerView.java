package main.view;

import javafx.scene.media.MediaPlayer;
import main.db.MusicPlayerDatabase;
import main.model.Playlist;
import main.model.Song;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

//import net.miginfocom.swing.MigLayout;

/**
 * MusicPlayerView handles all the GUI related things and also
 * the functions related to the buttons on the GUI. MVC is not
 * entirely implemented, but the actionlisteners are connected to
 * the MusicPlayerController.
 *
 * @author Viktor
 * @version 1.0
 */

public class MusicPlayerView extends JFrame {

    private MusicPlayerDatabase database = new MusicPlayerDatabase();

    public JFrame mainFrame;
    public JPanel panel1 = new JPanel();
    public JPanel panel2 = new JPanel();
    public JPanel panel3 = new JPanel();
    public JPanel panel4 = new JPanel();
    public JPanel panel4_1 = new JPanel();
    public JPanel panel4_2 = new JPanel();
    public JPanel panel5 = new JPanel();

    public JLabel currentSongLabel = new JLabel("Pick a song and press play");
    public JLabel currentPlaylistLabel = new JLabel();
    public JLabel albumImage = new JLabel();
    public JLabel status = new JLabel("");

    public JTextArea playlistDescription;
    public JScrollPane lyricsScrollPane;
    public JScrollPane songTableScrollPane;
    public JTextField searchField = new JTextField("Search" , 20);
    public JTextArea lyricsTextArea = new JTextArea();

    public DefaultTableModel songTableModel = new DefaultTableModel();
    public JTable songTable = new JTable(songTableModel);

    public DefaultListModel libraryListModel = new DefaultListModel();
    public JList libraryList = new JList(libraryListModel);

    private JButton addButton = new JButton("Add song");
    private JButton removeButton = new JButton("Remove song");
    private JButton playButton = new JButton("Play");
    private JButton nextButton = new JButton("Next");
    private JButton prevButton = new JButton("Prev");
    private JButton stopButton = new JButton("Stop");
    private JButton shuffleButton = new JButton("Shuffle");
    private JButton loopButton = new JButton("Loop");
    private JButton favoriteButton = new JButton("Favorite");

    private JButton fullscreenButton = new JButton("Fullscreen");

    public JProgressBar seekBar = new JProgressBar();
    public JSlider volumeSlider = new JSlider();

    public Border border;

    private JMenuItem addSongToLibraryItem;
    private JMenuItem addSongToPlaylistItem;
    private JMenuItem addPlaylistItem;
    private JMenuItem removeSongItem;
    private JMenuItem removePlaylistItem;
    private JMenuItem editPlaylistItem;



    //random variables, placeholder while trying things out
    private BufferedImage image;
    private ArrayList<Song> songList = new ArrayList<>();
    public ArrayList<Playlist> playlistList = new ArrayList<>();
    public MediaPlayer mediaPlayer;
    public Boolean songPlaying = false;
    public Boolean shuffleToggled = false;
    public Boolean loopToggled = false;
    public Boolean fullscreen = false;
    public Song currentSong;
    public Playlist currentPlaylist;


    /**
     * The constructor for the View, all GUI components are added
     * and configured. Currently BorderLayout is used and the
     * components are assigned to different panels depending on
     * where the components should be placed.
     */

    public MusicPlayerView() {


        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        //UIManager.put("Button.mouseHoverEnable", false);
        //JDialog.setDefaultLookAndFeelDecorated(true);
        //JFrame.setDefaultLookAndFeelDecorated(false);

        //Menubar init
        JMenuBar menuBar = new JMenuBar();
        JMenu addMenu = new JMenu("Add");
        menuBar.add(addMenu);
        JMenu removeMenu = new JMenu("Remove");
        menuBar.add(removeMenu);
        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);
        addSongToLibraryItem = new JMenuItem("Add Song to library");
        addMenu.add(addSongToLibraryItem);
        addSongToPlaylistItem = new JMenuItem("Add Song to playlist");
        addMenu.add(addSongToPlaylistItem);
        addPlaylistItem = new JMenuItem("Add Playlist");
        addMenu.add(addPlaylistItem);
        removeSongItem = new JMenuItem("Remove Song");
        removeMenu.add(removeSongItem);
        removePlaylistItem = new JMenuItem("Remove Playlist");
        removeMenu.add(removePlaylistItem);
        //JMenuItem editSongItem = new JMenuItem("Edit Song");
        //editMenu.add(editSongItem);
        editPlaylistItem = new JMenuItem("Edit Playlist");
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


        border = BorderFactory.createLineBorder(Color.black);

        currentPlaylistLabel.setText(currentPlaylist.getPlaylistName());
        currentPlaylistLabel.setHorizontalAlignment(JLabel.CENTER);
        currentPlaylistLabel.setVerticalAlignment(JLabel.CENTER);

        libraryList.setPreferredSize(new Dimension(300, 800));
        libraryList.setBackground(Color.lightGray);
        libraryList.setFont(new Font(null, Font.PLAIN, 18));
        libraryList.setBorder(border);

        playlistDescription = new JTextArea();
        playlistDescription.setPreferredSize(new Dimension(300, 300));
        playlistDescription.setBorder(border);
        playlistDescription.setText(currentPlaylist.getPlaylistDescription());
        playlistDescription.setEditable(false);



        lyricsTextArea.setLineWrap(true);
        lyricsTextArea.setWrapStyleWord(true);
        lyricsTextArea.setEditable(false);

        albumImage.setPreferredSize(new Dimension(300, 300));
        albumImage.setHorizontalAlignment(JLabel.CENTER);
        albumImage.setVerticalAlignment(JLabel.CENTER);

        try {

            URL file = getClass().getResource("/resources/images/africa.jpg");

            System.out.println(file.getPath());

            image = ImageIO.read(file);
            Image img = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            albumImage.setIcon(new ImageIcon(img));


        } catch (IOException e) {
            e.printStackTrace();
        }

        //setting the lyrics
        /*try {
            InputStream inputStream = new FileInputStream("MusicPlayer/src/resources/lyrics/tameimpala.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = bufferedReader.readLine();
            StringBuilder stringBuilder = new StringBuilder();

            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }

            String lyrics = stringBuilder.toString();
            lyricsTextArea.setText(lyrics);


        } catch (IOException e) {
            e.printStackTrace();
        }*/

        songTable.setBounds(30, 40, 200, 300);
        songTable.setDefaultEditor(Object.class, null);
        songTable.setFont(new Font("", Font.PLAIN, 24));
        songTable.setRowHeight(40);
        songTable.setBorder(border);
        songTableScrollPane = new JScrollPane(songTable);
        songTableScrollPane.setPreferredSize(new Dimension(1100, 800));

        lyricsScrollPane = new JScrollPane(lyricsTextArea);
        lyricsScrollPane.setPreferredSize(new Dimension(300, 500));


        volumeSlider.setValue(100);
        seekBar.setStringPainted(true);
        seekBar.setString("0:00/0:00");

        //songTable.();
        searchField.setToolTipText("Search");
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search")){
                    searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().equals("")){
                    searchField.setText("Search");
                }

            }
        });
        /*searchField.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (searchField.getText().equals("Search")){
                    searchField.setText("");
                }
            }
        });*/



        setBorderlayout();
        //setMigLayout();



        //JFrame fullscreenFrame = new JFrame("Fullscreen Player");
        //fullscreenFrame.add(albumImage, BorderLayout.WEST);
        //fullscreenFrame.add(lyricsScrollPane, BorderLayout.EAST);
        //fullscreenFrame.add(panel4, BorderLayout.SOUTH);
        //fullscreenFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        //fullscreenFrame.setVisible(true);

        //extra customization
        songTable.setGridColor(Color.black);
        ImageIcon image = new ImageIcon("MusicPlayer/src/resources/images/icon.png");
        mainFrame.setIconImage(image.getImage());
        mainFrame.setJMenuBar(menuBar);

        //frame.setSize(1700, 900);
        mainFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        mainFrame.setExtendedState(MAXIMIZED_BOTH);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    public void setBorderlayout() {
        panel1.setLayout(new BorderLayout(15, 15));
        panel2.setLayout(new BorderLayout(15, 15));
        panel3.setLayout(new BorderLayout(15, 15));
        panel4.setLayout(new BorderLayout(15,15));
        panel4_1.setLayout(new FlowLayout());
        panel4_2.setLayout(new BorderLayout(15,15));
        panel5.setLayout(new BorderLayout(15, 15));

        panel1.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        panel2.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        panel3.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panel4.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        panel5.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        //panel1.setPreferredSize(new Dimension(0,50  ));

        //panel1.add(addButton, BorderLayout.WEST);
        panel1.add(status, BorderLayout.WEST);
        panel1.add(currentPlaylistLabel, BorderLayout.CENTER);
        panel1.add(searchField, BorderLayout.EAST);
        //panel1.add(removeButton, BorderLayout.EAST);
        panel2.add(libraryList);
        panel2.add(playlistDescription, BorderLayout.SOUTH);
        panel3.add(songTableScrollPane, BorderLayout.CENTER);
        panel4_2.add(currentSongLabel, BorderLayout.WEST);
        panel4_2.add(seekBar, BorderLayout.SOUTH);
        panel4_2.add(volumeSlider, BorderLayout.EAST);
        panel4_1.add(playButton);
        panel4_1.add(prevButton);
        panel4_1.add(nextButton);
        panel4_1.add(stopButton);
        panel4_1.add(shuffleButton);
        panel4_1.add(loopButton);
        panel4_1.add(favoriteButton);
        panel4_1.add(fullscreenButton);
        panel4.add(panel4_1, BorderLayout.SOUTH);
        panel4.add(panel4_2, BorderLayout.NORTH);
        panel5.add(lyricsScrollPane, BorderLayout.NORTH);
        panel5.add(albumImage, BorderLayout.SOUTH);


        mainFrame = new JFrame("Music Player");

        mainFrame.add(panel1, BorderLayout.NORTH);
        mainFrame.add(panel2, BorderLayout.WEST);
        mainFrame.add(panel3, BorderLayout.CENTER);
        mainFrame.add(panel5, BorderLayout.EAST);
        mainFrame.add(panel4, BorderLayout.SOUTH);

    }

    //This layout is still a work in progress,
    /*public void setMigLayout() {
        JPanel panel = new JPanel(new MigLayout());
        JPanel controlPanel = new JPanel(new MigLayout());
        controlPanel.setBorder(border);
        panel.add(currentPlaylistLabel, BorderLayout.NORTH);
        panel.add(searchField, "wrap");
        panel.add(libraryList, BorderLayout.WEST);
        panel.add(songTableScrollPane, BorderLayout.CENTER);
        panel.add(lyricsScrollPane, "wrap");
        panel.add(playlistDescription, BorderLayout.WEST);
        panel.add(albumImage, "wrap");
        //panel.add(status);
        panel.add(controlPanel, "wrap, south");

        controlPanel.add(currentSongLabel, "west");
        controlPanel.add(seekBar, "span");
        controlPanel.add(volumeSlider, "east");
        controlPanel.add(playButton);
        controlPanel.add(prevButton);
        controlPanel.add(nextButton);
        controlPanel.add(stopButton);
        controlPanel.add(shuffleButton);
        controlPanel.add(loopButton);
        controlPanel.add(favoriteButton);
        controlPanel.add(fullscreenButton);


        mainFrame = new JFrame("Music Player");
        mainFrame.add(panel);

    }*/




    public void addSongToLibraryListener(ActionListener actionListener) {
        addButton.addActionListener(actionListener);
        addSongToLibraryItem.addActionListener(actionListener);
    }

    public void addSongToPlaylistListener(ActionListener actionListener) {
        addSongToPlaylistItem.addActionListener(actionListener);
    }

    public void addPlaylistListener(ActionListener actionListener) {
        addPlaylistItem.addActionListener(actionListener);
    }

    public void addRemovePlaylistListener(ActionListener actionListener) {
        removePlaylistItem.addActionListener(actionListener);
    }

    public void addRemoveSongListener(ActionListener actionListener) {
        removeButton.addActionListener(actionListener);
        removeSongItem.addActionListener(actionListener);
    }

    public void addEditPlaylistListener(ActionListener actionListener) {
        editPlaylistItem.addActionListener(actionListener);
    }

    public void addPlaySongListener(ActionListener actionListener) {
        playButton.addActionListener(actionListener);
    }

    public void addMouseListener(MouseAdapter mouseAdapter) {
        songTable.addMouseListener(mouseAdapter);
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

    public void addShuffleListener(ActionListener actionListener) {
        shuffleButton.addActionListener(actionListener);
    }

    public void addLoopListener(ActionListener actionListener) {
        loopButton.addActionListener(actionListener);
    }

    public void addFavoriteListener(ActionListener actionListener) {
        favoriteButton.addActionListener(actionListener);
    }

    public void addFullscreenListener(ActionListener actionListener) {
        fullscreenButton.addActionListener(actionListener);
    }

    public void addListEventListener(ListSelectionListener listSelectionListener) {
        libraryList.addListSelectionListener(listSelectionListener);
    }

    public void addVolumeListener(ChangeListener changeListener) {
        volumeSlider.addChangeListener(changeListener);
    }

    public void addSeekbarListener(MouseAdapter mouseAdapter) {
        seekBar.addMouseListener(mouseAdapter);
    }

    public void addSearchListener(ActionListener actionListener) {
        searchField.addActionListener(actionListener);
    }


    /**
     * This method adds song to the table
     *
     * @param title    Title of the song
     * @param artist   Artist of the song
     * @param songPath Local path of the song.
     */

    public void addSongToTable(String title, String artist, String songPath) {


    }

    /*public void addSongToLibrary(String title, String artist, String songPath) {

        database.addSongToLibrary(title, artist, songPath);

        *//*Song song = new Song(title, artist, songPath);
        currentPlaylist.getSongs().add(song);
        songTableModel.addRow(new Object[]{song.getId(), song.getName(), song.getArtist()});*//*
    }*/

    /**
     * This method removes a song from the current playlist.
     * The song that is selected in the table is removed.
     * A song has to be selected before it can be removed.
     */

    public void removeSongFromPlaylist() {

    }


    public void playButtonPressed() {


    }


    public void playSong(Song song) {

        //stops song if a song is currently playing

    }


    public void stopSong() {


    }


    public void nextSong() {


    }

    /**
     * This method plays the previous song in the playlist.
     * If the current song is the firest one in the playlist, the
     * user will be informed and the current song keeps playing.
     */
    public void prevSong() {


    }

    /**
     * This method changes the current playlist to a playlist
     * chosen by the user. The table is cleared and the songs
     * from the selected playlist will be added to the table
     */

    public void changePlaylist() {


    }

    /**
     * This method updates the table, for example when
     * a song is removed or added to the current playlist.
     */


    /**
     * This method adds a playlist to the list of playlists.
     * First it is added to the database and then to the list of playlists.
     *
     * @param name        Name of the playlist
     * @param description Description of the playlist, optional
     */
    public void addPlaylist(String name, String description) {

    }

    /**
     * This method removes a playlist from the list of playlists.
     * If the user has chosen the song library to be removed, the
     * user will be informed that the song library cannot be removed.
     */
    public void removePlaylist() {


    }
}
