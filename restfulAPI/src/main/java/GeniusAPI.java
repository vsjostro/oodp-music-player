import java.net.URLEncoder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class GeniusAPI {
    public static void main(String[] args) throws Exception {
        HttpResponse<String> response = Unirest.get("https://genius.p.rapidapi.com/artists/16775/songs")
                .header("x-rapidapi-host", "genius.p.rapidapi.com")
                .header("x-rapidapi-key", "f5b9667b81mshe5ad2b60905317bp1db8d5jsn21ec6987e4bd")
                .asString();

        System.out.println(response.getStatus());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(response.getBody().toString());
        String prettyJsonString = gson.toJson(je);
        System.out.println(prettyJsonString);

    }
}
