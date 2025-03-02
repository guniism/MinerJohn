package world;

import game.GameController;
import game.MainPane;
import javafx.scene.canvas.GraphicsContext;

public class Ladder extends Block implements Interactable {
	private boolean isClicked;

	public Ladder() {
		super("block-sprite.png");
		this.isClicked = false;
//		this.setOnMouseClicked(event -> response());
		render(this.getGraphicsContext2D());
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

	@Override
	public void response() {
		// TODO Auto-generated method stub
//		System.out.println("hii");
		if (!isClicked) {
//			System.out.println("clicked");
			int playerFootGridX = (int) ((GameController.getGamePane().getPlayer().getX()
					+ (8 + 24 - 8) * GameController.getScale()) / GameController.getScale() / 16);
			int playerFootGridY = (int) ((GameController.getGamePane().getPlayer().getY()
					+ (32) * GameController.getScale()) / GameController.getScale() / 16);

			System.out.println(playerFootGridX + " " + playerFootGridY);
			System.out.println(GameController.getGamePane().getLadderX() + " " + GameController.getGamePane().getLadderY());

//			if (Math.abs(playerFootGridX - GameController.getGamePane().getLadderX()) <= 1
//					&& Math.abs(playerFootGridY - GameController.getGamePane().getLadderY()) <= 1) {
			if (Math.abs(playerFootGridX - GameController.getGamePane().getLadderX()) <= 1
			&& Math.abs(playerFootGridY - GameController.getGamePane().getLadderY()) <= 1) {
				    // Player's foot is near OR exactly aligned with the ladder
				

				
				GameController.getGamePane().getPlayer().setMining(false);
				GameController.getGamePane().getPlayer().setCanMove(false);
				GameController.getKeyboardController().setAttacking(false);
				GameController.getGamePane().enterNextFloor();
				MainPane.setFloorNum(MainPane.getFloorNum() + 1);
				this.isClicked = true;
			}
			
			
			
		}
	}
}