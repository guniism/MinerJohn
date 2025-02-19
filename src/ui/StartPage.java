package ui;

import game.GameController;
import game.MainMenuButton;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StartPage extends VBox {

    public StartPage(Stage primaryStage) {
        this.setAlignment(Pos.CENTER);

        String bgPath = ClassLoader.getSystemResource("start_bg.png").toString();
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(bgPath),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        this.setBackground(new Background(backgroundImage));

        VBox buttonContainer = new VBox(35);
        buttonContainer.setAlignment(Pos.CENTER);

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
        buttonContainer.setSpacing(20);
        buttonContainer.getChildren().addAll(newGameButton, tutorialButton, exitButton);
        buttonContainer.setTranslateY(120);
        this.getChildren().add(buttonContainer);
    }

    public static void showStartPage(Stage primaryStage) {
        StartPage startPage = new StartPage(primaryStage);
        Scene startScene = new Scene(startPage, 1220, 720);
        primaryStage.setScene(startScene);
        primaryStage.show();
    }
}
