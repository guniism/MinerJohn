package game;

import entities.Slime;
import entities.Zombie;
import entities.FloatingItem;
import entities.Monster;
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
import ui.InventoryButton;
import world.Block;
import world.Interactable;
import world.Ladder;
import world.LadderUp;
import world.Map;
import world.Ore;
import world.Pickaxeable;

import java.util.ArrayList;
import java.util.Iterator;
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
	private int[][] mapBlock, mapMonster;
	private List<Block> blocks = new ArrayList<>(); // Store all rocks
	private List<FloatingItem> floatingItems = new ArrayList<>();
	private Rectangle transitionScreen;
//	private static final int ROCK_COUNT = 10; // Number of randomly placed rocks
	private int SLIME_COUNT = 2, ZOMBIE_COUNT = 0;
	private List<Monster> monsters;
	private int ladderX;
	private int ladderY;

	public GamePane(Pane mother) {
		this.mother = mother;
		this.gameMap = new Map();
		this.player = new Player();
		this.blocks = new ArrayList<>();
		this.getChildren().add(this.gameMap);

		this.playerCenterAbsX = this.player.getX() + this.getLayoutX() - 16 * GameController.getScale();
		this.playerCenterAbsY = this.player.getY() + this.getLayoutY() - 8 * GameController.getScale();

		transitionScreen = new Rectangle();
		transitionScreen.setFill(Color.BLACK);
		transitionScreen.setOpacity(0);
		transitionScreen.setViewOrder(-1000);
		transitionScreen.setVisible(false);
		this.getChildren().add(transitionScreen);

		generateNextMap();

		setupMouseHandler();
		startTimer();
	}

	private void generateNextMap() {
		Random rand = new Random();

//		System.out.println(MainPane.getFloorNum());
//		if(MainPane.getFloorNum() >= 1) {
//			this.gameMap.setMap(3);
//		}
//		else if(MainPane.getFloorNum() >= 10) {
//			this.gameMap.setMap(2);
//		}
//		else {
//			this.gameMap.setMap(1);
//		}
		this.gameMap.setMap(rand.nextInt(3) + 1);

		int arraySizeH = (int) (this.gameMap.getHeight() / GameController.getScale()) / 16;
		int arraySizeW = (int) (this.gameMap.getWidth() / GameController.getScale()) / 16;
		this.mapBlock = new int[arraySizeH][arraySizeW];

		// Random player start position
		int playerSpawnX = rand.nextInt((mapBlock[0].length - 3 - 1) - 3 + 1) + 3;
		int playerSpawnY = 4;

		this.player.setX((playerSpawnX - 1) * 16 * GameController.getScale());
		this.player.setY(2 * 16 * GameController.getScale());
		this.player.setLayoutX(this.player.getX());
		this.player.setLayoutY(this.player.getY());
		this.mapBlock[playerSpawnY][playerSpawnX] = -2;

		LadderUp ladderUp = new LadderUp();
		ladderUp.setLayoutX(this.player.getX() + 16 * GameController.getScale());
		ladderUp.setLayoutY(this.player.getY());
		this.getChildren().add(ladderUp);
		blocks.add(ladderUp);

		for (int i = 0; i < mapBlock.length; i++) {
			for (int j = 0; j < mapBlock[0].length; j++) {
				if (i > 3 && i < mapBlock.length - 3 && j < mapBlock[0].length - 3 && j > 2) {
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
						this.mapBlock[i][j] = 2;
					} else if (randomValue < p1 + p2 + p3 + p4) {
						this.mapBlock[i][j] = 3;

					} else {
						this.mapBlock[i][j] = 4;
					}

					Ore block = new Ore(this.mapBlock[i][j]);
					if (this.mapBlock[i][j] == 1) {
						block = new Ore(rand.nextInt(2));
					}
					block.setLayoutX(16 * GameController.getScale() * j);
					block.setLayoutY(16 * GameController.getScale() * i);
					this.getChildren().add(block);
					blocks.add(block);

				} else {
					this.mapBlock[i][j] = -1;
				}
			}
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

		int randomLadder = rand.nextInt(blocks.size());
		while (!(blocks.get(randomLadder) instanceof Pickaxeable)) {
			randomLadder = rand.nextInt(blocks.size());
		}
		int TargetLadderX = (int) (blocks.get(randomLadder).getLayoutX() / GameController.getScale() / 16);
		int TargetLadderY = (int) (blocks.get(randomLadder).getLayoutY() / GameController.getScale() / 16);
		System.out.println(TargetLadderX + " " + TargetLadderY);
		this.ladderX = TargetLadderX;
		this.ladderY = TargetLadderY;

		this.getChildren().add(this.player);
		mapMonster = mapBlock;

		monsters = new ArrayList<Monster>();
		generateRandomSlimes();
		generateRandomZombies();
//	    Slime slime = new Slime(5 * 16 * GameController.getScale(), 5 * 16 * GameController.getScale(), 2, 1, 1, player);
//	    monsters.add(slime);
//	    this.getChildren().add(slime);
	}

	public void createLadder(int gridX, int gridY) {
		Ladder ladder = new Ladder();
		ladder.setLayoutX(16 * GameController.getScale() * gridX);
		ladder.setLayoutY(16 * GameController.getScale() * gridY);

		this.getChildren().add(ladder);
		blocks.add(ladder);
	}

	private void setupMouseHandler() {
		// Add event handler for mouse clicks
		this.setOnMouseClicked(this::handleMouseClick);
	}

	private void handleMouseClick(MouseEvent event) {

		if (interactBlockClick(event)) {
			return;
		}

		if (Player.getUsingItem() == null) {
			System.out.println("Do nothing");
			return;
		}

		
		if (Player.getUsingItem().getRow() == 1 && Player.getUsingItem().getCol() == 4) {
			System.out.println("player is Attacking");
			player.setAttacking(true);
			player.attack();
		}
		
		if (player.getStamina() > 0) {
			// Only execute mining if ladder wasn’t clicked
			if (Player.getUsingItem().getRow() == 0 && Player.getUsingItem().getCol() == 0 && !player.isMining()) {
				System.out.println("player is Mining");
				player.setMining(true);
				player.mine();
				
			}
		} else {
			System.out.println("Player has no stamina!");
		}

	
	}

	private boolean interactBlockClick(MouseEvent event) {
		for (Block block : this.blocks) {
			if (block instanceof Interactable interactBlock) {
				int mouseX = (int) (event.getX() / GameController.getScale() / 16);
				int mouseY = (int) (event.getY() / GameController.getScale() / 16);
				int x = (int) (block.getLayoutX() / GameController.getScale() / 16);
				int y = (int) (block.getLayoutY() / GameController.getScale() / 16);

//				System.out.println("X=" + mouseX + ", Y=" + mouseY);
//				System.out.println("X=" + x + ", Y=" + y);

				if (mouseX == x && mouseY == y) {
					interactBlock.response();
					return true;
				}
			}
		}
		return false;
	}

	private void generateRandomSlimes() {
		Random random = new Random();
		for (int i = 0; i < SLIME_COUNT; i++) {
			int slimeY, slimeX;

			do {
				slimeY = random.nextInt(5, mapMonster.length - 4);
				slimeX = random.nextInt(5, mapMonster[0].length - 4);
			} while (!canSpawnAt(slimeX, slimeY, mapMonster));
			mapMonster[slimeY][slimeX] = -3;
			slimeY *= GameController.getScale() * 16;
			slimeX *= GameController.getScale() * 16;

			Slime slime = new Slime(slimeX, slimeY, 2, 1, 1, player);

			monsters.add(slime);
			this.getChildren().add(slime);
		}
	}

	private void generateRandomZombies() {
		Random random = new Random();
		for (int i = 0; i < ZOMBIE_COUNT; i++) {
			int zombieY, zombieX;

			do {
				zombieY = random.nextInt(5, mapMonster.length - 4);
				zombieX = random.nextInt(5, mapMonster[0].length - 4);
			} while (!canSpawnAt(zombieX, zombieY, mapMonster));
			mapMonster[zombieY][zombieX] = -3;
			zombieY *= GameController.getScale() * 16;
			zombieX *= GameController.getScale() * 16;

			Zombie zombie = new Zombie(zombieX, zombieY, 1, 5, 1, player);

			monsters.add(zombie);
			this.getChildren().add(zombie);
		}
	}

	private boolean canSpawnAt(int x, int y, int[][] map) {
		// Check 3x3 surrounding area
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				int checkY = y + i;
				int checkX = x + j;
				if (checkY < 0 || checkY >= map.length || checkX < 0 || checkX >= map[0].length
						|| map[checkY][checkX] != 0) {
					return false; // Not a valid spawn location
				}
			}
		}
		return true;
	}

	private void startTimer() {
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				update();
			}
		};
		timer.start();
	}

	private void update() {
		double dx = 0, dy = 0;

		boolean movingDown = false;
		boolean movingUp = false;
		boolean movingRight = false;
		boolean movingLeft = false;

		if (GameController.getKeyboardController().isBag() && pass) {
//			Thread thread = new Thread(() -> {
//				Platform.runLater(() -> {
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

			// ponG บอกมา
			if (!mother.getChildren().contains(bag)) {
				mother.getChildren().add(bag);
			} else {
				System.out.println("Bag is already added!");
			}

			mother.getChildren().addAll(inv, closeButton);
			pass = false;
//				});
//			});
//			thread.start();
		} else if (!GameController.getKeyboardController().isBag() && !pass) {
//			Platform.runLater(() -> {
			mother.getChildren().removeAll(inv, bag, closeButton);
			bag = null;
			inv = null;
			closeButton = null;
			pass = true;
//			});
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
		this.setLayoutX(newLayoutX);
		this.setLayoutY(newLayoutY);

		this.player.setX(this.player.getX() + dx);
		this.player.setY(this.player.getY() + dy);
		this.player.setLayoutX(this.player.getX());
		this.player.setLayoutY(this.player.getY());
//		adjustViewOrder();

		// Floating item
		Iterator<FloatingItem> iterator = floatingItems.iterator();
		while (iterator.hasNext()) {
			FloatingItem item = iterator.next();
			int playerFootGridX = (int) ((this.player.getX() + (8 + 24 - 8) * GameController.getScale())
					/ GameController.getScale() / 16);
			int playerFootGridY = (int) ((this.player.getY() + (32) * GameController.getScale())
					/ GameController.getScale() / 16);
			int itemCenGridX = (int) ((item.getLayoutX() + 8 * GameController.getScale()) / GameController.getScale()
					/ 16);
			int itemCenGridY = (int) ((item.getLayoutY() + 8 * GameController.getScale()) / GameController.getScale()
					/ 16);

			if (Math.abs(playerFootGridX - itemCenGridX) <= 1 && Math.abs(playerFootGridY - itemCenGridY) <= 1) {
//				System.out.println("start follow");
				double itemX = (item.getLayoutX() + 8 * GameController.getScale());
				double itemY = (item.getLayoutY() + 8 * GameController.getScale());

				double playerCenX = (this.player.getX() + (8 + 24 - 8) * GameController.getScale());
				double playerCenY = (this.player.getY() + (32) * GameController.getScale());

				double itemDx = playerCenX - itemX;
				double itemDy = playerCenY - itemY;
				double distance = Math.sqrt(itemDx * itemDx + itemDy * itemDy);

				if (distance > 1) {
					double speed = 2;
					double moveX = (itemDx / distance) * speed;
					double moveY = (itemDy / distance) * speed;

					item.setLayoutX(item.getLayoutX() + moveX);
					item.setLayoutY(item.getLayoutY() + moveY);
				}

			}
			if (playerFootGridX == itemCenGridX && playerFootGridY == itemCenGridY) {
//				System.err.println("item over u");
				Player.addItem(new Item(item.getRow(), item.getCol()), Player.containerGrid);
				this.getChildren().remove(item);
				iterator.remove();

			}
		}

		// Update monster
		for (Monster monster : getMonsters()) {
			Platform.runLater(() -> monster.update());

		}
	}

//	private void adjustViewOrder() {
//		for (Zombie zombie : zombies) {
//			if (player.getY() + 1 * GameController.getScale() >= zombie.getY()) {
//				player.setViewOrder(-501); // player in front
//				zombie.setViewOrder(-500); // zombie behind player
//			} else {
//				player.setViewOrder(-500); // player behind zombie
//				zombie.setViewOrder(-501); // zombie in front
//			}
//		}
//
//		for (Slime slime : slimes) {
//			if (player.getY() >= slime.getY()) {
//				player.setViewOrder(-501);
//				slime.setViewOrder(-500);
//			} else {
//				player.setViewOrder(-500);
//				slime.setViewOrder(-501);
//			}
//		}
//	}

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

	public void enterNextFloor() {
		System.out.println("Entering next floor...");
		updateOverlaySize();
		transitionScreen.setVisible(true);

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
						getChildren().remove(getPlayer());
						getChildren().removeAll(floatingItems);
						getChildren().removeAll(blocks);

						floatingItems.clear();
						blocks.clear();

						generateNextMap();

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
		fadeOut.setOnFinished(event -> {
			transitionScreen.setVisible(false);
			player.setCanMove(true);
		});

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
		setPlayerHealth(player.getHealth() - damage);
	}

	public void setPlayerHealth(int health) {
		player.setHealth(health);
		GameController.getMainPane().setHBar(player.getHealth());
	}

	public void setPlayerStamina(int stamina) {
		player.setStamina(stamina);
		GameController.getMainPane().setSBar(player.getStamina());
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
						getChildren().remove(getPlayer());
						getChildren().removeAll(blocks);
						getChildren().removeAll(monsters);
//						getChildren().removeAll(slimes);
//						getChildren().removeAll(zombies);

						blocks.clear();
						monsters.clear();
//						slimes.clear();
//						zombies.clear();

						// Regenerate world
//						generateRandomBlocks();
//						generateRandomLadder();
						// generateRandomSlimes();
						// generateRandomZombies();

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
//						MainPane mainPane = (MainPane) mother;
//						if (mainPane != null) {
//							mainPane.getHBar().setBar(100); // Reset health to full
//						}

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

	public List<FloatingItem> getfloatingItems() {
		return floatingItems;
	}

	public List<Monster> getMonsters() {
		return monsters;
	}

//	public void setBlocks(List<Block> blocks) {
//		this.blocks = blocks;
//	}

	public int[][] getMapBlock() {
		return mapBlock;
	}

	public int getLadderX() {
		return ladderX;
	}

	public int getLadderY() {
		return ladderY;
	}

	public Player getPlayer() {
		return player;
	}

}