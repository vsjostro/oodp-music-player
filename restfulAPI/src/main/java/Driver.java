import javax.swing.*;

public class Driver {
    public static void main(String[] args) throws Exception {
        String userInput = "it might be time tame impala";
        Song song = new Song(userInput);

        APIDetails songAPIDetails = song.getApiDetails();
        String lyrics = songAPIDetails.getSongLyrics();
//        System.out.println(songAPIDetails.getSongName());
        LyricsWindow frame = new LyricsWindow(lyrics);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
