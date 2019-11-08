package main.controller;

import main.model.MusicPlayerModel;
import main.view.MusicPlayerView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MusicPlayerController {

    private MusicPlayerView view;
    private MusicPlayerModel model;

    public MusicPlayerController(MusicPlayerModel model, MusicPlayerView view) {
        this.model = model;
        this.view = view;

        view.addSongListener(new AddSongListener());
        view.addRemoveListener(new RemoveSongListener());
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

    class RemoveSongListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.removeSong();
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
