package game;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class GameController {
    private static final int SCREEN_WIDTH = 1080;
    private static final int SCREEN_HEIGHT = 720;
    private static final int SCALE = 5;
    private static Scene scene;
    private static KeyboardController keyboardController;
    private static boolean gameEnd;
    
    public static void setUpScene() {
        Pane root = new MainPane();
        scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        keyboardController = new KeyboardController();
        gameEnd = false;
    }
    
    public static boolean isGameEnded() {
        return gameEnd;
    }

    public static Scene getScene() {
        return scene;
    }
    
    public static KeyboardController getKeyboardController() {
        return keyboardController;
    }
    
    // Retrieve the GamePane stored in MainPane
    public static GamePane getGamePane() {
        return ((MainPane) scene.getRoot()).getGamePane();
    }

    public static int getScale() {
        return SCALE;
    }
}
