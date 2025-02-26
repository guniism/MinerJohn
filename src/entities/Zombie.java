package entities;

import game.GameController;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import world.Block;
import java.util.List;

public class Zombie extends Monster {
	private final int WIDTH;
	private final int HEIGHT;
	private Image spriteSheet;
	private int frameIndex;
	private final int totalFrames = 8;
	private final int frameWidth = 16;
	private final int frameHeight = 32;
	private Player player;

	private AnimationTimer animationTimer;
	private int frameDelay = 10;
	private int frameCounter = 0;

	private boolean movingUp, movingDown, movingLeft, movingRight;
	private boolean isMoving = false;
	private double moveSpeed = 0.5;
	private String lastDirection = "down";

	public Zombie(double x, double y, int blood, int damage, int speed, Player player) {
		super(x, y, blood, damage, speed);
		this.player = player;
		WIDTH = frameWidth * GameController.getScale();
		HEIGHT = frameHeight * GameController.getScale();

		String path = ClassLoader.getSystemResource("zombie.png").toString();
		spriteSheet = new Image(path);

		this.setWidth(WIDTH);
		this.setHeight(HEIGHT);
		this.setLayoutX(x);
		this.setLayoutY(y);

		frameIndex = 0;
		movingDown = false;

		setupAnimationTimer();
		draw();
	}

	private void setupAnimationTimer() {
		animationTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				moveTowardPlayer();
				animate();
				draw();
			}
		};
		animationTimer.start();
	}

	private void animate() {
		isMoving = movingUp || movingDown || movingRight || movingLeft;

		if (isMoving) {
			frameCounter++;
			if (frameCounter >= frameDelay) {
				frameCounter = 0;
				frameIndex = (frameIndex + 1) % totalFrames;
			}

			if (movingDown)
				lastDirection = "down";
			else if (movingUp)
				lastDirection = "up";
			else if (movingRight)
				lastDirection = "right";
			else if (movingLeft)
				lastDirection = "left";
		} else {
			frameIndex = 0;
		}
	}

	private void moveTowardPlayer() {
		double playerX = player.getLayoutX() + 16 * GameController.getScale();
		double playerY = player.getLayoutY() + 8 * GameController.getScale();
		double zombieX = this.getLayoutX();
		double zombieY = this.getLayoutY();

		double diffX = playerX - zombieX;
		double diffY = playerY - zombieY;

		double length = Math.sqrt(diffX * diffX + diffY * diffY);
		double dx = (diffX / length) * moveSpeed;
		double dy = (diffY / length) * moveSpeed;

		movingRight = movingLeft = movingUp = movingDown = false;

		if (Math.abs(diffX) > Math.abs(diffY)) {
			if (diffX > 0) {
				movingRight = true;
			} else {
				movingLeft = true;
			}
		} else {
			if (diffY > 0) {
				movingDown = true;
			} else {
				movingUp = true;
			}
		}

		// **Collision Check: Prevent movement through blocks and other zombies**
		boolean canMoveX = !isColliding(dx, 0) && !isZombieColliding(dx, 0);
		boolean canMoveY = !isColliding(0, dy) && !isZombieColliding(0, dy);

		if (canMoveX) {
			this.setLayoutX(zombieX + dx);
		}
		if (canMoveY) {
			this.setLayoutY(zombieY + dy);
		}
	}

	private boolean isColliding(double dx, double dy) {
		double nextX = this.getLayoutX() + dx;
		double nextY = this.getLayoutY() + dy;
		double zombieWidth = this.getWidth();
		double zombieHeight = this.getHeight();

		List<Block> blocks = GameController.getGamePane().getBlocks();

		for (Block block : blocks) {
			double blockX = block.getLayoutX();
			double blockY = block.getLayoutY();
			double blockSize = 16 * GameController.getScale();
			double collisionPadding = 4 * GameController.getScale();

			double blockLeft = blockX + collisionPadding;
			double blockRight = blockX + blockSize - collisionPadding;
			double blockTop = blockY + collisionPadding;
			double blockBottom = blockY;

			// ✅ Prevent zombies from passing through blocks
			if (nextX + zombieWidth > blockLeft && nextX < blockRight && nextY + zombieHeight > blockTop
					&& nextY < blockBottom) {
				return true;
			}
		}

		return false;
	}

	private boolean isZombieColliding(double dx, double dy) {
		double nextX = this.getLayoutX() + dx;
		double nextY = this.getLayoutY() + dy;
		double zombieWidth = this.getWidth();
		double zombieHeight = this.getHeight();

		List<Zombie> zombies = GameController.getGamePane().getZombies(); // ✅ Get all zombies

		for (Zombie zombie : zombies) {
			if (zombie == this)
				continue; // ✅ Skip self-check

			double otherX = zombie.getLayoutX();
			double otherY = zombie.getLayoutY();

			double collisionPadding = 4 * GameController.getScale();

			double otherLeft = otherX + collisionPadding;
			double otherRight = otherX + zombieWidth - collisionPadding;
			double otherTop = otherY + collisionPadding;
			double otherBottom = otherY + zombieHeight - collisionPadding;

			// ✅ Prevent zombies from stacking on each other
			if (nextX + zombieWidth > otherLeft && nextX < otherRight && nextY + zombieHeight > otherTop
					&& nextY < otherBottom) {
				return true;
			}
		}

		return false;
	}

	public void draw() {
		GraphicsContext gc = this.getGraphicsContext2D();
		gc.clearRect(0, 0, this.getWidth(), this.getHeight());
		gc.setImageSmoothing(false);

		int row = 0;

		if (isMoving) {
			if (movingRight || movingLeft) {
				row = 2;
			} else if (movingUp) {
				row = 1;
			} else if (movingDown) {
				row = 0;
			}
		} else {
			switch (lastDirection) {
			case "right":
				row = 2;
				break;
			case "up":
				row = 1;
				break;
			case "down":
				row = 0;
				break;
			case "left":
				row = 2;
				break;
			}
		}

		double srcX = frameIndex * frameWidth;
		double srcY = row * frameHeight;

		if (movingLeft || (!isMoving && lastDirection.equals("left"))) {
			gc.save();
			gc.translate(WIDTH, 0);
			gc.scale(-1, 1);
			gc.drawImage(spriteSheet, srcX, srcY, frameWidth, frameHeight, 0, 0, WIDTH, HEIGHT);
			gc.restore();
		} else {
			gc.drawImage(spriteSheet, srcX, srcY, frameWidth, frameHeight, 0, 0, WIDTH, HEIGHT);
		}
	}
}
