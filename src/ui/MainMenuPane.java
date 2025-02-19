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
		Image bg = new Image(path);

		menuBg.setWidth(bg.getWidth() * GameController.getScale());
		menuBg.setHeight(bg.getHeight() * GameController.getScale());

		GraphicsContext gc = menuBg.getGraphicsContext2D();
		gc.setImageSmoothing(false);
		gc.scale(GameController.getScale(), GameController.getScale());
		gc.drawImage(bg, 0, 0, bg.getWidth(), bg.getHeight());

		menuBg.setTranslateY(24);
		this.getChildren().add(menuBg);
	}
}
