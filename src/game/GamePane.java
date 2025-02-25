package game;

import entities.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import world.Ladder;
import world.Map;
import world.Rock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePane extends Pane {
    private Map gameMap;
    private Player player;
    private double playerCenterAbsX;
    private double playerCenterAbsY;
    private List<Rock> rocks = new ArrayList<>(); // Store all rocks

    private static final int ROCK_COUNT = 20; // Number of randomly placed rocks

    public GamePane() {
        this.gameMap = new Map();
        this.player = new Player();
        this.getChildren().add(this.gameMap);
        this.getChildren().add(this.player);

        this.playerCenterAbsX = this.player.getX() + this.getLayoutX();
        this.playerCenterAbsY = this.player.getY() + this.getLayoutY();

        // Generate random rocks
        generateRandomRocks();
        // Generate random ladder
        generateRandomLadder();
        
        // Set up mouse click event handler
        setupMouseHandler();
        
        startMovement();
    }

    private void setupMouseHandler() {
        // Add event handler for mouse clicks
        this.setOnMouseClicked(this::handleMouseClick);
    }
    
    private void handleMouseClick(MouseEvent event) {
        // If player is not mining and can move, start mining on mouse click
        if (!player.isMining() && player.canMove()) {
            player.mine();
        }
    }

    private void generateRandomRocks() {
        Random random = new Random();
        for (int i = 0; i < ROCK_COUNT; i++) {
            int gridX = random.nextInt(5,25); // Adjust based on map size
            int gridY = random.nextInt(5,10);
            
            Rock rock = new Rock();
            double rockX = 16 * GameController.getScale() * gridX;
            double rockY = 16 * GameController.getScale() * gridY;
            rock.setLayoutX(rockX);
            rock.setLayoutY(rockY);

            this.getChildren().add(rock);
            rocks.add(rock);
        }
    }
    
    private void generateRandomLadder() {
        Random random = new Random();
        int gridX = random.nextInt(5,25); // Adjust based on map size
        int gridY = random.nextInt(5,10);

        Ladder ladder = new Ladder();
        double ladderX = 16 * GameController.getScale() * gridX;
        double ladderY = 16 * GameController.getScale() * gridY;
        ladder.setLayoutX(ladderX);
        ladder.setLayoutY(ladderY);

        this.getChildren().add(ladder);
    }

    private void startMovement() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                move();
                checkMining();
            }
        };
        timer.start();
    }

    private void move() {
        double dx = 0, dy = 0;

        boolean movingDown = false;
        boolean movingUp = false;
        boolean movingRight = false;
        boolean movingLeft = false;

        if(player.canMove()) {    
            if (GameController.getKeyboardController().isMoveUp()) {
                if (this.player.getY() >= 180 && !isColliding(0, -this.player.getSpeed())) {
                    dy -= this.player.getSpeed();
                    movingUp = true;
                }
            }
            if (GameController.getKeyboardController().isMoveDown()) {
                if (this.player.getY() <= 900 && !isColliding(0, this.player.getSpeed())) {
                    dy += this.player.getSpeed();
                    movingDown = true; // Start walking down animation
                }
            }
            if (GameController.getKeyboardController().isMoveLeft()) {
                if (this.player.getX() >= 3 * 16 * GameController.getScale() && !isColliding(-this.player.getSpeed(), 0)) {
                    dx -= this.player.getSpeed();
                    movingLeft = true;
                }
            }
            if (GameController.getKeyboardController().isMoveRight()) {
                if (this.player.getX() <= 2258 && !isColliding(this.player.getSpeed(), 0)) {
                    dx += this.player.getSpeed();
                    movingRight = true;
                }
            }
        }

        player.setMovingDown(movingDown);
        player.setMovingUp(movingUp);
        player.setMovingRight(movingRight);
        player.setMovingLeft(movingLeft);
        
        // Normalize diagonal movement
        if (dx != 0 || dy != 0) {
            double length = Math.sqrt(dx * dx + dy * dy);
            dx = (dx / length) * this.player.getSpeed();
            dy = (dy / length) * this.player.getSpeed();
        }

        // Update camera position
        double newLayoutX = playerCenterAbsX - this.player.getX();
        double newLayoutY = playerCenterAbsY - this.player.getY();
        newLayoutX = Math.max(-1440, Math.min(0, newLayoutX));
        newLayoutY = Math.max(-540, Math.min(0, newLayoutY));

        this.setLayoutX(newLayoutX);
        this.setLayoutY(newLayoutY);

        // Move Player
        this.player.setX(this.player.getX() + dx);
        this.player.setY(this.player.getY() + dy);
        this.player.setLayoutX(this.player.getX());
        this.player.setLayoutY(this.player.getY());
    }
    
    private void checkMining() {
        if (GameController.getKeyboardController().isAttacking() && !player.isMining()) {
            player.mine();
            // Reset the attack flag after starting the mining
            GameController.getKeyboardController().setAttacking(false);
        }
    }

    // Collision Detection for Random Rocks
    private boolean isColliding(double dx, double dy) {
        double nextX = this.player.getX() + dx + 16 * GameController.getScale();
        double nextY = this.player.getY() + dy + 16 * GameController.getScale();
        
        double hitboxWidth = 16 * GameController.getScale();
        double hitboxHeight = 16 * GameController.getScale();
        
//        double playerWidth = this.player.getWidth();
//        double playerHeight = this.player.getHeight();

        for (Rock rock : rocks) {
            double rockX = rock.getLayoutX();
            double rockY = rock.getLayoutY();
            double rockSize = 16 * GameController.getScale(); // Adjust based on rock size

            // Smaller collision box for smoother movement
            double collisionPadding = 4 * GameController.getScale();
            double rockLeft = rockX + collisionPadding;
            double rockRight = rockX + rockSize - collisionPadding;
            double rockTop = rockY - collisionPadding ;
            double rockBottom = rockY;

            // Check if player's next position overlaps with rock
            if (nextX + hitboxWidth > rockLeft && nextX < rockRight &&
                nextY + hitboxHeight > rockTop && nextY < rockBottom) {
                return true; // Collision detected
            }
           
        }
        return false; // No collision
    }
}