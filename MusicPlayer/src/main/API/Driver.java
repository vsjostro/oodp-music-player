package main.API;

public class Driver {
    public static void main(String[] args) throws Exception {
        String userInput = "tongue tied grouplove";
//        Song song = new Song(userInput);

        APIDetails songAPIDetails = new APIDetails(userInput);
        String lyrics = songAPIDetails.getSongLyricsPath();
        System.out.println(lyrics);

//        final ClassLoader loader = Driver.class.getClassLoader();
//        System.out.println(loader.getResource("resources/images"));

    }
}
