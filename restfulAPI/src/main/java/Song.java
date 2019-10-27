//import com.mashape.unirest.http.exceptions.UnirestException;
//
///** A class to represent a "Song" object. A Song is an object that contains the necessary
// *  fields required to play a music file, along with additional information about the file/song.
// *  A song is created by first retrieving details about it from an API, and then storing that
// *  info in the object.
// *
// * @author Parker Mitchell
// * @version 1.0
// * */
//public class Song {
//
//    // DISCUSS WHAT OTHER FIELDS WE NEED LIKE FILETYPE, FILEPATH
//
//    /** Private field for song, an APIDetails object.
//     *  APIDetails contains the following information about the song:
//     *      - String songName
//     *      - String artistName
//     *      - String smallAlbumArtURL
//     *      - String bigAlbumArtURL
//     *
//     * @see APIDetails
//     * */
//    private APIDetails apiDetails;
//
//
//    /** Constructor for Song object
//     *
//     * @param String name input value
//     * */
//    public Song (String userInput) throws UnirestException {
//        this.apiDetails = new APIDetails(userInput);
//    }
//
//    public APIDetails getApiDetails() {
//        return this.apiDetails;
//    }
//
//}
