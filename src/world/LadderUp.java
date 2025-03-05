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

//	@Override
//	public void response() {
//		// TODO Auto-generated method stub
//		KeyboardController.isEsc();
//      	if(!KeyboardController.isBag() && !KeyboardController.isEsc()) {
//        	KeyboardController.setEsc(true);
//        	GameController.getMainPane().createEsc(KeyboardController.isEsc());
//    	}
//	}
	
	public void response() {
	    // Get player's grid position
	    int playerFootGridX = (int) ((GameController.getGamePane().getPlayer().getX()
	            + (8 + 24 - 8) * GameController.getScale()) / GameController.getScale() / 16);
	    int playerFootGridY = (int) ((GameController.getGamePane().getPlayer().getY()
	            + (32) * GameController.getScale()) / GameController.getScale() / 16);

	    // Get block's grid position
	    int blockGridX = (int) (this.getLayoutX() / GameController.getScale() / 16);
	    int blockGridY = (int) (this.getLayoutY() / GameController.getScale() / 16);

	    // Check if player is near the block (16x48)
	    boolean isNearBlock = 
	        Math.abs(playerFootGridX - blockGridX) <= 1 &&  // X-axis check
	        Math.abs(playerFootGridY - (blockGridY + 1)) <= 2; // Y-axis check (block is 48 tall)

	    // Only execute if player is near the block
	    if (isNearBlock) {
	        if (!KeyboardController.isBag() && !KeyboardController.isEsc()) {
	            KeyboardController.setEsc(true);
	            GameController.getMainPane().createEsc(KeyboardController.isEsc());
	        }
	    }
	}


}
