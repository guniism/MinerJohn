package game;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import world.Map;

public class GameController {
    private static final int SCREEN_WIDTH = 1080;
    private static final int SCREEN_HEIGHT = 720;
	private static final int SCALE = 5;
	private static Scene scene;
	private static KeyboardController keyboardController;
	private static boolean gameEnd;
	
	
	public static void setUpScene() {
		Pane root = new GamePane();

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


	public static int getScale() {
		return SCALE;
	}


	
	
}
