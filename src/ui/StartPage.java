package ui;

import audio.AudioController;
import game.GameController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class StartPage extends StackPane {
	private AudioController menusound = new AudioController("menubgm_sfx");
	
    public StartPage(Stage primaryStage) {
        this.setAlignment(Pos.CENTER);
        
        MainMenuPane bg= new MainMenuPane();
        bg.setAlignment(Pos.CENTER);
        VBox buttonContainer = new VBox();
        buttonContainer.setAlignment(Pos.CENTER);
        this.setTranslateY(-23);
        MainMenuButton newGameButton = new MainMenuButton(0);
        //play menu bgm
        menusound.play();
        menusound.setVolume(0.8f);
        menusound.loop();
        
        newGameButton.setOnMouseClicked(e -> {
            GameController.setUpScene(primaryStage);
            primaryStage.setScene(GameController.getScene());
            //stop menubgm when start new game
            menusound.stop();
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
        
        this.setBackground(new Background(new BackgroundFill(Color.web("#331B17"), null, null)));
    }

    public static void showStartPage(Stage primaryStage) {
        StartPage startPage = new StartPage(primaryStage);
        Scene startScene = new Scene(startPage, GameController.getScreenWidth(), GameController.getScreenHeight());
        primaryStage.setScene(startScene);
        primaryStage.show();
    }
}
