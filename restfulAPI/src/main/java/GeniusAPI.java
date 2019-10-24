import java.net.URLEncoder;
import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import java.util.Scanner;


public class GeniusAPI {

    public static void main(String[] args) throws Exception {
//        HttpResponse<String> response = Unirest.get("https://genius.p.rapidapi.com/artists/16775/songs")
////                .header("x-rapidapi-host", "genius.p.rapidapi.com")
////                .header("x-rapidapi-key", "f5b9667b81mshe5ad2b60905317bp1db8d5jsn21ec6987e4bd")
////                .asString();

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter in your song in the following format: \"Song Name (space) Artist Name\"");
        String userInput = scan.nextLine();
        System.out.println(userInput);
        String transformed = userInput.replaceAll(" ", "%20");
        System.out.println(transformed);
        String unirestURL = "https://genius.p.rapidapi.com/search?q=" + transformed;


        HttpResponse<String> response = Unirest.get(unirestURL)
                .header("x-rapidapi-host", "genius.p.rapidapi.com")
                .header("x-rapidapi-key", "f5b9667b81mshe5ad2b60905317bp1db8d5jsn21ec6987e4bd")
                .asString();



//        System.out.println(response.getStatus());
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonParser jp = new JsonParser();
//        JsonElement je = jp.parse(response.getBody().toString());
//        JsonObject jobject = je.getAsJsonObject();
//
//        JsonArray matchingSongs = jobject.getAsJsonObject("response").getAsJsonArray("hits");
//        JsonObject song = matchingSongs.get(0).getAsJsonObject().getAsJsonObject("result");
//
//        String songName = song.get("title_with_featured").toString();
//        String artist = song.getAsJsonObject("primary_artist").get("name").toString();
//        String smallAlbumArtURL = song.get("song_art_image_thumbnail_url").toString();
//        String bigAlbumArtURL = song.get("song_art_image_url").toString();
//
//        String prettyJsonString = gson.toJson(song);
//        System.out.println(prettyJsonString);
//        System.out.println("--------------------------------------------------------------------------------------");
//        System.out.println(songName);
//        System.out.println(artist);
//        System.out.println(smallAlbumArtURL);
//        System.out.println(bigAlbumArtURL);

    }
}
