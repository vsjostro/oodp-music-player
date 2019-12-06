package main.API;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

/** A class that handles API requests and information gathering from
 *  the APIs used. This class is used to get information about a given
 *  song passed in by the user.
 *
 *
 * @author Parker Mitchell
 * @version 1.0
 */

/* Current Client Access Token: Fpdwsgm7gI_ma2g6KbXe6oLGpSTvU1VG2qWQaBMRXQ8HSeYVLSeWH9L4bqrvPGvI */

public class APIDetails {
    /** Private String object that contains the name of the song */
    private String songName;

    /** Private String object that contains the name of the artist */
    private String artistName;

    /** Private String object that contains the url of a small version of the song's album artwork */
    private String smallAlbumArtURL;

    /** Private String object that contains the url of a big version of the song's album artwork */
    private String bigAlbumArtURL;

    /** Private String object that contains the path to the lyrics file for the song */
    private String songLyricsPath;

    /** Private String object that contains the path to the album artwork of the song */
    private String albumArtPath;


    /** The constructor for the API details object. This takes in a String, "userInput",
     *  and creates an API details object.
     *
     * @param userInput
     */
    public APIDetails(String userInput) throws Exception {
        String[] apiDetails = getAPIDetails(userInput);
        if (apiDetails == null) {
            this.songName = null;
            this.artistName = null;
            this.smallAlbumArtURL = null;
            this.bigAlbumArtURL = null;
            this.albumArtPath = null;
            this.songLyricsPath = null;
        }
        else {
            this.songName = apiDetails[0];
            this.artistName = apiDetails[1];
            this.smallAlbumArtURL = apiDetails[2];
            this.bigAlbumArtURL = apiDetails[3];
            this.albumArtPath = apiDetails[4];
            this.songLyricsPath = extractLyrics(this.songName, this.artistName,
                    "Fpdwsgm7gI_ma2g6KbXe6oLGpSTvU1VG2qWQaBMRXQ8HSeYVLSeWH9L4bqrvPGvI");
        }
    }

    /** Method to drive the creation and collection of the fields required for the constructor.
     *  This method gets the song title, artist name, artwork, and lyrics. It then stores that
     *  data in a String array and returns the array.
     *
     * @param String userInput
     * @return A string array of the fields of the APIDetails object
     * @throws UnirestException Throws this in case there is an issue getting the API details via the request
     */
    // MAKE SURE TO PASS IN USER INPUT STRING
    private String[] getAPIDetails(String userInput) throws UnirestException {
        String[] apiDetails = new String[5];

        try {
            HttpResponse<String> response = getHTTPResponse(userInput);

            // CHECK FOR NULL RESPONSE
            if (response == null) {
                return null;
            }

            JsonObject jobject = createJSONObject(response);
            JsonObject song = getSongObject(jobject);

            apiDetails[0] = extractSongTitle(song);
            apiDetails[1] = extractArtistName(song);
            apiDetails[2] = extractSmallArtURL(song);
            apiDetails[3] = extractBigArtURL(song);

            String songName = apiDetails[0];
            String artistName = apiDetails[1];
            String bigArtURL = apiDetails[3];

            apiDetails[4] = getAlbumArtPath(songName, artistName, bigArtURL);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return apiDetails;
    }

    /**
     * Getter method for the songName field.
     *
     * @return String songName
     * */
    public String getSongName() {
        return this.songName;
    }

    /**
     * Getter method for the artistName field.
     *
     * @return String artistName
     * */
    public String getArtistName() {
        return this.artistName;
    }

    /**
     * Getter method for the smallAlbumArtURL field.
     *
     * @return String smallAlbumArtURL
     * */
    public String getSmallAlbumArtURL() {
        return this.smallAlbumArtURL;
    }

    /**
     * Getter method for the bigAlbumArtURL field.
     *
     * @return String bigAlbumArtURL
     * */
    public String getBigAlbumArtURL() {
        return this.bigAlbumArtURL;
    }

    public String getAlbumArtPath() { return this.albumArtPath; }

    /**
     * Getter method for the songLyricsPath field.
     *
     * @return String songLyricsPath
     * */
    public String getSongLyricsPath() {
        return this.songLyricsPath;
    }

    /**
     * Method to check the status of the API request
     *
     * @param int statusCode
     *
     * @return boolean true if the status code is 200 (meaning no API request issues), false otherwise
     * */
    private boolean checkAPIStatus(int statusCode) {
        return statusCode == 200;
    }


    /**
     * Method to get an HTTPResponse from the Genius Music API
     *
     * @param String userInput
     *
     * @return HTTPResponse object with the Genius API information about the song
     * */
    // MAKE SURE KEY IS CORRECT/UPDATED
    // takes in the user input name for the song and artist (we should tell them to follow certain format)
    private HttpResponse<String> getHTTPResponse(String userInput) throws UnirestException {
        HttpResponse<String> response = null;

        try {
            String transformed = userInput.replaceAll(" ", "%20");
            String unirestURL = "https://genius.p.rapidapi.com/search?q=" + transformed;

            response = Unirest.get(unirestURL)
                    .header("x-rapidapi-host", "genius.p.rapidapi.com")
                    .header("x-rapidapi-key", "f5b9667b81mshe5ad2b60905317bp1db8d5jsn21ec6987e4bd")
                    .asString();

            int responseStatusCode = response.getStatus();

            if (!checkAPIStatus(responseStatusCode)) {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * Method to create a JsonObject from the input response, so that we can parse the JSON
     * and retrieve the song information
     *
     * @param HTTResponse response
     *
     * @return JsonObject jobject
     * */
    private JsonObject createJSONObject(HttpResponse<String> response) {
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(response.getBody().toString());
        JsonObject jobject = je.getAsJsonObject();

        return jobject;
    }

    /**
     * Method to get the correct song (the song the user requested) from the API response.
     *
     * @param JsonObject jobject
     *
     * @return JsonObject song
     * */
    private JsonObject getSongObject(JsonObject jobject) {
        JsonArray matchingSongs = jobject.getAsJsonObject("response").getAsJsonArray("hits");
        JsonObject song = matchingSongs.get(0).getAsJsonObject().getAsJsonObject("result");

        return song;
    }

    /**
     * Method to get the correct song title from the API response.
     *
     * @param JsonObject song
     *
     * @return String song title
     * */
    private String extractSongTitle(JsonObject song) {
        return song.get("title_with_featured").toString();
    }

    /**
     * Method to get the correct artist name from the API response.
     *
     * @param JsonObject song
     *
     * @return String artist name
     * */
    private String extractArtistName(JsonObject song) {
        return song.getAsJsonObject("primary_artist").get("name").toString();
    }

    /**
     * Method to get the correct URL for the small album artwork from the API response.
     *
     * @param JsonObject song
     *
     * @return String small album artwork URL
     * */
    private String extractSmallArtURL(JsonObject song) {
        return song.get("song_art_image_thumbnail_url").toString();
    }

    /**
     * Method to get the correct URL for the big album artwork from the API response.
     *
     * @param JsonObject song
     *
     * @return String big album artwork URL
     * */
    private String extractBigArtURL(JsonObject song) {
        return song.get("song_art_image_url").toString();
    }


    // TO-DO

    public String getAlbumArtPath(String songName, String artistName, String bigAlbumArtURL) throws IOException {
        String fileName = songName + artistName + ".jpg";
        fileName = fileName.replace(' ', '_');
        fileName = fileName.replace("\"", "");

        URL url = new URL(bigAlbumArtURL.replace("\"", ""));
        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
        httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");

        InputStream in = new BufferedInputStream(httpcon.getInputStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        while (-1!=(n=in.read(buf)))
        {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
        byte[] response = out.toByteArray();

        final ClassLoader loader = APIDetails.class.getClassLoader();
        //String path = loader.getResource("resources/images").getPath();

        String path = APIDetails.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = URLDecoder.decode(path, "UTF-8");
        if (decodedPath.contains("MusicPlayer.jar")) {
            decodedPath = decodedPath.replace("MusicPlayer.jar", "classes/resources/images/");
            System.out.println(decodedPath);
        } else {
            decodedPath = loader.getResource("resources/images").getPath();
            System.out.println(decodedPath);
        }

        FileOutputStream fos = new FileOutputStream(decodedPath + "/" +  fileName);
        fos.write(response);
        fos.close();

        return decodedPath + "/" + fileName;
    }

    /**
     * Method to get the song lyrics path from the API response. Checks what operating system the program
     * is running on and executes a program to parse for the lyrics accordingly. Takes in the song name
     * and artist name, as well as the accessToken for the API to get the lyrics and returns a String
     * containing the path to the lyrics txt file.
     *
     * @param String songName, String artistName, String clientAccessToken
     *
     * @return String lyrics
     * */
    private String extractLyrics(String songName, String artistName, String clientAccessToken) throws Exception {
        String line;

        String fileName = songName + artistName + ".txt";
        fileName = fileName.replace(' ', '_');
        fileName = fileName.replace("\"", "");

        final ClassLoader loader = APIDetails.class.getClassLoader();
        //String path = loader.getResource("resources/lyrics").getPath();

        String path = APIDetails.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = URLDecoder.decode(path, "UTF-8");
        System.out.println(decodedPath);
        if (decodedPath.contains("MusicPlayer.jar")){
            decodedPath = decodedPath.replace("MusicPlayer.jar", "classes/resources/lyrics/");
            System.out.println(decodedPath);
        } else {
            decodedPath = loader.getResource("resources/lyrics").getPath();
            System.out.println(decodedPath);
        }

        PrintWriter writer = new PrintWriter(decodedPath + "/" + fileName, "UTF-8");

        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("mac")) {
                final ClassLoader execLoader = APIDetails.class.getClassLoader();
                String execPath = execLoader.getResource("main/API/dist/lyricParser").getPath();

//                Process process = Runtime.getRuntime().exec(new String[]{
//                        "./MusicPlayer/src/main/API/dist/lyricParser",
//                        songName, artistName, clientAccessToken});
//                process.waitFor();
                Process process = Runtime.getRuntime().exec(new String[]{
                        execPath, songName, artistName, clientAccessToken});
                process.waitFor();

                BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

                // To get rid of the extra leading information that is unneeded
                stdInput.readLine();
                stdInput.readLine();
                stdInput.readLine();

                while ((line = stdInput.readLine()) != null) {
                    writer.write(line);
                    writer.write("\n");
                }
                writer.close();

            }
            else if (os.contains("windows")) {
//                final ClassLoader execLoader = APIDetails.class.getClassLoader();
//                String execPath = execLoader.getResource("main/API/dist/lyricsGeniusTest.exe").getPath();

                //final ClassLoader loader = APIDetails.class.getClassLoader();

                path = APIDetails.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                String exePath = URLDecoder.decode(path, "UTF-8");
                if (exePath.contains("MusicPlayer.jar")) {
                    exePath = exePath.replace("MusicPlayer.jar", "classes/main/API/dist/lyricsGeniusTest.exe");
                    System.out.println(exePath);
                }else {
                    exePath = loader.getResource("main/API/dist/lyricsGeniusTest.exe").getPath();
                }

                Process process = Runtime.getRuntime().exec(new String[]{
                        exePath,
                        songName, artistName, clientAccessToken});
                process.waitFor();
                //Process process = Runtime.getRuntime().exec(new String[]{
                //        execPath, songName, artistName, clientAccessToken});
                //process.waitFor();

                BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

                // To get rid of the extra leading information that is unneeded
                stdInput.readLine();
                stdInput.readLine();
                stdInput.readLine();

                while ((line = stdInput.readLine()) != null) {
                    writer.write(line);
                    writer.write("\n");
                }
                writer.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return decodedPath + "/" + fileName;
    }
}
