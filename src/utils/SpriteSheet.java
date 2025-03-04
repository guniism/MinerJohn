package utils;

import game.GameController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SpriteSheet extends Canvas {

	public SpriteSheet(String filename, int tileWidth, int tileHeight, int r, int c, int mode) {
//		mode x y
//		0 = row col
//		1 = srcX srcY
		String path = ClassLoader.getSystemResource(filename).toString();
		Image spriteSheet = new Image(path);

//        int tileWidth = 48;
//        int tileHeight = 17;

		this.setWidth(tileWidth * GameController.getScale());
		this.setHeight(tileHeight * GameController.getScale());

		GraphicsContext gc = this.getGraphicsContext2D();
		gc.setImageSmoothing(false);
		gc.scale(GameController.getScale(), GameController.getScale());

//        int col = 0; // ตำแหน่งเป็น Grid
//        int row = 0;

		double srcX = c;
		double srcY = r;

		if (mode == 0) {
			srcX *= tileWidth;
			srcY *= tileHeight;
		}

		double destX = 0;
		double destY = 0;

		gc.drawImage(spriteSheet, srcX, srcY, tileWidth, tileHeight, destX, destY, tileWidth, tileHeight);
	}
}
