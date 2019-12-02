package main.API;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Driver {
    public static void main(String[] args) throws Exception {
        String userInput = "africa toto";
//        Song song = new Song(userInput);

        APIDetails songAPIDetails = new APIDetails(userInput);
        String lyrics = songAPIDetails.getSongLyricsPath();
        System.out.println(lyrics);
//        LyricWindow lw = new LyricWindow(lyrics);
//        lw.makeLyricWindow(lyrics);


//        try(InputStream in = new URL("https://i.ytimg.com/vi/pjRs_WT8VzM/maxresdefault.jpg").openStream()){
//            Files.copy(in, Paths.get("./MusicPlayer/src/resources/images/cat.jpg"));
//        }


    }
}
