package game;


import entities.Slime;
import entities.Zombie;
import entities.Player;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import ui.Bag;
import ui.CloseButtonPane;
import ui.HealthStamBar;
import ui.InventoryButton;
import world.Block;
import world.Ladder;
import world.Map;
import world.Ore;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePane extends Pane {
	private Map gameMap;
	private Player player;
	private double playerCenterAbsX;
	private double playerCenterAbsY;
	private Pane mother;
	private InventoryButton inv;
	private Bag bag;
	private CloseButtonPane closeButton;
	private static boolean pass = true;
	private int[][] mapBlock;
//	private Block[][] blockArray;
	private List<Block> blocks = new ArrayList<>(); // Store all rocks
	private Rectangle transitionScreen;
//	private static final int ROCK_COUNT = 10; // Number of randomly placed rocks
	private int SLIME_COUNT = 3, ZOMBIE_COUNT = 3;
	private List<Slime> slimes;
	private List<Zombie> zombies;

	public GamePane(Pane mother) {
		this.mother = mother;
		this.gameMap = new Map();
		this.player = new Player();
		this.slimes = new ArrayList<>();

		this.blocks = new ArrayList<>();
		this.zombies = new ArrayList<>();
		this.getChildren().add(this.gameMap);

		this.playerCenterAbsX = this.player.getX() + this.getLayoutX() - 16 * GameController.getScale();
		this.playerCenterAbsY = this.player.getY() + this.getLayoutY() - 8 * GameController.getScale();
		transitionScreen = new Rectangle();
		transitionScreen.setFill(Color.BLACK);
		transitionScreen.setOpacity(0);
		transitionScreen.setViewOrder(-1000);
		this.getChildren().add(transitionScreen);

		genererateNextMap();

		// Generate random rocks
//		generateRandomBlocks();
		// Generate random ladder
//		generateRandomLadder();

//		generateRandomSlimes();
//		generateRandomZombies();
		// Set up mouse click event handler

		this.getChildren().add(this.player);
		setupMouseHandler();
		startMovement();
	}

	private void genererateNextMap() {
		int arraySizeH = (int) (this.gameMap.getHeight() / GameController.getScale()) / 16;
		int arraySizeW = (int) (this.gameMap.getWidth() / GameController.getScale()) / 16;
		this.mapBlock = new int[arraySizeH][arraySizeW];
//		this.blockArray = new Block[arraySizeH][arraySizeW];

		Random rand = new Random();

		// Random player start position
		int playerSpawnX = rand.nextInt((mapBlock[0].length - 3 - 1) - 3 + 1) + 3;
		int playerSpawnY = 4;

		this.player.setX((playerSpawnX - 1) * 16 * GameController.getScale());
		this.player.setY(2 * 16 * GameController.getScale());
		this.player.setLayoutX(this.player.getX());
		this.player.setLayoutY(this.player.getY());
		this.mapBlock[playerSpawnY][playerSpawnX] = -2;

		for (int i = 0; i < mapBlock.length; i++) {
			for (int j = 0; j < mapBlock[0].length; j++) {
				if (i > 3 && i < mapBlock.length - 3 && j < mapBlock[0].length - 3 && j > 2) {
//					this.blockArray[i][j] = null;
					if (i == playerSpawnY && j == playerSpawnX) {
						continue;
					}
					int randomValue = rand.nextInt(100);

					int p1 = 90;
					int p2 = 4;
					int p3 = 3;
					int p4 = 2;
					int p5 = 1;

					if (randomValue < p1) {
						this.mapBlock[i][j] = 0;
						continue;
					} else if (randomValue < p1 + p2) {
						this.mapBlock[i][j] = 1;
					} else if (randomValue < p1 + p2 + p3) {
						this.mapBlock[i][j] = 1;
					} else if (randomValue < p1 + p2 + p3 + p4) {
						this.mapBlock[i][j] = 2;

					} else {
						this.mapBlock[i][j] = 3;
					}
					Ore block = new Ore(this.mapBlock[i][j]);
					block.setLayoutX(16 * GameController.getScale() * j);
					block.setLayoutY(16 * GameController.getScale() * i);
					this.getChildren().add(block);
//					this.blockArray[i][j] = block;
					blocks.add(block);
				} else {
					this.mapBlock[i][j] = -1;
				}
			}
		}
		for (int i = 0; i < this.getChildren().size(); i++) {
			System.out.println("Child " + i + ": " + this.getChildren().get(i));
		}

		for (int i = 0; i < mapBlock.length; i++) {
			String tmp = "";
			for (int j = 0; j < mapBlock[0].length; j++) {
				tmp += this.mapBlock[i][j] + " ";
				if (this.mapBlock[i][j] == 1) {
				}
			}
			System.out.println(tmp);
		}

//		for (int i = 0; i < blockArray.length; i++) {
//			String tmp = "";
//			for (int j = 0; j < blockArray[0].length; j++) {
//				tmp += this.blockArray[i][j] + " ";
//			}
//			System.out.println(tmp);
//		}
	}

	private void setupMouseHandler() {
		// Add event handler for mouse clicks
		this.setOnMouseClicked(this::handleMouseClick);
	}

	private void handleMouseClick(MouseEvent event) {
		if (checkLadderClick(event)) {
			return;
		}

		if (Player.getUsingItem() == null) {
			System.out.println("Do nothing");
			return;
		}

		// Only execute mining if ladder wasn’t clicked
		if (Player.getUsingItem().getRow() == 2 && Player.getUsingItem().getCol() == 0 && !player.isMining()) {
			System.out.println("player is Mining");
			player.setMining(true);
			player.mine();
		}

		else if (Player.getUsingItem().getRow() == 3 && Player.getUsingItem().getCol() == 4) {
			System.out.println("player is Attacking");
			player.setAttacking(true);
			player.attack();
		}
	}

//	private boolean isValidBlockLocation(double gridX, double gridY) {
//		// Use grid coordinates so that blocks never share the same cell.
//		// Check if any block is already at that grid coordinate.
//		double tileSize = 16 * GameController.getScale();
//		double candidateX = gridX * tileSize;
//		double candidateY = gridY * tileSize;
//		for (Block block : blocks) {
//			// Compare positions (using a tolerance of 0 since blocks are placed on a grid)
//			if (Math.abs(block.getLayoutX() - candidateX) < 0.1 && Math.abs(block.getLayoutY() - candidateY) < 0.1) {
//				return false;
//			}
//		}
//		return true;
//	}

//	private void generateRandomBlocks() {
//		Random random = new Random();
//		int ORE_TYPE_COUNT = 5; // Number of ore types
//		double scale = GameController.getScale();
//		double tileSize = 16 * scale;
//
//		// Calculate player's spawn grid coordinates (assuming player's spawn is where
//		// the player is initially)
//		int playerSpawnGridX = (int) (player.getX() / tileSize);
//		int playerSpawnGridY = (int) (player.getY() / tileSize);
//
//		for (int i = 0; i < ROCK_COUNT; i++) {
//			int gridX, gridY;
//			int attempts = 0;
//			do {
//				gridX = random.nextInt(5, 25);
//				gridY = random.nextInt(5, 10);
//				attempts++;
//				// Continue if:
//				// 1. The grid is exactly the player's spawn cell.
//				// 2. There is already a block at that grid.
//			} while (((gridX == playerSpawnGridX && gridY == playerSpawnGridY) || !isValidBlockLocation(gridX, gridY))
//					&& attempts < 100);
//			// If after many attempts no valid cell is found, we simply skip.
//			if (attempts >= 100)
//				continue;
//
//			Block block;
//			if (random.nextBoolean()) {
//				block = new Ore(0);
//			} else {
//				int oreType = random.nextInt(ORE_TYPE_COUNT);
//				block = new Ore(oreType + 1);
//			}
//			double blockX = tileSize * gridX;
//			double blockY = tileSize * gridY;
//			block.setLayoutX(blockX);
//			block.setLayoutY(blockY);
//			this.getChildren().add(block);
//			blocks.add(block);
//		}
//	}

	private boolean isValidSpawnLocation(double x, double y) {
		double scale = GameController.getScale();
		double safeDistance = 20 * scale; // minimum distance from player's spawn
		// Assume player's spawn is at player's initial coordinates:
		double playerSpawnX = player.getX() + 16 * GameController.getScale();
		double playerSpawnY = player.getY() + 16 * GameController.getScale();
		// Check that the candidate spawn is at least safeDistance away from player
		// spawn.
		double dx = x - playerSpawnX;
		double dy = y - playerSpawnY;
		if (Math.sqrt(dx * dx + dy * dy) < safeDistance) {
			return false;
		}

		// Check if the candidate location is too close to any block.
		double candidateSize = 16 * scale; // using tile size for blocks
		for (Block block : blocks) {
			// Here we check if the candidate location overlaps with the block.
			// We can consider a simple rectangular overlap check.
			double blockX = block.getLayoutX();
			double blockY = block.getLayoutY();
			if (x < blockX + candidateSize && x + candidateSize > blockX && y < blockY + candidateSize
					&& y + candidateSize > blockY) {
				return false;
			}
		}

		// Also ensure that it does not overlap the player.
		if (Math.abs(x - player.getLayoutX()) < 32 && Math.abs(y - player.getLayoutY()) < 32) {
			return false;
		}

		// And check monsters do not overlap with each other.
		for (Slime slime : slimes) {
			if (Math.abs(x - slime.getLayoutX()) < 16 && Math.abs(y - slime.getLayoutY()) < 16) {
				return false;
			}
		}
		for (Zombie zombie : zombies) {
			if (Math.abs(x - zombie.getLayoutX()) < 64 && Math.abs(y - zombie.getLayoutY()) < 32) {
				return false;
			}
		}

		return true;
	}

	private void generateRandomSlimes() {
		Random random = new Random();
		int attempts;
		for (int i = 0; i < SLIME_COUNT; i++) {
			double slimeX = 0, slimeY = 0;
			boolean isValidSpawn = false;
			attempts = 0;
			while (!isValidSpawn && attempts < 50) {
				int gridX = random.nextInt(5, 25);
				int gridY = random.nextInt(5, 10);
				slimeX = 16 * GameController.getScale() * gridX;
				slimeY = 16 * GameController.getScale() * gridY;
				isValidSpawn = isValidSpawnLocation(slimeX, slimeY);
				attempts++;
			}
			if (isValidSpawn) {
				Slime slime = new Slime(slimeX, slimeY, 10, 2, 1, player);
				slimes.add(slime);
				this.getChildren().add(slime);
			}
		}
	}

	private void generateRandomZombies() {
		Random random = new Random();
		int attempts;
		for (int i = 0; i < ZOMBIE_COUNT; i++) {
			double zombieX = 0, zombieY = 0;
			boolean isValidSpawn = false;
			attempts = 0;
			while (!isValidSpawn && attempts < 50) {
				int gridX = random.nextInt(5, 25);
				int gridY = random.nextInt(5, 10);
				zombieX = 16 * GameController.getScale() * gridX;
				zombieY = 16 * GameController.getScale() * gridY;
				isValidSpawn = isValidSpawnLocation(zombieX, zombieY);
				attempts++;
			}
			if (isValidSpawn) {
				Zombie zombie = new Zombie(zombieX, zombieY, 20, 5, 1, player);
				zombies.add(zombie);
				this.getChildren().add(zombie);
			}
		}
	}

	private void generateRandomLadder() {
		Random random = new Random();
		double ladderX, ladderY;
		boolean isColliding;

		// Keep generating positions until we find an empty spot
		do {
			int gridX = random.nextInt(5, 25);
			int gridY = random.nextInt(5, 10);
			ladderX = 16 * GameController.getScale() * gridX;
			ladderY = 16 * GameController.getScale() * gridY;

			// Check if the ladder would collide with another block or the player
			isColliding = isBlockAt(ladderX, ladderY);
		} while (isColliding); // Repeat until an empty, non-colliding position is found

		// Create and place the ladder
		Ladder ladder = new Ladder();
		ladder.setLayoutX(ladderX);
		ladder.setLayoutY(ladderY);

		// Add ladder to the scene and store it in blocks (since Ladder extends Block)
		this.getChildren().add(ladder);
		blocks.add(ladder);
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

		boolean movingDown = false;
		boolean movingUp = false;
		boolean movingRight = false;
		boolean movingLeft = false;

		if (GameController.getKeyboardController().isBag() && pass) {
			Thread thread = new Thread(() -> {
				Platform.runLater(() -> {
					if (bag == null) {
						bag = new Bag();
					}
					inv = new InventoryButton();
					inv.setLayoutX(265);
					inv.setLayoutY(60);
					bag.setLayoutX(265);
					bag.setLayoutY(115);
					closeButton = new CloseButtonPane(mother, bag, inv);
					closeButton.setLayoutX(760);
					closeButton.setLayoutY(60);
					mother.getChildren().addAll(inv, bag, closeButton);
					pass = false;
				});
			});
			thread.start();
		} else if (!GameController.getKeyboardController().isBag() && !pass) {
			Platform.runLater(() -> {
				mother.getChildren().removeAll(inv, bag, closeButton);
				MainPane.setFloorText("1");
				bag = null;
				inv = null;
				closeButton = null;
				pass = true;
			});
		}

		if (player.canMove()) {
			if (GameController.getKeyboardController().isMoveUp()) {
				if (this.player.getY() >= 2 * 16 * GameController.getScale()
						&& !isColliding(0, -this.player.getSpeed())) {
					dy -= this.player.getSpeed();
					movingUp = true;
				}
			}
			if (GameController.getKeyboardController().isMoveDown()) {
				if (this.player.getY() <= this.gameMap.getHeight() - (5 * 16 + 8) * GameController.getScale()
						&& !isColliding(0, this.player.getSpeed())) {
					dy += this.player.getSpeed();
					movingDown = true;
				}
			}
			if (GameController.getKeyboardController().isMoveLeft()) {
				if (this.player.getX() >= 2 * 16 * GameController.getScale()
						&& !isColliding(-this.player.getSpeed(), 0)) {
					dx -= this.player.getSpeed();
					movingLeft = true;
				}
			}
			if (GameController.getKeyboardController().isMoveRight()) {
				if (this.player.getX() <= this.gameMap.getWidth() - 5 * 16 * GameController.getScale()
						&& !isColliding(this.player.getSpeed(), 0)) {
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
		newLayoutX = Math.max(-(this.gameMap.getWidth() - (14 * 16 * GameController.getScale())),
				Math.min(0, newLayoutX));
		newLayoutY = Math.max(-(this.gameMap.getHeight() - ((10 * 16 - 8) * GameController.getScale())),
				Math.min(0, newLayoutY));
		// System.out.println(newLayoutX);
		this.setLayoutX(newLayoutX);
		this.setLayoutY(newLayoutY);

		// Move Player
		this.player.setX(this.player.getX() + dx);
		this.player.setY(this.player.getY() + dy);
		this.player.setLayoutX(this.player.getX());
		this.player.setLayoutY(this.player.getY());
		adjustViewOrder();
	}

	private void adjustViewOrder() {
		for (Zombie zombie : zombies) {
			if (player.getY() + 1 * GameController.getScale() >= zombie.getY()) {
				player.setViewOrder(-501); // player in front
				zombie.setViewOrder(-500); // zombie behind player
			} else {
				player.setViewOrder(-500); // player behind zombie
				zombie.setViewOrder(-501); // zombie in front
			}
		}

		for (Slime slime : slimes) {
			if (player.getY() >= slime.getY()) {
				player.setViewOrder(-501);
				slime.setViewOrder(-500);
			} else {
				player.setViewOrder(-500);
				slime.setViewOrder(-501);
			}
		}
	}

	// Collision Detection for Random Rocks
	private boolean isColliding(double dx, double dy) {
		double nextX = this.player.getX() + dx + 16 * GameController.getScale();
		double nextY = this.player.getY() + dy + 16 * GameController.getScale();

		double hitboxWidth = 16 * GameController.getScale();
		double hitboxHeight = 16 * GameController.getScale();

		for (Block rock : blocks) {
			double rockX = rock.getLayoutX();
			double rockY = rock.getLayoutY();
			double rockSize = 16 * GameController.getScale(); // Adjust based on rock size

			// Smaller collision box for smoother movement
			double collisionPadding = 4 * GameController.getScale();
			double rockLeft = rockX + collisionPadding;
			double rockRight = rockX + rockSize - collisionPadding;
			double rockTop = rockY - collisionPadding;
			double rockBottom = rockY;

			// Check if player's next position overlaps with rock
			if (nextX + hitboxWidth > rockLeft && nextX < rockRight && nextY + hitboxHeight > rockTop
					&& nextY < rockBottom) {
				return true; // Collision detected
			}

		}
		return false; // No collision
	}

	private boolean isBlockAt(double x, double y) {
		for (Block block : blocks) {
			if (block.getLayoutX() == x && block.getLayoutY() == y) {
				return true;
			}
		}
		return false;
	}

	private boolean checkLadderClick(MouseEvent event) {
		double scale = GameController.getScale();
		double range = 32 * scale; // Interaction range

		// Get player's center coordinates
		double playerCenterX = player.getX() + player.getWidth() / 2;
		double playerCenterY = player.getY() + player.getHeight() / 2;

		for (Block block : blocks) {
			if (block instanceof Ladder) {
				// Calculate ladder's center assuming the ladder's area is range by range
				double ladderCenterX = block.getLayoutX() + range / 2;
				double ladderCenterY = block.getLayoutY() + range / 2;

				// Calculate distance between player and ladder
				double dx = playerCenterX - ladderCenterX;
				double dy = playerCenterY - ladderCenterY;
				double distance = Math.sqrt(dx * dx + dy * dy);

				// If the player is within range, trigger the ladder action
				if (distance <= range) {
					// Disable mining and reset any mining attack flag
					player.setMining(false);
					player.setCanMove(false);
					GameController.getKeyboardController().setAttacking(false);
					enterNextFloor();
					MainPane.setFloorText((MainPane.getFloorNum() + 1) + "");
					MainPane.setFloorNum(MainPane.getFloorNum() + 1);
					return true;
				}
			}
		}
		return false;
	}

	private void enterNextFloor() {
		System.out.println("Entering next floor...");
		updateOverlaySize();

		FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), transitionScreen);
		fadeIn.setFromValue(0);
		fadeIn.setToValue(1);

		PauseTransition pause = new PauseTransition(Duration.seconds(0.5));

		Transition resetFloor = new Transition() {
			{
				setCycleDuration(Duration.seconds(0.1));
			}

			@Override
			protected void interpolate(double frac) {
				if (frac == 1.0) {
					Platform.runLater(() -> {
						getChildren().removeAll(blocks);
						blocks.clear();
//						generateRandomBlocks();
						generateRandomLadder();
						double startX = 1080 / 2 - player.getWidth() / 2 - 16 * GameController.getScale();
						double startY = 720 / 2 - player.getHeight() / 2 - player.getHeight() / 4
								- 8 * GameController.getScale();
						player.setX(startX);
						player.setY(startY);
						player.setLayoutX(startX);
						player.setLayoutY(startY);

						if (!getChildren().contains(player)) {
							getChildren().add(player);
						}
					});
				}
			}
		};

		FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), transitionScreen);
		fadeOut.setFromValue(1);
		fadeOut.setToValue(0);
		fadeOut.setOnFinished(event -> player.setCanMove(true));

		SequentialTransition transition = new SequentialTransition(fadeIn, pause, resetFloor, fadeOut);
		transition.play();
	}

	private void updateOverlaySize() {
		transitionScreen.setWidth(this.getWidth());
		transitionScreen.setHeight(this.getHeight());
		transitionScreen.toFront(); // Ensure it's on top
	}

	public void reducePlayerHealth(int damage) {
		// Prevent taking damage if already dead
		if (player.isDead())
			return;

		MainPane mainPane = (MainPane) mother;
		if (mainPane != null) {
			HealthStamBar healthBar = mainPane.gethBar();
			int currentHealth = healthBar.getBarAmount();

			int newHealth = Math.max(0, currentHealth - damage);
			healthBar.setBarAmount(newHealth);

			if (newHealth == 0 && !player.isDead()) {
				System.out.println("Player is dead!");
				player.die(() -> resetGame()); // Only called once
			}
		}
	}

	private void resetGame() {
		System.out.println("Resetting game...");
		MainPane mainPane = (MainPane) mother;

		// ✅ Ensure transitionScreen covers the entire game area
		updateOverlaySize();
		transitionScreen.toFront(); // Ensure fade effect is on top

		// Fade out to black
		FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), transitionScreen);
		fadeOut.setFromValue(0);
		fadeOut.setToValue(1);

		// Pause before resetting
		PauseTransition pause = new PauseTransition(Duration.seconds(1));

		// Reset logic
		Transition resetLogic = new Transition() {
			{
				setCycleDuration(Duration.seconds(0.1));
			}

			@Override
			protected void interpolate(double frac) {
				if (frac == 1.0) {
					Platform.runLater(() -> {
						// Remove all game objects
						getChildren().removeAll(blocks);
						getChildren().removeAll(slimes);
						getChildren().removeAll(zombies);

						blocks.clear();
						slimes.clear();
						zombies.clear();

						// Regenerate world
//						generateRandomBlocks();
						generateRandomLadder();
						generateRandomSlimes();
						generateRandomZombies();

						// Reset player stats
						player.setX(1080 / 2 - player.getWidth() / 2 - 16 * GameController.getScale());
						player.setY(720 / 2 - player.getHeight() / 2 - player.getHeight() / 4
								- 8 * GameController.getScale());
						player.setLayoutX(player.getX());
						player.setLayoutY(player.getY());
						player.setAttacking(false);
						player.setMining(false);
						player.setCanMove(true);

						// Reset player's health
						MainPane mainPane = (MainPane) mother;
						if (mainPane != null) {
							mainPane.gethBar().setBarAmount(30); // Reset health to full
						}

						// Ensure the player is added back
						if (!getChildren().contains(player)) {
							getChildren().add(player);
						}
					});
				}
			}
		};

		// Fade in after resetting
		FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), transitionScreen);
		fadeIn.setFromValue(1);
		fadeIn.setToValue(0);
		fadeIn.setOnFinished(event -> {
			player.setCanMove(true);
			player.setDead(false);
			player.setDying(false);
		});

		// Play reset sequence
		SequentialTransition resetSequence = new SequentialTransition(fadeOut, pause, resetLogic, fadeIn);
		resetSequence.play();

	}

	public static boolean isPass() {
		return pass;
	}

	public static void setPass(boolean pass) {
		GamePane.pass = pass;
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}

	public List<Zombie> getZombies() {
		return zombies;
	}

}