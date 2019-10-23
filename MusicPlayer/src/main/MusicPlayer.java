package main;

import main.controller.MusicPlayerController;
import main.model.MusicPlayerModel;
import main.view.MusicPlayerView;

public class MusicPlayer {

    public static void main(String[] args) {

        MusicPlayerModel model = new MusicPlayerModel();
        MusicPlayerView view = new MusicPlayerView(model);
        MusicPlayerController controller = new MusicPlayerController(model, view);
    }

}
