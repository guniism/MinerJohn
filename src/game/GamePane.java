package game;

import entities.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import world.Map;

public class GamePane extends Pane {
	private Map gameMap;
	private Player player;
	private double playerCenterAbsX;
	private double playerCenterAbsY;

	public GamePane() {
		this.gameMap = new Map();
		this.player = new Player();
		this.getChildren().add(this.gameMap);
		this.getChildren().add(this.player);
		
		this.playerCenterAbsX = this.player.getX() + this.getLayoutX();
		this.playerCenterAbsY = this.player.getY() + this.getLayoutY();


		startMovement();
	}

	private void startMovement() {
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				move();
			}
		};
		timer.start();
	}

	private void move() {
		double dx = 0, dy = 0;
		if (GameController.getKeyboardController().isMoveUp()) {
			dy -= this.player.getSpeed();
		}
		if (GameController.getKeyboardController().isMoveDown()) {
			dy += this.player.getSpeed();
		}
		if (GameController.getKeyboardController().isMoveLeft()) {
			dx -= this.player.getSpeed();
		}
		if (GameController.getKeyboardController().isMoveRight()) {
			dx += this.player.getSpeed();
		}

		// แก้เดินแนวทแยง
		if (dx != 0 || dy != 0) {
			double length = Math.sqrt(dx * dx + dy * dy);
			dx = (dx / length) * this.player.getSpeed();
			;
			dy = (dy / length) * this.player.getSpeed();
			;
		}
		
//		System.out.println(this.isOutMapX + ", " + 
//			this.isOutMapY + ", " + 
//			this.getLayoutX() + ", " + 
//			this.getLayoutY() + ", " + 
//			this.player.getX() + ", " + 
//			this.player.getY()
//		);
		
		System.out.println(this.player.getX() + this.getLayoutX() + ", " + playerCenterAbsX);
		if(this.getLayoutX() - dx >= 0) {
			this.setLayoutX(0);
		}
//		this.player.getX() + this.getLayoutX() คือ Absolute Position
		else if(this.player.getX() + this.getLayoutX() == playerCenterAbsX) {
			this.setLayoutX(this.getLayoutX() - dx);
		}

		this.setLayoutY(this.getLayoutY() - dy);

		this.player.setX(this.player.getX() + dx);
		this.player.setY(this.player.getY() + dy);
		this.player.setLayoutX(this.player.getX());
		this.player.setLayoutY(this.player.getY());
	}

}
