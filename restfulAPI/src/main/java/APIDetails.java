import java.net.URLEncoder;
import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.Scanner;

public class APIDetails {
    private String songName;
    private String artistName;
    private String smallAlbumArtURL;
    private String bigAlbumArtURL;

    public APIDetails() throws UnirestException {
        String[] apiDetails = getAPIDetails();

        this.songName = apiDetails[0];
        this.artistName = apiDetails[1];
        this.smallAlbumArtURL = apiDetails[2];
        this.bigAlbumArtURL = apiDetails[3];
    }

    // MAKE SURE TO PASS IN USER INPUT STRING
    private String[] getAPIDetails() throws UnirestException {
        String[] apiDetails = new String[4];

        HttpResponse<String> response = getHTTPResponse("slow dancing in the dark joji");

        // CHECK FOR NULL RESPONSE
        JsonObject jobject = createJSONObject(response);
        JsonObject song = getSongObject(jobject);

        apiDetails[0] = extractSongTitle(song);
        apiDetails[1] = extractArtistName(song);
        apiDetails[2] = extractSmallArtURL(song);
        apiDetails[3] = extractBigArtURL(song);

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

    private boolean checkAPIStatus(int statusCode) {
        return statusCode == 200;
    }

    // MAKE SURE KEY IS CORRECT/UPDATED
    // takes in the user input name for the song and artist (we should tell them to follow certain format)
    private HttpResponse<String> getHTTPResponse(String userInput) throws UnirestException {
        String transformed = userInput.replaceAll(" ", "%20");
        String unirestURL = "https://genius.p.rapidapi.com/search?q=" + transformed;

        HttpResponse<String> response = Unirest.get(unirestURL)
                .header("x-rapidapi-host", "genius.p.rapidapi.com")
                .header("x-rapidapi-key", "f5b9667b81mshe5ad2b60905317bp1db8d5jsn21ec6987e4bd")
                .asString();

        int responseStatusCode = response.getStatus();
        if (!checkAPIStatus(responseStatusCode)) {
            return null;
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

}