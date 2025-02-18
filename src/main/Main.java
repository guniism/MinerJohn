package main;


import game.GameController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application{


	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		GameController.setUpScene();
		primaryStage.setTitle("Miner John");
		primaryStage.setScene(GameController.getScene());
		primaryStage.show();
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
		// TODO Auto-generated method stub
		launch(args);
	}

}
