package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.controller.MusicPlayerController;
import main.model.MusicPlayerModel;
import main.view.MusicPlayerView;

public class MusicPlayer extends Application{

    public static void main(String[] args) {
        Application.launch();
    }

    public void start(Stage args0) {
        MusicPlayerModel model = new MusicPlayerModel();
        MusicPlayerView view = new MusicPlayerView(model);
        MusicPlayerController controller = new MusicPlayerController(model, view);

    }

}
