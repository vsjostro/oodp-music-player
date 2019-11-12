package main.controller;

import main.view.MusicPlayerView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The controller for the application. Handles the events when buttons
 * are pressed.
 *
 * @author Viktor
 * @version 1.0
 */

public class MusicPlayerController {

    private MusicPlayerView view;
    //private MusicPlayerModel model;

    public MusicPlayerController(MusicPlayerView view) {
        //this.model = model;
        this.view = view;

        view.addSongListener(new AddSongListener());
        view.addRemoveListener(new RemoveSongListener());
        view.addPlaylistListener(new AddPlaylistListener());
        view.removePlaylistListener(new RemovePlaylistListener());
        view.addPlaySongListener(new PlaySongListener());
        view.addStopSongListener(new StopSongListener());
        view.addNextSongListener(new NextSongListener());
        view.addPrevSongListener(new PrevSongListener());
        view.addListEventListener(new ListEventListener());

    }

    class AddSongListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            try {
                JFileChooser chooser = new JFileChooser();

                chooser.showOpenDialog(null);
                System.out.println(chooser.getSelectedFile().getPath());
                String songName = chooser.getSelectedFile().getName();
                String songPath = chooser.getSelectedFile().getPath();

                //String song = JOptionPane.showInputDialog(null,"Enter song title");
                String artist = JOptionPane.showInputDialog(null, "Enter artist name");

                if (songName.isEmpty()) {
                    System.out.println("no title given");
                } else {
                    view.addSongToTable(songName, artist, songPath);
                }
            } catch (NullPointerException n) {
                System.out.println(n);
            }

        }
    }

    class AddPlaylistListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String name = JOptionPane.showInputDialog(null, "Enter playlist name");
            String description = JOptionPane.showInputDialog(null, "Enter description (optional)");

            if (name.equals("") || name == null) {
                System.out.println("Playlist name cannot be empty");
            } else {
                view.addPlaylist(name, description);
            }

        }
    }

    class RemovePlaylistListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            view.removePlaylist();
        }
    }

    class RemoveSongListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.removeSongFromPlaylist();
        }
    }

    class PlaySongListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            view.playButtonPressed();
        }
    }

    class StopSongListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            view.stopSong();
        }
    }

    class NextSongListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            view.nextSong();
        }
    }

    class PrevSongListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            view.prevSong();
        }
    }

    class ListEventListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            view.changePlaylist();
        }
    }
}
