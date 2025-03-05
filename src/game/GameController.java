package game;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ui.StartPage;

public class GameController {
    private static final int SCREEN_WIDTH = 1080;
    private static final int SCREEN_HEIGHT = 720;
    private static final int SCALE = 5;
    private static Scene scene;
    private static KeyboardController keyboardController;
    private static boolean gameEnd;
    private static Stage primaryStage;
    
    public static void setUpScene(Stage stage) {
      	primaryStage = stage;
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
    
    public static GamePane getGamePane() {
        return ((MainPane) scene.getRoot()).getGamePane();
    }
    
    public static MainPane getMainPane() {
        return ((MainPane) scene.getRoot());
    }

    public static int getScale() {
        return SCALE;
    }
    
    public static void goToStartPage() {
        if (primaryStage != null) {
            StartPage.showStartPage(primaryStage);
        }
    }

	public static int getScreenWidth() {
		return SCREEN_WIDTH;
	}

	public static int getScreenHeight() {
		return SCREEN_HEIGHT;
	}
    
}
