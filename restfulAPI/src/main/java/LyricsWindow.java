import javax.swing.*;
import java.awt.*;

//public class LyricsWindow {
//    public LyricsWindow(String lyrics) {
//        LyricAreaFrame frame = new LyricAreaFrame(lyrics);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);
//    }
//}

/**
 A frame with a text area and buttons for text editing
 */
public class LyricsWindow extends JFrame
{
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 500;

    LyricsWindow(String lyrics)
    {
//        System.out.println(lyrics);
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
        LyricsWindow frame = new LyricsWindow(lyrics);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

