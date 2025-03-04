package entities;

import java.util.ArrayList;
import java.util.Iterator;

import game.GameController;
import game.Item;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ui.ContainerPane;
import world.Block;
import world.Ore;
import world.Pickaxeable;

public class Player extends Canvas {
    private final int WIDTH;
    private final int HEIGHT;
    private final int MAX_HEALTH;
    private final int MAX_STAMINA;
    private double x;
    private double y;
    private int speed;
    private int health;
    private int stamina;
    
    private Image spriteSheet;
    private Image miningSpriteSheet;
    private Image attackingSpriteSheet; 
    private Image deathSpriteSheet; 
    private int frameIndex;
    private final int totalFrames = 8; // 8 frames for walking animation
    private final int frameWidth = 16;
    private final int frameHeight = 32;
    
    // Mining animation properties
    private final int miningTotalFrames = 3; // 3 frames for mining animation
    private final int miningFrameWidth = 48;
    private final int miningFrameHeight = 48;
    private int miningFrameIndex = 0;
    
    // Attacking animation properties
    private final int attackingTotalFrames = 3; // 3 frames for attacking animation
    private final int attackingFrameWidth = 48;
    private final int attackingFrameHeight = 48;
    private int attackingFrameIndex = 0;
    
    private final int deathTotalFrames = 4; // 4 frames for death animation
    private final int deathFrameWidth = 32;
    private final int deathFrameHeight = 32;
    private int deathFrameIndex = 0;
    private int deathFrameDelay = 15;
    private int deathFrameCounter = 0;
    private boolean isDying = false;
    private boolean isDead = false;
    // Direction
    private boolean movingUp, movingDown, movingRight, movingLeft;
    private String lastDirection = "down"; // Tracks the last direction moved
    private boolean isMoving = false; // Tracks if currently moving in any direction
    
    // State
    private boolean canMove = true;
    private boolean isMining = false;
    private boolean isAttacking = false;
    
    private AnimationTimer animationTimer;

    // Animation speed control
    private int frameDelay = 10; // Movement animation speed
    private int frameCounter = 0;
    private int miningFrameDelay = 10; // Mining animation speed
    private int miningFrameCounter = 0;
    private int attackingFrameDelay = 10; // Attacking animation speed (faster than mining)
    private int attackingFrameCounter = 0;
    
    private static Item usingItem;

    public static ArrayList<ArrayList<Item>> Inventory;
    public static ContainerPane[][] containerGrid= new ContainerPane[5][5];

    public Player() {
        WIDTH = frameWidth * GameController.getScale();
        HEIGHT = frameHeight * GameController.getScale();
        MAX_HEALTH = 30;
        MAX_STAMINA = 60;
        setSpeed(GameController.getScale());
        setX(1080 / 2 - WIDTH / 2);
        setY(720 / 2 - HEIGHT / 2 - HEIGHT / 4);
        setHealth(MAX_HEALTH);
        setStamina(MAX_STAMINA);
        
        // Load sprite sheets
        String playerPath = ClassLoader.getSystemResource("boy.png").toString();
        spriteSheet = new Image(playerPath);
        
        String miningPath = ClassLoader.getSystemResource("boy_useaxe.png").toString();
        miningSpriteSheet = new Image(miningPath);
        
        String attackingPath = ClassLoader.getSystemResource("boy-attack.png").toString();
        attackingSpriteSheet = new Image(attackingPath);
        
        String deadPath = ClassLoader.getSystemResource("boy-dead.png").toString();
        deathSpriteSheet = new Image(deadPath); // Load death animation
        
        // Set canvas to the largest of the animations to accommodate all
        this.setWidth(Math.max(Math.max(WIDTH, miningFrameWidth * GameController.getScale()), 
                             attackingFrameWidth * GameController.getScale()));
        this.setHeight(Math.max(Math.max(HEIGHT, miningFrameHeight * GameController.getScale()), 
                              attackingFrameHeight * GameController.getScale()));

        frameIndex = 0;
        movingDown = false;

        Inventory = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            ArrayList<Item> row = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                row.add(null);
            }
            Inventory.add(row);
        }
        for(int i=0;i<5;i++) {
            for(int j=0;j<5;j++) {
                containerGrid[i][j]=null;
            }
        }
        setupAnimationTimer();
        draw(); // Draw initial frame
    }
    
    public static boolean addItem(Item item, ContainerPane[][] containerGrid) {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (Inventory.get(row).get(col) == null) {
                    Inventory.get(row).set(col, item);
                    
                    if (containerGrid != null&&row==0) {
                        containerGrid[row][col].loadItemFromInventory();
                        containerGrid[row][col].drawContainer();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean useItem(Item item, int amount, ContainerPane[][] containerGrid) {
        int count = 0;

        for (int row = 4; row >=0; row--) {
            for (int col =4; col >=0; col--) {
                if (Inventory.get(row).get(col) != null && Inventory.get(row).get(col).equals(item)) {
                    count++;
                }
            }
        }

        if (count < amount) return false;

        for (int row = 4; row >=0; row--) {
            for (int col =4; col >=0; col--) {
                if (Inventory.get(row).get(col) != null && Inventory.get(row).get(col).equals(item) && amount > 0) {
                    Inventory.get(row).set(col, null);
                    amount--;

                    if (containerGrid != null&&row==0) {
                        containerGrid[row][col].loadItemFromInventory();
                        containerGrid[row][col].drawContainer();
                    }
                }
            }
        }
        return true;
    }

    public static ArrayList<ArrayList<Item>> getInventory() {
        return Inventory;
    }
    
    public static Item getUsingItem() {
        return usingItem;
    }

    public static void setUsingItem(Item usingItem) {
        Player.usingItem = usingItem;
    }

    private void setupAnimationTimer() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isAttacking) {
                    attackingFrameCounter++;
                    if (attackingFrameCounter >= attackingFrameDelay) {
                        attackingFrameCounter = 0;
                        attackingFrameIndex = (attackingFrameIndex + 1) % attackingTotalFrames;
                        
                        // If we've completed the animation cycle, stop attacking
                        if (attackingFrameIndex == 0) {
                            isAttacking = false;
                            setCanMove(true);
                        }
                        
                        if (attackingFrameIndex == 2) {
                        	useSword();
                        }
                    }
                } else if (isMining) {
                    miningFrameCounter++;
                    if (miningFrameCounter >= miningFrameDelay) {
                        miningFrameCounter = 0;
                        miningFrameIndex = (miningFrameIndex + 1) % miningTotalFrames;
                        // If we've completed the animation cycle, stop mining
                        if (miningFrameIndex == 0) {
                            isMining = false;
                            setCanMove(true);
                            usePickaxe();
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

        if (isDying) {
            drawDeathAnimation(gc);  // Draw death animation when dying
        } else if (isAttacking) {
            drawAttackingAnimation(gc);  // Draw attacking animation
        } else if (isMining) {
            drawMiningAnimation(gc);  // Draw mining animation
        } else {
            drawMovementAnimation(gc);  // Draw normal movement animation
        }
    }
    
    private void drawMovementAnimation(GraphicsContext gc) {
        int row = 0;
        
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
        
        setCanMove(false);
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
    
    private void drawAttackingAnimation(GraphicsContext gc) {
        int row;
        
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
        
        setCanMove(false);
        double srcX = attackingFrameIndex * attackingFrameWidth;
        double srcY = row * attackingFrameHeight;
        
        // Calculate position to center the attacking sprite on the canvas
        double drawX = (this.getWidth() - attackingFrameWidth * GameController.getScale()) / 2;
        double drawY = (this.getHeight() - attackingFrameHeight * GameController.getScale()) / 2;
        
        // Check if attacking left, then flip the sprite
        if (lastDirection.equals("left")) {
            gc.save(); // Save the current state
            gc.translate(drawX + attackingFrameWidth * GameController.getScale(), drawY); // Position for flipping
            gc.scale(-1, 1); // Flip horizontally
            gc.drawImage(attackingSpriteSheet, srcX, srcY, attackingFrameWidth, attackingFrameHeight, 
                       0, 0+40, attackingFrameWidth * GameController.getScale(), attackingFrameHeight * GameController.getScale());
            gc.restore(); // Restore the original state
        } else {
            // Draw normally for other directions
            gc.drawImage(attackingSpriteSheet, srcX, srcY, attackingFrameWidth, attackingFrameHeight, 
                       drawX, drawY+40, attackingFrameWidth * GameController.getScale(), attackingFrameHeight * GameController.getScale());
        }
    }
    
    public void die(Runnable onDeathComplete) {
        if (isDead) return; // Prevent multiple deaths
        isDead = true; // Mark player as dead

        System.out.println("Playing death animation...");
        isDying = true;
        deathFrameIndex = 0; // Reset animation

        // Disable movement
        setCanMove(false);

        // Start death animation
        AnimationTimer deathAnimation = new AnimationTimer() {
            private int deathFrameCounter = 0;

            @Override
            public void handle(long now) {
                deathFrameCounter++;
                if (deathFrameCounter >= deathFrameDelay) {
                    deathFrameCounter = 0;
                    if (deathFrameIndex < deathTotalFrames - 1) {
                        deathFrameIndex++;
                    } else {
                        stop(); // Stop animation when complete
                        Platform.runLater(() -> {
                            onDeathComplete.run();
                            isDead = false; // Reset for next life
                        });
                    }
                }
                draw(); // Keep updating the frame
            }
        };

        deathAnimation.start();
    }

    
    private void drawDeathAnimation(GraphicsContext gc) {
        double srcX = deathFrameIndex * deathFrameWidth;
        double srcY = 0; // Assuming the death animation is in a single row

        double drawX = (this.getWidth() - deathFrameWidth * GameController.getScale()) / 2;
        double drawY = (this.getHeight() - deathFrameHeight * GameController.getScale()) / 2;

        if (lastDirection.equals("left")) {
            // Flip horizontally
            gc.save();
            gc.translate(drawX + deathFrameWidth * GameController.getScale(), drawY);
            gc.scale(-1, 1);
            gc.drawImage(deathSpriteSheet, srcX, srcY, deathFrameWidth, deathFrameHeight, 
                         0, 0, deathFrameWidth * GameController.getScale(), deathFrameHeight * GameController.getScale());
            gc.restore();
        } else {
            // Normal rendering (no flipping)
            gc.drawImage(deathSpriteSheet, srcX, srcY, deathFrameWidth, deathFrameHeight, 
                         drawX, drawY, deathFrameWidth * GameController.getScale(), deathFrameHeight * GameController.getScale());
        }
    }

    
    public void attack() {
        if (!isAttacking && !isMining && canMove) {
            setCanMove(false);
            isAttacking = true;
            
            // Reset frame index to start animation from beginning
            attackingFrameIndex = 0;
            attackingFrameCounter = 0;
        }
    }
    
    public void mine() {
        if (!isMining && !isAttacking && canMove) {
            setCanMove(false);
            isMining = true;
            
            // Reset frame index to start animation from beginning
            miningFrameIndex = 0;
            miningFrameCounter = 0;
        }
    }
    
    public void useSword() {
    	Iterator<Monster> iterator = GameController.getGamePane().getMonsters().iterator();
    	while (iterator.hasNext()) {
    	    Monster slime = iterator.next();
    	    int damage = 1;
    	    if (slime.getAttack(damage)) {
    	        System.err.println("monster die");
//    	        iterator.remove(); // Safely remove from the list
//    	        GameController.getGamePane().getChildren().remove(slime); // Remove from UI
    	    }
    	}
    }
    
    public void usePickaxe() {
		double playerFootX = getX() + (8 + 24 - 8) * GameController.getScale();
		double playerFootY = getY() + (32) * GameController.getScale();

		double targetMineBlockX = 0;
		double targetMineBlockY = 0;
		switch (getLastDirection()) {
		case "up":
			targetMineBlockX = ((playerFootX) / GameController.getScale() / 16);
			targetMineBlockY = ((playerFootY) / GameController.getScale() / 16) - 1;
			break;
		case "right":
			targetMineBlockX = ((playerFootX) / GameController.getScale() / 16) + 1;
			targetMineBlockY = ((playerFootY) / GameController.getScale() / 16);
			break;
		case "left":
			targetMineBlockX = ((playerFootX) / GameController.getScale() / 16) - 1;
			targetMineBlockY = ((playerFootY) / GameController.getScale() / 16);
			break;
		case "down":
			targetMineBlockX = ((playerFootX) / GameController.getScale() / 16);
			targetMineBlockY = ((playerFootY) / GameController.getScale() / 16) + 1;
		default:

			break;
		}
		if (targetMineBlockX != 0 && targetMineBlockY != 0) {
			Iterator<Block> iterator = GameController.getGamePane().getBlocks().iterator();
			while (iterator.hasNext()) {
				Block block = iterator.next();

				int blockCenX = (int) ((block.getLayoutX() + (8 * GameController.getScale()))
						/ GameController.getScale() / 16);
				int blockCenY = (int) ((block.getLayoutY() + (8 * GameController.getScale()))
						/ GameController.getScale() / 16);

				if (blockCenX == (int) targetMineBlockX && blockCenY == (int) targetMineBlockY) {
					int damage = 1;

					if (block instanceof Pickaxeable) {
						Ore ore = (Ore) block;
						new Thread(() -> {
					        try {
					        	 Platform.runLater(() -> {
					                    ore.setLayoutY(ore.getLayoutY() - 2);
					                });
					                Thread.sleep(50);

					                Platform.runLater(() -> {
					                    ore.setLayoutY(ore.getLayoutY() + 2);
					                });
					                Thread.sleep(50);

					        } catch (InterruptedException e) {
					            e.printStackTrace();
					        }
					    }).start();
						GameController.getGamePane().setPlayerStamina(getStamina() - 1);

						if (ore.isBrokeFromBreak(damage)) {
							FloatingItem dropItem = new FloatingItem(ore.getItemRow(), ore.getItemCol(),(int) targetMineBlockX, (int) targetMineBlockY);
							iterator.remove();
							GameController.getGamePane().getChildren().remove(this);
							if((int) targetMineBlockX == GameController.getGamePane().getLadderX() && (int) targetMineBlockY == GameController.getGamePane().getLadderY()) {
								
								GameController.getGamePane().createLadder((int) targetMineBlockX, (int) targetMineBlockY);
								System.out.println("Ladder created");		
								
								
							}
							GameController.getGamePane().getChildren().add(dropItem);
							GameController.getGamePane().getfloatingItems().add(dropItem);
							GameController.getGamePane().getChildren().remove(block);
							GameController.getGamePane().getMapBlock()[(int) targetMineBlockY][(int) targetMineBlockX] = 0;
							
							GameController.getGamePane().getChildren().add(this);
							return;
							
						}
					}
				}
			}
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
    
    public void setMining(boolean isMining) {
        this.isMining = isMining;
    }
    
    public boolean isAttacking() {
        return isAttacking;
    }
    
    public void setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }
    
    public static Item getitem() {
        return usingItem;
    }

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public boolean isDying() {
		return isDying;
	}

	public void setDying(boolean isDying) {
		this.isDying = isDying;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		if(health <= 0) {
			this.health = 0;
			//dead
		}
		else if(health > MAX_HEALTH) {
			this.health = MAX_HEALTH;
		}
		else {
			this.health = health;
		}
	}
	
	public int getMaxHealth() {
		return MAX_HEALTH;
	}
	
	
	public int getStamina() {
		return stamina;
	}

	public void setStamina(int stamina) {
		if(stamina <= 0) {
			this.stamina = 0;
		}
		else if(stamina > MAX_STAMINA) {
			this.stamina = MAX_STAMINA;
		}
		else {
			this.stamina = stamina;
		}
	}
	
	public int getMaxStamina() {
		return MAX_STAMINA;
	}

    
    
}