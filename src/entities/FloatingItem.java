package entities;

import game.GameController;
import javafx.scene.layout.Pane;
import utils.SpriteSheet;

public class FloatingItem extends Pane {
	private SpriteSheet spriteSheet;
//	private int GridX;
//	private int GridY;
	private int row;
	private int col;
	
	public FloatingItem(int row, int col, int x, int y) {
		this.row = row;
		this.col = col;
		this.spriteSheet = new SpriteSheet("item-sprite.png", 16, 16, row, col, 0);
		this.setLayoutX(x * 16 * GameController.getScale());
		this.setLayoutY(y * 16 * GameController.getScale());
		this.getChildren().add(spriteSheet);
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
}
