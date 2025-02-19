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
    private int frameIndex;
    private final int totalFrames = 8; // 8 frames for walking down animation
    private final int frameWidth = 16;
    private final int frameHeight = 32;
    
    //direction
    private boolean movingUp, movingDown, movingRight, movingLeft;

    private String lastDirection = "down"; // Tracks the last direction moved
    private boolean isMoving = false; // Tracks if currently moving in any direction
    
    private AnimationTimer animationTimer;

    // Animation speed control
    private int frameDelay = 10; // Lower is faster, higher is slower
    private int frameCounter = 0;

    public Player() {
        WIDTH = frameWidth * GameController.getScale();
        HEIGHT = frameHeight * GameController.getScale();
        setSpeed(GameController.getScale());
        setX(1080 / 2 - WIDTH / 2);
        setY(720 / 2 - HEIGHT / 2 - HEIGHT / 4);

        String path = ClassLoader.getSystemResource("boy.png").toString();
        spriteSheet = new Image(path);

        this.setWidth(WIDTH);
        this.setHeight(HEIGHT);

        frameIndex = 0;
        movingDown = false;

        setupAnimationTimer();

        draw(); // Draw initial frame
    }

    private void setupAnimationTimer() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
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
                draw();
            }
        };
        animationTimer.start();
    }

    public void draw() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        gc.setImageSmoothing(false);

        int row = 0;

	    if(isMoving) {    
        	if (movingRight || movingLeft) {
	            row = 2; // Use walking right animation row for both right and left
	        } else if (movingUp) {
	            row = 1; // Walking up
	        } else if (movingDown) {
	            row = 0; // Walking down
	        }
	    }else {
	    	switch (lastDirection) {
            case "right": row = 2; break;
            case "up": row = 1; break;
            case "down": row = 0; break;
            case "left": row = 2; break; // Uses right sprites but flipped
        }
	    }
        double srcX = frameIndex * frameWidth;
        double srcY = row * frameHeight;

        // Check if moving left, then flip the sprite
        if (movingLeft ||  (!isMoving && lastDirection.equals("left"))) {
        	gc.save(); // Save the current state
            gc.translate(WIDTH, 0); // Move to right edge of canvas
            gc.scale(-1, 1); // Flip horizontally
            gc.drawImage(spriteSheet, srcX, srcY, frameWidth, frameHeight, 0, 0, WIDTH, HEIGHT);
            gc.restore(); // Restore the original state
        	
        } else {
            // Draw normally when not moving left
            gc.drawImage(spriteSheet, srcX, srcY, frameWidth, frameHeight, 0, 0, WIDTH, HEIGHT);
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
}
