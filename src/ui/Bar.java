package ui;

import game.GameController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import utils.SpriteSheet;

public class Bar extends Pane {
	private static final int TILE_WIDTH = 34;
	private static final int TILE_HEIGHT = 8;
	private static final int SCALE = GameController.getScale();
	private Canvas innerBar;
	private GraphicsContext innerGC;
	private int maxValue;
	private double srcX;
	private double srcY;

	public Bar(int maxValue, int type) {
		this.maxValue = maxValue;
		this.srcX = (type == 1) ? 6 : 36;
		this.srcY = 8;
		
		SpriteSheet bgSprite = new SpriteSheet("bar-sprite.png", 34, 8, 0, 34, 1);
		SpriteSheet frameSprite = new SpriteSheet("bar-sprite.png", 34, 8, 0, 0, 1);
		this.getChildren().add(bgSprite);
		this.setWidth(TILE_WIDTH * SCALE);
		this.setHeight(TILE_HEIGHT * SCALE);

		innerBar = new Canvas();
		innerGC = innerBar.getGraphicsContext2D();
		innerBar.setLayoutX(2 * GameController.getScale());
		innerBar.setLayoutY(2 * GameController.getScale());
		innerGC.scale(GameController.getScale(), GameController.getScale());
		innerGC.setImageSmoothing(false);
		setBar(this.maxValue);

		this.getChildren().add(innerBar);
		this.getChildren().add(frameSprite);
	}

	public void setBar(int value) {
		int tileWidth = 30;
		int tileHeight = 4;

		String path = ClassLoader.getSystemResource("bar-sprite.png").toString();
		Image spriteSheet = new Image(path);
		innerBar.setWidth(tileWidth * GameController.getScale());
		innerBar.setHeight(tileHeight * GameController.getScale());

		innerGC.clearRect(0, 0, this.getWidth(), this.getHeight());

		double destX = -(tileWidth - (tileWidth * (double) value / this.maxValue));
		double destY = 0;
		innerGC.drawImage(spriteSheet, this.srcX, this.srcY, tileWidth, tileHeight, destX, destY, tileWidth, tileHeight);
	}
}
