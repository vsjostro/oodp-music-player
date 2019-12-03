package main.API;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Driver {
    public static void main(String[] args) throws Exception {
        String userInput = "mind mischief tame impala";
//        Song song = new Song(userInput);

        APIDetails songAPIDetails = new APIDetails(userInput);
        String lyrics = songAPIDetails.getSongLyricsPath();
        System.out.println(lyrics);
//        LyricWindow lw = new LyricWindow(lyrics);
//        lw.makeLyricWindow(lyrics);

//        URL url = new URL("http://www.yahoo.com/image_to_read.jpg");
//        InputStream in = new BufferedInputStream(url.openStream());
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        byte[] buf = new byte[1024];
//        int n = 0;
//        while (-1!=(n=in.read(buf)))
//        {
//            out.write(buf, 0, n);
//        }
//        out.close();
//        in.close();
//        byte[] response = out.toByteArray();


    }
}
