package entities;

import game.GameController;
import javafx.animation.PauseTransition;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;
import world.Block;
import java.util.List;

public class Zombie extends Monster {
    private final int WIDTH;
    private final int HEIGHT;
    private Image spriteSheet, zombieBitingSpriteSheet, zombieDeathSpriteSheet;
    private int frameIndex;
    private int totalFrames = 8;
    private final int frameWidth = 16;
    private final int frameHeight = 32;
    private Player player;

    private int frameDelay = 10;
    private int frameCounter = 0;
    private double moveSpeed = 0.5;
    private String lastDirection = "down";
    private String currentAnimation = "";
    private double visionRange = 40 * GameController.getScale();
    private double randomDx = 0;
    private double randomDy = 0;
    private int randomWalkCounter = 0;
    private final int randomWalkChangeInterval = 60;

    private int deadFrameWidth;
    private int deadFrameHeight;

    private boolean isDead = false;
    private boolean isBiting = false;
    private final int bitingTotalFrames = 3;
    private final int bitingFrameWidth = 16;
    private final int bitingFrameHeight = 32;
    private int bitingFrameIndex = 0;
    private int bitingFrameDelay = 30;
    private int bitingFrameCounter = 0;
    
    // Field to lock the orientation of the death animation.
    private String deathFacing = "";

    public Zombie(double x, double y, int blood, int damage, int speed, Player player) {
        super(x, y, blood, damage, speed);
        this.boxWidth = 10;
        this.boxHeight = 17;
        this.plusToCenX = 8;
        this.plusToCenY = 16 + 7;

        this.player = player;
        WIDTH = frameWidth * GameController.getScale();
        HEIGHT = frameHeight * GameController.getScale();

        // Load spritesheets
        String path = ClassLoader.getSystemResource("zombie.png").toString();
        spriteSheet = new Image(path);

        path = ClassLoader.getSystemResource("zombie-attack-sprite.png").toString();
        zombieBitingSpriteSheet = new Image(path);

        String deathPath = ClassLoader.getSystemResource("zombie-dead.png").toString();
        zombieDeathSpriteSheet = new Image(deathPath);
        deadFrameWidth = (int) zombieDeathSpriteSheet.getWidth() / 4;
        deadFrameHeight = (int) zombieDeathSpriteSheet.getHeight();

        int mainSize = 32 * GameController.getScale();
        this.setWidth(mainSize);
        this.setHeight(mainSize);
        this.setLayoutX(x);
        this.setLayoutY(y);

        frameIndex = 0;
        setAnimation("walk_down");
        draw();
    }

    @Override
    public void update() {
        if (!isDead && !currentAnimation.equals("death")) {
            moveTowardPlayer();
        }
        animate();
        draw();
    }

    private void animate() {
        frameCounter++;
        if (frameCounter >= frameDelay) {
            frameCounter = 0;
            if (currentAnimation.equals("death")) {
                if (frameIndex < totalFrames - 1) {
                    frameIndex++;
                }
            } else {
                frameIndex = (frameIndex + 1) % totalFrames;
            }
        }
        if (isDead && frameIndex == totalFrames - 1) {
            GameController.getGamePane().getChildren().remove(this);
            GameController.getGamePane().getMonsters().remove(this);
        }
    }

    private void setAnimation(String state) {
        if (currentAnimation.equals(state)) {
            return;
        }
        currentAnimation = state;
        switch (state) {
            case "damaged_down":
                frameIndex = 0;
                totalFrames = 1;
                frameDelay = 50;
                lastDirection = "down";
                break;
            case "walk_down":
                frameIndex = 0;
                totalFrames = 8;
                frameDelay = 15;
                lastDirection = "down";
                break;
            case "damaged_up":
                frameIndex = 0;
                totalFrames = 1;
                frameDelay = 50;
                lastDirection = "up";
                break;
            case "walk_up":
                frameIndex = 0;
                totalFrames = 8;
                frameDelay = 15;
                lastDirection = "up";
                break;
            case "damaged_right":
                frameIndex = 0;
                totalFrames = 1;
                frameDelay = 50;
                lastDirection = "right";
                break;
            case "walk_right":
                frameIndex = 0;
                totalFrames = 8;
                frameDelay = 15;
                lastDirection = "right";
                break;
            case "damaged_left":
                frameIndex = 0;
                totalFrames = 1;
                frameDelay = 50;
                lastDirection = "left";
                break;
            case "walk_left":
                frameIndex = 0;
                totalFrames = 8;
                frameDelay = 15;
                lastDirection = "left";
                break;
            case "death":
                frameIndex = 0;
                totalFrames = 5;
                frameDelay = 50;
                // Do not update lastDirection; deathFacing will lock the orientation.
                break;
        }
    }

    private void moveTowardPlayer() {
        if (isBiting || currentAnimation.startsWith("damaged") || currentAnimation.equals("death"))
            return;

        double playerX = player.getLayoutX() + 16 * GameController.getScale();
        double playerY = player.getLayoutY() + 8 * GameController.getScale();
        double zombieX = this.getLayoutX();
        double zombieY = this.getLayoutY();

        double diffX = playerX - zombieX;
        double diffY = playerY - zombieY;
        double distance = Math.sqrt(diffX * diffX + diffY * diffY);

        if (distance > visionRange) {
            randomWalk();
            return;
        }
        if (distance < 20 * GameController.getScale()) {
            startBiting();
            return;
        }

        double dx = (diffX / distance) * moveSpeed;
        double dy = (diffY / distance) * moveSpeed;

        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (diffX > 0)
                setAnimation("walk_right");
            else
                setAnimation("walk_left");
        } else {
            if (diffY > 0)
                setAnimation("walk_down");
            else
                setAnimation("walk_up");
        }

        if (!isColliding(dx, 0) && !isOnInvalidCell(zombieX + dx, zombieY) && !isZombieColliding(dx, 0))
            this.setLayoutX(zombieX + dx);
        if (!isColliding(0, dy) && !isOnInvalidCell(zombieX, zombieY + dy) && !isZombieColliding(0, dy))
            this.setLayoutY(zombieY + dy);
    }

    private void randomWalk() {
        randomWalkCounter++;
        if (randomWalkCounter >= randomWalkChangeInterval) {
            randomWalkCounter = 0;
            double angle = Math.random() * 2 * Math.PI;
            randomDx = Math.cos(angle) * moveSpeed;
            randomDy = Math.sin(angle) * moveSpeed;

            if (Math.abs(randomDx) > Math.abs(randomDy)) {
                if (randomDx > 0)
                    setAnimation("walk_right");
                else
                    setAnimation("walk_left");
            } else {
                if (randomDy > 0)
                    setAnimation("walk_down");
                else
                    setAnimation("walk_up");
            }
        }

        double newX = this.getLayoutX() + randomDx;
        double newY = this.getLayoutY() + randomDy;
        if (!isColliding(randomDx, 0) && !isOnInvalidCell(newX, this.getLayoutY()) && !isZombieColliding(randomDx, 0))
            this.setLayoutX(newX);
        if (!isColliding(0, randomDy) && !isOnInvalidCell(this.getLayoutX(), newY) && !isZombieColliding(0, randomDy))
            this.setLayoutY(newY);
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
            if (nextX + zombieWidth > blockLeft && nextX < blockRight &&
                nextY + zombieHeight > blockTop && nextY < blockBottom) {
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
        List<Monster> zombies = GameController.getGamePane().getMonsters();
        for (Monster zombie : zombies) {
            if (zombie instanceof Zombie && zombie != this) {
                double otherX = zombie.getLayoutX();
                double otherY = zombie.getLayoutY();
                double collisionPadding = 4 * GameController.getScale();
                double otherLeft = otherX + collisionPadding;
                double otherRight = otherX + zombieWidth - collisionPadding;
                double otherTop = otherY + collisionPadding;
                double otherBottom = otherY + zombieHeight - collisionPadding;
                if (nextX + zombieWidth > otherLeft && nextX < otherRight &&
                    nextY + zombieHeight > otherTop && nextY < otherBottom) {
                    return true;
                }
            }
        }
        return false;
    }

    public void draw() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        gc.setImageSmoothing(false);
        if (currentAnimation.equals("death") || isDead) {
            drawDeathAnimation(gc);
        } else if (isBiting) {
            drawBitingAnimation(gc);
        } else {
            drawMovementAnimation(gc);
        }
    }

    private void drawDeathAnimation(GraphicsContext gc) {
        double srcX = frameIndex * deadFrameWidth;
        double srcY = 0;
        double drawX = (this.getWidth() - (deadFrameWidth + 12) * GameController.getScale()) / 2;
        double drawY = (this.getHeight() - (deadFrameHeight + 2) * GameController.getScale()) / 2;
        if (deathFacing.equals("left")) {
            gc.save();
            gc.translate(drawX + deadFrameWidth * GameController.getScale() + 8 * GameController.getScale(), drawY);
            gc.scale(-1, 1);     
            gc.drawImage(zombieDeathSpriteSheet, srcX, srcY, deadFrameWidth, deadFrameHeight,
                         0, 0, deadFrameWidth * GameController.getScale(), deadFrameHeight * GameController.getScale());
            gc.restore();
        } else {
            gc.drawImage(zombieDeathSpriteSheet, srcX, srcY, deadFrameWidth, deadFrameHeight,
                         drawX, drawY, deadFrameWidth * GameController.getScale(), deadFrameHeight * GameController.getScale());
        }
    }

    private void drawBitingAnimation(GraphicsContext gc) {
        int row;
        switch (lastDirection) {
            case "up": row = 1; break;
            case "right": row = 2; break;
            case "left": row = 2; break;
            case "down":
            default: row = 0; break;
        }
        double srcX = bitingFrameIndex * bitingFrameWidth;
        double srcY = row * bitingFrameHeight;
        double drawX = (this.getWidth() - 2 * bitingFrameWidth * GameController.getScale()) / 2;
        double drawY = (this.getHeight() - bitingFrameHeight * GameController.getScale()) / 2;
        if (lastDirection.equals("left")) {
            gc.save();
            gc.translate(drawX + bitingFrameWidth * GameController.getScale(), drawY);
            gc.scale(-1, 1);
            gc.drawImage(zombieBitingSpriteSheet, srcX, srcY, bitingFrameWidth, bitingFrameHeight,
                         0, 0, bitingFrameWidth * GameController.getScale(), bitingFrameHeight * GameController.getScale());
            gc.restore();
        } else {
            gc.drawImage(zombieBitingSpriteSheet, srcX, srcY, bitingFrameWidth, bitingFrameHeight,
                         drawX, drawY, bitingFrameWidth * GameController.getScale(), bitingFrameHeight * GameController.getScale());
        }
        bitingFrameCounter++;
        if (bitingFrameCounter >= bitingFrameDelay) {
            bitingFrameCounter = 0;
            bitingFrameIndex++;
            if (bitingFrameIndex >= bitingTotalFrames) {
                bitingFrameIndex = 0;
                if (isPlayerInRange()) {
                    GameController.getGamePane().reducePlayerHealth(1);
                }
                isBiting = false;
            }
        }
    }

    private void drawMovementAnimation(GraphicsContext gc) {
        int row;
        if (currentAnimation.startsWith("damaged")) {
            switch (lastDirection) {
                case "down": row = 3; break;
                case "up": row = 4; break;
                case "right": row = 5; break;
                case "left": row = 5; break;
                default: row = 3;
            }
        } else {
            switch (lastDirection) {
                case "up": row = 1; break;
                case "down": row = 0; break;
                default: row = 2; break;
            }
        }
        double srcX = frameIndex * frameWidth;
        double srcY = row * frameHeight;
        if (lastDirection.equals("left")) {
            gc.save();
            gc.translate(WIDTH, 0);
            gc.scale(-1, 1);
            gc.drawImage(spriteSheet, srcX, srcY, frameWidth, frameHeight,
                         0, 0, WIDTH, HEIGHT);
            gc.restore();
        } else {
            gc.drawImage(spriteSheet, srcX, srcY, frameWidth, frameHeight,
                         0, 0, WIDTH, HEIGHT);
        }
    }

    private void startBiting() {
        isBiting = true;
        bitingFrameIndex = 0;
        bitingFrameCounter = 0;
    }

    @Override
    public void staggerAnimation() {
        isBiting = false;
        setAnimation("damaged_" + lastDirection);
        PauseTransition pause = new PauseTransition(Duration.millis(200));
        pause.setOnFinished(e -> setAnimation("walk_" + lastDirection));
        pause.play();
    }

    @Override
    public void playDeathAnimation() {
        if (isDead)
            return;
        isDead = true;
        isBiting = false;
        deathFacing = lastDirection; // Lock the death facing
        setAnimation("death");
		if(lastDirection.equals("left")) {
			this.setLayoutX(this.getLayoutX() - 10 * GameController.getScale());
		}
		else {
			this.setLayoutX(this.getLayoutX() - 2 * GameController.getScale());
		}
		this.setLayoutY(this.getLayoutY() + GameController.getScale());
    }

    public void takeDamage(int damage) {
        this.blood -= damage;
        if (this.blood <= 0 && !isDead) {
            playDeathAnimation();
        } else {
            staggerAnimation();
        }
    }

    private boolean isOnInvalidCell(double x, double y) {
        int gridX = (int) (x / (16 * GameController.getScale()));
        int gridY = (int) (y / (16 * GameController.getScale()));
        int[][] map = GameController.getGamePane().getMapBlock();
        if (gridY < 5 || gridY >= map.length - 5 || gridX < 5 || gridX >= map[0].length - 5) {
            return true;
        }
        return map[gridY][gridX] == -1;
    }
}
