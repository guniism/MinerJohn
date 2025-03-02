package world;

import game.GameController;
import javafx.scene.canvas.GraphicsContext;

public class LadderUp extends Block implements Interactable{

	public LadderUp() {
		super("block-sprite.png");
//		this.setOnMouseClicked(event -> response());
		
		this.setHeight(48 * GameController.getScale());
		render(this.getGraphicsContext2D());
	}

	@Override
	protected void render(GraphicsContext gc) {
		int tileWidth = 16;
		int tileHeight = 48;
		double srcX = 0;
		double srcY = 32;
		double destX = 0;
		double destY = 0;
		gc.drawImage(spriteSheet, srcX, srcY, tileWidth, tileHeight, destX, destY, tileWidth, tileHeight);
	}

	@Override
	public void response() {
		// TODO Auto-generated method stub
		
	}

}
