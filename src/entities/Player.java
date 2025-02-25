package entities;

import game.GameController;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Player extends Canvas {
    private final int WIDTH;
    private final int HEIGHT;
    private double x;
    private double y;
    private int speed;

    private Image spriteSheet;
    private Image miningSpriteSheet;
    private int frameIndex;
    private final int totalFrames = 8; // 8 frames for walking animation
    private final int frameWidth = 16;
    private final int frameHeight = 32;
    
    // Mining animation properties
    private final int miningTotalFrames = 3; // 3 frames for mining animation
    private final int miningFrameWidth = 48;
    private final int miningFrameHeight = 48;
    private int miningFrameIndex = 0;
    
    // Direction
    private boolean movingUp, movingDown, movingRight, movingLeft;
    private String lastDirection = "down"; // Tracks the last direction moved
    private boolean isMoving = false; // Tracks if currently moving in any direction
    
    // State
    private boolean canMove = true;
    private boolean isMining = false;
    
    private AnimationTimer animationTimer;

    // Animation speed control
    private int frameDelay = 10; // Movement animation speed
    private int frameCounter = 0;
    private int miningFrameDelay = 15	; // Mining animation speed
    private int miningFrameCounter = 0;

    public Player() {
        WIDTH = frameWidth * GameController.getScale();
        HEIGHT = frameHeight * GameController.getScale();
        setSpeed(GameController.getScale());
        setX(1080 / 2 - WIDTH / 2);
        setY(720 / 2 - HEIGHT / 2 - HEIGHT / 4);

        // Load sprite sheets
        String playerPath = ClassLoader.getSystemResource("boy.png").toString();
        spriteSheet = new Image(playerPath);
        
        String miningPath = ClassLoader.getSystemResource("boy_useaxe.png").toString();
        miningSpriteSheet = new Image(miningPath);

        // Set canvas to the larger of the two animations to accommodate both
        this.setWidth(Math.max(WIDTH, miningFrameWidth * GameController.getScale()));
        this.setHeight(Math.max(HEIGHT, miningFrameHeight * GameController.getScale()));
//        this.setWidth(Math.max(WIDTH, frameWidth * GameController.getScale()));
//        this.setHeight(Math.max(HEIGHT, frameHeight * GameController.getScale()));

        frameIndex = 0;
        movingDown = false;
        
        setupAnimationTimer();
        draw(); // Draw initial frame
    }

    private void setupAnimationTimer() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isMining) {
                    miningFrameCounter++;
                    if (miningFrameCounter >= miningFrameDelay) {
                        miningFrameCounter = 0;
                        miningFrameIndex = (miningFrameIndex + 1) % miningTotalFrames;
                        
                        // If we've completed the animation cycle, stop mining
                        if (miningFrameIndex == 0) {
                            isMining = false;
                            setCanMove(true);
                        }
                    }
                } else {
                    isMoving = movingDown || movingUp || movingRight || movingLeft;
                    
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
                
                draw(); // Draw the current frame
            }
        };
        animationTimer.start();
    }

    public void draw() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        gc.setImageSmoothing(false);

        if (isMining) {
            // Draw mining animation
            drawMiningAnimation(gc);
        } else {
            // Draw movement animation
            drawMovementAnimation(gc);
        }
    }
    
    private void drawMovementAnimation(GraphicsContext gc) {
        int row = 0;
//        this.setWidth(Math.max(WIDTH, frameWidth * GameController.getScale()));
//        this.setHeight(Math.max(HEIGHT, frameHeight * GameController.getScale()));
        if (isMoving) {    
            if (movingRight || movingLeft) {
                row = 2; // Use walking right animation row for both right and left
            } else if (movingUp) {
                row = 1; // Walking up
            } else if (movingDown) {
                row = 0; // Walking down
            }
        } else {
            switch (lastDirection) {
                case "right": row = 2; break;
                case "up": row = 1; break;
                case "down": row = 0; break;
                case "left": row = 2; break; // Uses right sprites but flipped
            }
        }
        
        double srcX = frameIndex * frameWidth;
        double srcY = row * frameHeight;
        
        // Calculate position to center the player sprite on the canvas
        double drawX = (this.getWidth() - WIDTH) / 2;
        double drawY = (this.getHeight() - HEIGHT) / 2;

        // Check if moving left, then flip the sprite
        if (movingLeft || (!isMoving && lastDirection.equals("left"))) {
            gc.save(); // Save the current state
            gc.translate(drawX + WIDTH, drawY); // Position for flipping
            gc.scale(-1, 1); // Flip horizontally
            gc.drawImage(spriteSheet, srcX, srcY, frameWidth, frameHeight, 0, 0, WIDTH, HEIGHT);
            gc.restore(); // Restore the original state
        } else {
            // Draw normally when not moving left
            gc.drawImage(spriteSheet, srcX, srcY, frameWidth, frameHeight, drawX, drawY, WIDTH, HEIGHT);
        }
    }
    
    private void drawMiningAnimation(GraphicsContext gc) {
        int row;
//        this.setWidth(Math.max(WIDTH, miningFrameWidth * GameController.getScale()));
//        this.setHeight(Math.max(HEIGHT, miningFrameHeight * GameController.getScale()));
        // Select the correct row based on direction
        switch (lastDirection) {
            case "up":
                row = 1;
                break;
            case "right":
                row = 2;
                break;
            case "left":
                row = 2; // Uses right sprites but flipped
                break;
            case "down":
            default:
                row = 0;
                break;
        }
        
        double srcX = miningFrameIndex * miningFrameWidth;
        double srcY = row * miningFrameHeight;
        
        // Calculate position to center the mining sprite on the canvas
        double drawX = (this.getWidth() - miningFrameWidth * GameController.getScale()) / 2;
        double drawY = (this.getHeight() - miningFrameHeight * GameController.getScale()) / 2;
        
        // Check if mining left, then flip the sprite
        if (lastDirection.equals("left")) {
            gc.save(); // Save the current state
            gc.translate(drawX + miningFrameWidth * GameController.getScale(), drawY); // Position for flipping
            gc.scale(-1, 1); // Flip horizontally
            gc.drawImage(miningSpriteSheet, srcX, srcY, miningFrameWidth, miningFrameHeight, 
                       0, 0+40, miningFrameWidth * GameController.getScale(), miningFrameHeight * GameController.getScale());
            gc.restore(); // Restore the original state
        } else {
            // Draw normally for other directions
            gc.drawImage(miningSpriteSheet, srcX, srcY, miningFrameWidth, miningFrameHeight, 
                       drawX, drawY+40, miningFrameWidth * GameController.getScale(), miningFrameHeight * GameController.getScale());
        }
    }
    
    public void mine() {
        if (!isMining && canMove) {
            setCanMove(false);
            isMining = true;
            
            // Reset frame index to start animation from beginning
            miningFrameIndex = 0;
            miningFrameCounter = 0;
        }
    }
    
    public void setMovingDown(boolean movingDown) {
        this.movingDown = movingDown;
    }
    
    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }
    
    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }
    
    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public double getX() {
        return x;
    }

    public void setX(double d) {
        this.x = d;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    public String getLastDirection() {
        return lastDirection;
    }

    public boolean canMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
    
    public boolean isMining() {
        return isMining;
    }
}