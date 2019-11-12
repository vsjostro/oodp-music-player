package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.controller.MusicPlayerController;
import main.view.MusicPlayerView;

public class MusicPlayer extends Application{

    public static void main(String[] args) {
        Application.launch();
    }

    public void start(Stage args0) {
        //Playlist model = new getInitialPlaylist
        MusicPlayerView view = new MusicPlayerView();
        MusicPlayerController controller = new MusicPlayerController(view);

    }

}
