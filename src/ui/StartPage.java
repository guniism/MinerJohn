package ui;

import game.GameController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StartPage extends StackPane {

    public StartPage(Stage primaryStage) {
        this.setAlignment(Pos.CENTER);


        MainMenuPane bg= new MainMenuPane();
        bg.setAlignment(Pos.CENTER);
        VBox buttonContainer = new VBox();
        buttonContainer.setAlignment(Pos.CENTER);
        this.setTranslateY(-23);
        MainMenuButton newGameButton = new MainMenuButton(0);
        newGameButton.setOnMouseClicked(e -> {
            GameController.setUpScene();
            primaryStage.setScene(GameController.getScene());
        });

        MainMenuButton tutorialButton = new MainMenuButton(1);
        tutorialButton.setOnMouseClicked(e -> {
            System.out.println("EKOB");
        });

        MainMenuButton exitButton = new MainMenuButton(2);
        exitButton.setOnMouseClicked(e -> System.exit(0));
        buttonContainer.setSpacing(10);

        buttonContainer.getChildren().addAll(newGameButton, tutorialButton, exitButton);
        buttonContainer.setTranslateY(140);
        this.getChildren().addAll(bg,buttonContainer);
    }

    public static void showStartPage(Stage primaryStage) {
    	String path = ClassLoader.getSystemResource("start_bg.png").toString();
		Image bg = new Image(path);
        StartPage startPage = new StartPage(primaryStage);
        Scene startScene = new Scene(startPage, bg.getWidth() * GameController.getScale(), bg.getHeight() * GameController.getScale());
        primaryStage.setScene(startScene);
        primaryStage.show();
    }
}

