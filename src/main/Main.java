package main;

import game.MainPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ui.StartPage;
import world.Map;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        StartPage.showStartPage(primaryStage);
        
        primaryStage.setTitle("Miner John");
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });
		primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
