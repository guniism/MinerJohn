package world;

import game.GameController;
import game.KeyboardController;
import javafx.scene.canvas.GraphicsContext;

public class LadderUp extends Block implements Interactable{

	public LadderUp() {
		super("block-sprite.png");
		this.setWidth(16 * GameController.getScale());
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
		KeyboardController.isEsc();
      	if(!KeyboardController.isBag() && !KeyboardController.isEsc()) {
        	KeyboardController.setEsc(true);
        	GameController.getMainPane().createEsc(KeyboardController.isEsc());
    	}
	}

}
