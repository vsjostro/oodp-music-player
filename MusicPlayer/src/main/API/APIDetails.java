package main.API;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/** Class Description of APIDetails
 *
 *
 * @author Parker Mitchell
 * @version 1.0
 */


/* Current Client Access Token: Fpdwsgm7gI_ma2g6KbXe6oLGpSTvU1VG2qWQaBMRXQ8HSeYVLSeWH9L4bqrvPGvI */


public class APIDetails {
    private String songName;
    private String artistName;
    private String smallAlbumArtURL;
    private String bigAlbumArtURL;
    private String songLyrics;

    public APIDetails(String userInput) throws Exception {
        String[] apiDetails = getAPIDetails(userInput);

        this.songName = apiDetails[0];
        this.artistName = apiDetails[1];
        this.smallAlbumArtURL = apiDetails[2];
        this.bigAlbumArtURL = apiDetails[3];
        this.songLyrics = extractLyrics(this.songName, this.artistName,
                "Fpdwsgm7gI_ma2g6KbXe6oLGpSTvU1VG2qWQaBMRXQ8HSeYVLSeWH9L4bqrvPGvI");
    }

    // MAKE SURE TO PASS IN USER INPUT STRING
    private String[] getAPIDetails(String userInput) throws UnirestException {
        String[] apiDetails = new String[4];

        try {
            HttpResponse<String> response = getHTTPResponse(userInput);

            // CHECK FOR NULL RESPONSE
            JsonObject jobject = createJSONObject(response);
            JsonObject song = getSongObject(jobject);

            apiDetails[0] = extractSongTitle(song);
            apiDetails[1] = extractArtistName(song);
            apiDetails[2] = extractSmallArtURL(song);
            apiDetails[3] = extractBigArtURL(song);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return apiDetails;
    }

    public String getSongName() {
        return this.songName;
    }

    public String getArtistName() {
        return this.artistName;
    }

    public String getSmallAlbumArtURL() {
        return this.smallAlbumArtURL;
    }

    public String getBigAlbumArtURL() {
        return this.bigAlbumArtURL;
    }

    public String getSongLyrics() {
        return this.songLyrics;
    }

    private boolean checkAPIStatus(int statusCode) {
        return statusCode == 200;
    }

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

    private JsonObject createJSONObject(HttpResponse<String> response) {
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(response.getBody().toString());
        JsonObject jobject = je.getAsJsonObject();

        return jobject;
    }

    private JsonObject getSongObject(JsonObject jobject) {
        JsonArray matchingSongs = jobject.getAsJsonObject("response").getAsJsonArray("hits");
        JsonObject song = matchingSongs.get(0).getAsJsonObject().getAsJsonObject("result");

        return song;
    }

    private String extractSongTitle(JsonObject song) {
        return song.get("title_with_featured").toString();
    }

    private String extractArtistName(JsonObject song) {
        return song.getAsJsonObject("primary_artist").get("name").toString();
    }

    private String extractSmallArtURL(JsonObject song) {
        return song.get("song_art_image_thumbnail_url").toString();
    }

    private String extractBigArtURL(JsonObject song) {
        return song.get("song_art_image_url").toString();
    }

    // NEED TO INCLUDE PYTHON SCRIPT IN PACKAGE OR SOMETHING
    // THEN THEY NEED TO HAVE PYTHON3 ???
    // TURN .PY INTO EXECUTABLE
    private String extractLyrics(String songName, String artistName, String clientAccessToken) throws Exception {
        String line;
        StringBuilder lyrics = new StringBuilder();
        lyrics.append(songName);
        lyrics.append(" by ");
        lyrics.append(artistName);
        lyrics.append("\n\n");

        try {
            Process process = Runtime.getRuntime().exec(new String[]{"python3",
                    "./lyricParser.cpython-36.pyc",
                    songName, artistName, clientAccessToken});

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // To get rid of the extra leading information that is unneeded
            stdInput.readLine();
            stdInput.readLine();
            stdInput.readLine();

            while ((line = stdInput.readLine()) != null) {
                lyrics.append(line);
                lyrics.append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lyrics.toString();
    }
}
