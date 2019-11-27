package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.controller.MusicPlayerController;
import main.db.MusicPlayerDatabase;
import main.view.MusicPlayerView;

/**
 * The main class for the application. First a JavaFX
 * application if launched. Swing is used for the GUI
 * of this project, but JavaFX is used to play the media
 * files.
 *
 * @author Viktor
 * @version 1.0
 */

public class MusicPlayer extends Application {

    /**
     * The main method, launches the JavaFX application
     *
     * @param args Not used
     */
    public static void main(String[] args) {
        Application.launch();
    }

    /**
     * The MusicPlayerView and MusicplayerController are created
     */
    public void start(Stage args0) {
        MusicPlayerView view = new MusicPlayerView();
        MusicPlayerDatabase database = new MusicPlayerDatabase(view);
        MusicPlayerController controller = new MusicPlayerController(view, database);

    }

}
