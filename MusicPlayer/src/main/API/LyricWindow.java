package main.API;

import javax.swing.*;
import java.awt.*;

/** Class Description of LyricsWindow
 *
 *
 * @author Parker Mitchell
 * @version 1.0
 */


class LyricWindow extends JFrame
{
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 500;

    LyricWindow(String lyrics)
    {
        setTitle("Lyrics");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        Container contentPane = getContentPane();

        // add a text area with scroll bars
        JTextArea textArea = new JTextArea(lyrics, 8, 40);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);

        contentPane.add(scrollPane, BorderLayout.CENTER);
    }

    void makeLyricWindow(String lyrics) {
        LyricWindow frame = new LyricWindow(lyrics);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

