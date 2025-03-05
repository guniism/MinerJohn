package entities;

import game.GameController;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import world.Block;

public class Slime extends Monster {
    private final int WIDTH;
    private final int HEIGHT;
    private static final int FRAME_WIDTH = 16;
    private static final int FRAME_HEIGHT = 32;
    private Image spriteSheet;
    private int frameIndex;
    private int totalFrames;
    

    private Player player;
    
    private int frameDelay = 15; 
    private int frameCounter = 0;

//    private boolean movingUp, movingDown, movingLeft, movingRight;
    private boolean jumping;
    private double jumpSpeed = 1;
    private double targetX, targetY;
    private String lastDirection = "down";
    private boolean canDamage = true;
    private boolean canJump = true;
    private final long JUMP_DELAY = 2000; // 2-second delay between jumps

    public Slime(double x, double y, int blood, int damage, int speed, Player player) {
        super(x, y, blood, damage, speed);
        this.boxWidth = 12;
        this.boxHeight = 7;
        this.plusToCenX = 8;
        this.plusToCenY = 16 + 8 + 4;
        this.player = player;
        
        WIDTH = FRAME_WIDTH * GameController.getScale();
        HEIGHT = FRAME_HEIGHT * GameController.getScale();

        String path = ClassLoader.getSystemResource("slime-sprite.png").toString();
        this.spriteSheet = new Image(path);
        
        this.setWidth(WIDTH);
        this.setHeight(HEIGHT);
        this.setLayoutX(x);
        this.setLayoutY(y);

        frameIndex = 0;
        setAnimation("idle_down");
        draw();
    }

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if(!lastDirection.equals("death")) {
			detectPlayer();
			updateJump();
			checkCollisionWithPlayer();
		}
        animate();
        draw();
	}
  
    private void animate() {
        frameCounter++;
        if (frameCounter >= frameDelay) {
            frameCounter = 0;
            frameIndex = (frameIndex + 1) % totalFrames;
        }
        if(lastDirection.equals("death") && frameIndex == totalFrames - 1) {
//        	System.out.println("dead " + frameIndex);
        	GameController.getGamePane().getChildren().remove(this); // Remove slime
            GameController.getGamePane().getMonsters().remove(this); // Remove from monster list
        }
    }

    private void setAnimation(String state) {
        switch (state) {
            case "idle_down": frameIndex = 0; totalFrames = 2; frameDelay = 50; lastDirection = "down"; break;
            case "jump_down": frameIndex = 0; totalFrames = 7; frameDelay = 15; lastDirection = "down"; break;
            case "damaged_down": frameIndex = 2; totalFrames = 3; frameDelay = 50; lastDirection = "down"; break;
            case "idle_up": frameIndex = 0; totalFrames = 2; frameDelay = 50; lastDirection = "up"; break;
            case "jump_up": frameIndex = 0; totalFrames = 7; frameDelay = 15; lastDirection = "up"; break;
            case "damaged_up": frameIndex = 2; totalFrames = 3; frameDelay = 50; lastDirection = "up"; break;
            case "idle_right": frameIndex = 0; totalFrames = 2; frameDelay = 50; lastDirection = "right"; break;
            case "jump_right": frameIndex = 0; totalFrames = 7; frameDelay = 15; lastDirection = "right"; break;
            case "damaged_right": frameIndex = 2; totalFrames = 3; frameDelay = 50; lastDirection = "right"; break;
            case "idle_left": frameIndex = 0; totalFrames = 2; frameDelay = 50; lastDirection = "left"; break;
            case "jump_left": frameIndex = 0; totalFrames = 7; frameDelay = 15; lastDirection = "left"; break;
            case "damaged_left": frameIndex = 2; totalFrames = 3; frameDelay = 50; lastDirection = "left"; break;
            case "death_all": frameIndex = 0; totalFrames = 4; frameDelay = 15; lastDirection = "death"; break;
        }
    }

    private void detectPlayer() {
        double distanceX = Math.abs(this.getLayoutX() - (player.getLayoutX()+ 16 * GameController.getScale()));
        double distanceY = Math.abs(this.getLayoutY() - (player.getLayoutY()+ 8 * GameController.getScale()));

        if (!jumping && canJump && distanceX < 100 && distanceY < 100) { 
            jumpTowardPlayer();
            canJump = false;

            // ✅ Delay before next jump
            new Thread(() -> {
                try {
                    Thread.sleep(JUMP_DELAY);
                    canJump = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void jumpTowardPlayer() {
        jumping = true;
        double playerX = player.getLayoutX() + 16 * GameController.getScale();
        double playerY = player.getLayoutY() + 8 * GameController.getScale();
        double slimeX = this.getLayoutX();
        double slimeY = this.getLayoutY();

        if (Math.abs(playerX - slimeX) > Math.abs(playerY - slimeY)) {
            if (playerX > slimeX) {
//                movingRight = true; movingLeft = movingUp = movingDown = false;
                setAnimation("jump_right");
                targetX = slimeX + 50;
                targetY = slimeY;
            } else {
//                movingLeft = true; movingRight = movingUp = movingDown = false;
                setAnimation("jump_left");
                targetX = slimeX - 50;
                targetY = slimeY;
            }
        } else {
            if (playerY > slimeY) {
//                movingDown = true; movingUp = movingLeft = movingRight = false;
                setAnimation("jump_down");
                targetX = slimeX;
                targetY = slimeY + 50;
            } else {
//                movingUp = true; movingDown = movingLeft = movingRight = false;
                setAnimation("jump_up");
                targetX = slimeX;
                targetY = slimeY - 50;
            }
        }
        
    }

    private void updateJump() {
        if (jumping) {
            double slimeX = this.getLayoutX();
            double slimeY = this.getLayoutY();

            if (isBlockAt(targetX, targetY)) {
                jumping = false;
                setAnimation("idle_" + lastDirection);
                return;
            }

            if (Math.abs(targetX - slimeX) > jumpSpeed) {
                this.setLayoutX(slimeX + (targetX > slimeX ? jumpSpeed : -jumpSpeed));
            }
            if (Math.abs(targetY - slimeY) > jumpSpeed) {
                this.setLayoutY(slimeY + (targetY > slimeY ? jumpSpeed : -jumpSpeed));
            }

            if (Math.abs(targetX - slimeX) <= jumpSpeed && Math.abs(targetY - slimeY) <= jumpSpeed) {
                jumping = false;
                setAnimation("idle_" + lastDirection);
            }
        }
    }

    private boolean isBlockAt(double x, double y) {
        for (Block block : GameController.getGamePane().getBlocks()) {
            if (block.getLayoutX() == x && block.getLayoutY() == y) {
                return true;
            }
        }
        return false;
    }

    private void checkCollisionWithPlayer() {
        if (!jumping) return; // Only hit player when jumping

        double playerX = player.getLayoutX();
        double playerY = player.getLayoutY();
        double playerWidth = player.getWidth();
        double playerHeight = player.getHeight();

        double slimeX = this.getLayoutX();
        double slimeY = this.getLayoutY();
        double slimeWidth = this.getWidth();
        double slimeHeight = this.getHeight();

        boolean isColliding = playerX + playerWidth > slimeX && playerX < slimeX + slimeWidth &&
                              playerY + playerHeight > slimeY && playerY < slimeY + slimeHeight;

        if (isColliding && canDamage) {
            System.out.println("Slime hit the player!");
            GameController.getGamePane().reducePlayerHealth(1);

            canDamage = false;
            new Thread(() -> { 
                try {
                    Thread.sleep(2000);
                    canDamage = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
    

	@Override
	public void staggerAnimation() {
		// TODO Auto-generated method stub
		jumping = false; // Stop jumping
        setAnimation("damaged_" + lastDirection); // Set damaged animation

        new Thread(() -> {
            try {
                Thread.sleep(200); // Wait for 200ms before switching animation
                Platform.runLater(() -> setAnimation("idle_" + lastDirection)); // Switch to idle safely
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
	}

	@Override
    public void playDeathAnimation() {
		// TODO Auto-generated method stub
        jumping = false;
        setAnimation("death_all");
    }

    
    public void draw() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        gc.setImageSmoothing(false);

        // ✅ Set the correct animation row
        int row;
        if (lastDirection.equals("death")) {
            row = 6; // Death animation row
        } else if (lastDirection.equals("up")) {
            row = jumping ? 3 : 2;
        } else if (lastDirection.equals("down")) {
            row = jumping ? 1 : 0;
        } else {
            row = jumping ? 5 : 4;
        }

        double srcX = frameIndex * FRAME_WIDTH;
        double srcY = row * FRAME_HEIGHT;

        // ✅ Flip horizontally when facing left
        if (lastDirection.equals("left")) {
            gc.save(); // Save the current transformation
            gc.translate(WIDTH, 0); // Move to the right edge
            gc.scale(-1, 1); // Flip horizontally
            gc.drawImage(spriteSheet, srcX, srcY, FRAME_WIDTH, FRAME_HEIGHT, 0, 0, WIDTH, HEIGHT);
            gc.restore(); // Restore original transformation
        } else {
            gc.drawImage(spriteSheet, srcX, srcY, FRAME_WIDTH, FRAME_HEIGHT, 0, 0, WIDTH, HEIGHT);
        }
    }


}
