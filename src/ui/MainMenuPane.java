package ui;

import game.GameController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

public class MainMenuPane extends StackPane {
	public MainMenuPane() {
		Canvas menuBg = new Canvas();
		String path = ClassLoader.getSystemResource("start_bg.png").toString();
		Image boy = new Image(path);

		menuBg.setWidth(boy.getWidth() * GameController.getScale());
		menuBg.setHeight(boy.getHeight() * GameController.getScale());

		GraphicsContext gc = menuBg.getGraphicsContext2D();
		gc.setImageSmoothing(false);
		gc.scale(GameController.getScale(), GameController.getScale());
		gc.drawImage(boy, 0, 0, boy.getWidth(), boy.getHeight());

		menuBg.setTranslateY(24);
		this.getChildren().add(menuBg);
	}
}
