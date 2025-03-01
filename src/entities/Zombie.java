package entities;

import game.GameController;
import game.MainPane;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ui.HealthStamBar;
import world.Block;
import java.util.List;

public class Zombie extends Monster {
	private final int WIDTH;
	private final int HEIGHT;
	private Image spriteSheet, zombieBitingSpriteSheet;
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

	private boolean isBiting = false;
	private final int bitingTotalFrames = 3;
	private final int bitingFrameWidth = 16;
	private final int bitingFrameHeight = 32;
	private int bitingFrameIndex = 0;
	private int bitingFrameDelay = 15;
	private int bitingFrameCounter = 0;

	public Zombie(double x, double y, int blood, int damage, int speed, Player player) {
		super(x, y, blood, damage, speed);
		this.player = player;
		WIDTH = frameWidth * GameController.getScale();
		HEIGHT = frameHeight * GameController.getScale();

		String path = ClassLoader.getSystemResource("zombie.png").toString();
		spriteSheet = new Image(path);

		path = ClassLoader.getSystemResource("zombie-attack-sprite.png").toString();
		zombieBitingSpriteSheet = new Image(path);

		this.setWidth(Math.max(WIDTH, bitingFrameWidth * GameController.getScale()));
        this.setHeight(Math.max(HEIGHT, bitingFrameHeight * GameController.getScale()));
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
		if (isBiting)
			return; // Stop moving if already biting

		double playerX = player.getLayoutX() + 16 * GameController.getScale();
		double playerY = player.getLayoutY() + 8 * GameController.getScale();
		double zombieX = this.getLayoutX();
		double zombieY = this.getLayoutY();

		double diffX = playerX - zombieX;
		double diffY = playerY - zombieY;
		double distance = Math.sqrt(diffX * diffX + diffY * diffY);

		if (distance < 20 * GameController.getScale()) { // If close enough, start biting
			startBiting();
			return;
		}

		double length = Math.sqrt(diffX * diffX + diffY * diffY);
		double dx = (diffX / length) * moveSpeed;
		double dy = (diffY / length) * moveSpeed;

		movingRight = movingLeft = movingUp = movingDown = false;

		if (Math.abs(diffX) > Math.abs(diffY)) {
			if (diffX > 0)
				movingRight = true;
			else
				movingLeft = true;
		} else {
			if (diffY > 0)
				movingDown = true;
			else
				movingUp = true;
		}

		if (!isColliding(dx, 0))
			this.setLayoutX(zombieX + dx);
		if (!isColliding(0, dy))
			this.setLayoutY(zombieY + dy);
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
	
	private boolean isPlayerInRange() {
        double playerX = player.getLayoutX();
        double playerY = player.getLayoutY();
        double playerWidth = player.getWidth();
        double playerHeight = player.getHeight();

        double zombieX = this.getLayoutX();
        double zombieY = this.getLayoutY();
        double zombieWidth = this.getWidth();
        double zombieHeight = this.getHeight();

        return playerX + playerWidth > zombieX && playerX < zombieX + zombieWidth &&
               playerY + playerHeight > zombieY && playerY < zombieY + zombieHeight;
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

		if (isBiting) {
			drawBitingAnimation(gc);
		} else {
			drawMovementAnimation(gc);
		}
	}

	private void drawBitingAnimation(GraphicsContext gc) {
        int row;
        switch (lastDirection) {
            case "up":
                row = 1;
                break;
            case "right":
                row = 2;
                break;
            case "left":
                row = 2;
                break;
            case "down":
            default:
                row = 0;
                break;
        }

        double srcX = bitingFrameIndex * bitingFrameWidth;
        double srcY = row * bitingFrameHeight;
        
        double drawX = (this.getWidth() - bitingFrameWidth * GameController.getScale()) / 2;
        double drawY = (this.getHeight() - bitingFrameHeight * GameController.getScale()) / 2;
        
        if (lastDirection.equals("left")) {
            gc.save();
            gc.translate(drawX + bitingFrameWidth * GameController.getScale(), drawY);
            gc.scale(-1, 1);
            gc.drawImage(zombieBitingSpriteSheet, srcX, srcY, bitingFrameWidth, bitingFrameHeight, 0, 0, bitingFrameWidth * GameController.getScale(), bitingFrameHeight * GameController.getScale());
            gc.restore();
        } else {
            gc.drawImage(zombieBitingSpriteSheet, srcX, srcY, bitingFrameWidth, bitingFrameHeight, drawX, drawY, bitingFrameWidth * GameController.getScale(), bitingFrameHeight * GameController.getScale());
        }

        bitingFrameCounter++;
        if (bitingFrameCounter >= bitingFrameDelay*3) {
            bitingFrameCounter = 0;
            bitingFrameIndex++;
            
            if (bitingFrameIndex >= bitingTotalFrames) {
                bitingFrameIndex = 0;
                
                // Check if player is still within range, then deal damage
                if (isPlayerInRange()) {
                	GameController.getGamePane().reducePlayerHealth(1);
                }
                
                isBiting = false; // Reset biting state after animation completes
            }
        }
    }

	private void drawMovementAnimation(GraphicsContext gc) {
		int row;
		switch (lastDirection) {
		case "right":
			row = 2;
			break;
		case "left":
			row = 2; // Uses right sprites but flipped
			break;
		case "up":
			row = 1;
			break;
		case "down":
		default:
			row = 0;
			break;
		}

		double srcX = frameIndex * frameWidth;
		double srcY = row * frameHeight;

		if (lastDirection.equals("left")) {
			gc.save();
			gc.translate(WIDTH, 0);
			gc.scale(-1, 1);
			gc.drawImage(spriteSheet, srcX, srcY, frameWidth, frameHeight, 0, 0, WIDTH, HEIGHT);
			gc.restore();
		} else {
			gc.drawImage(spriteSheet, srcX, srcY, frameWidth, frameHeight, 0, 0, WIDTH, HEIGHT);
		}
	}

	private void startBiting() {
		isBiting = true;
		bitingFrameIndex = 0;
		bitingFrameCounter = 0;
	}
	
}
