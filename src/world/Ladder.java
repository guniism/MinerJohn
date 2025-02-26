package world;

import javafx.scene.canvas.GraphicsContext;

public class Ladder extends Block {

	public Ladder() {
		super("block.png");
	}

	@Override
	protected void render(GraphicsContext gc) {
		int tileWidth = 16;
		int tileHeight = 16;
		int col = 1;
		int row = 1;

		double srcX = col * tileWidth;
		double srcY = row * tileHeight;
		double destX = 0;
		double destY = 0;
		gc.drawImage(spriteSheet, srcX, srcY, tileWidth, tileHeight, destX, destY, tileWidth, tileHeight);
	}
}